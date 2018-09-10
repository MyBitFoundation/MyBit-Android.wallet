package com.wallet.crypto.mybitapp.repository;

import android.util.Log;

import com.wallet.crypto.mybitapp.C;
import com.wallet.crypto.mybitapp.entity.Balance;
import com.wallet.crypto.mybitapp.entity.NetworkInfo;
import com.wallet.crypto.mybitapp.entity.Ticker;
import com.wallet.crypto.mybitapp.entity.Token;
import com.wallet.crypto.mybitapp.entity.TokenInfo;
import com.wallet.crypto.mybitapp.entity.Wallet;
import com.wallet.crypto.mybitapp.repository.local.BalanceTokenSource;
import com.wallet.crypto.mybitapp.repository.local.TokenLocalSource;
import com.wallet.crypto.mybitapp.repository.session.SessionRepositoryType;
import com.wallet.crypto.mybitapp.service.TickerService;
import com.wallet.crypto.mybitapp.util.DecimalUtil;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.OkHttpClient;

import static com.wallet.crypto.mybitapp.util.BalanceUtils.weiToEth;

public class TokenRepository implements TokenRepositoryType {

    private final Balance emptyBalance = new Balance(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);

    private final TokenLocalSource tokenLocalSource;
    private final DefaultTokensRepositoryType defaultTokensRepository;
    private final BalanceTokenSource balanceTokenSource;
    private final TickerService tickerService;
    private final OkHttpClient httpClient;
    private final SessionRepositoryType sessionRepository;
    private Web3j web3j;
    private HttpService httpService;

    public TokenRepository(
            TokenLocalSource tokenLocalSource,
            BalanceTokenSource balanceTokenSource,
            DefaultTokensRepositoryType defaultTokensRepository,
            OkHttpClient okHttpClient,
            TickerService tickerService,
            SessionRepositoryType sessionRepository) {
        this.tokenLocalSource = tokenLocalSource;
        this.defaultTokensRepository = defaultTokensRepository;
        this.balanceTokenSource = balanceTokenSource;
        this.tickerService = tickerService;
        this.httpClient = okHttpClient;
        this.sessionRepository = sessionRepository;

        buildClients(sessionRepository.getNetwork());
        this.sessionRepository.addOnSessionChangeListener(session -> buildClients(session.networkInfo));
    }

    private void buildClients(NetworkInfo network) {
        web3j = Web3jFactory.build(new HttpService(network.rpcServerUrl, httpClient, false));
        httpService = new HttpService(network.rpcServerUrl, httpClient, false);
    }

    @Override
    public Completable clear(Wallet wallet) {
        NetworkInfo defaultNetwork = sessionRepository.getNetwork();
        return Completable.fromAction(() -> tokenLocalSource.clear(defaultNetwork, wallet))
                .andThen(balanceTokenSource.clear(defaultNetwork, wallet));
    }

    @Override
    public Observable<Token[]> fetch(String walletAddress) {
        return Observable.create(e -> {
            NetworkInfo defaultNetwork = sessionRepository.getNetwork();
            Wallet wallet = new Wallet(walletAddress);

            TokenInfo[] tokenInfos = defaultTokensRepository.getTokens(defaultNetwork);
            Map<String, Balance> balances = balanceTokenSource.fetch(defaultNetwork, wallet).blockingGet();
            Token[] tokens = mergeTokensWithBalances(tokenInfos, balances);
            if (tokens != null && tokens.length > 0) {
                e.onNext(tokens);
            }

            try {
                updateBalancesCache(wallet, tokenInfos)
                        .onErrorResumeNext(throwable -> balanceTokenSource.clear(defaultNetwork, wallet))
                        .blockingAwait();
            } catch (Exception ex) {
                //ignore
            }

            //balances = balanceTokenSource.fetch(defaultNetwork, wallet).blockingGet();
            //tokens = mergeTokensWithBalances(tokenInfos, balances);
            e.onNext(tokens);
            e.onComplete();
        });
    }

    private Token[] mergeTokensWithBalances(TokenInfo[] tokenInfos, Map<String, Balance> balances) {
        Token[] tokens = new Token[tokenInfos.length];
        for (int i = 0; i < tokenInfos.length; ++i) {
            Balance balance = balances.get(tokenInfos[i].address);
            if (balance == null) {
                balance = emptyBalance;
            }
            tokens[i] = new Token(tokenInfos[i], balance);
        }

        return tokens;
    }

    public static byte[] createTokenTransferData(String to, BigInteger tokenAmount) {
        List<Type> params = Arrays.<Type>asList(new Address(to), new Uint256(tokenAmount));

        List<TypeReference<?>> returnTypes = Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {
        });

        Function function = new Function("transfer", params, returnTypes);
        String encodedFunction = FunctionEncoder.encode(function);
        return Numeric.hexStringToByteArray(Numeric.cleanHexPrefix(encodedFunction));
    }

    private Single<Ticker> getTicker(String symbol) {
        return tickerService.fetchTickerPrice(symbol);
    }

    private Single<BigInteger> balanceInWei(Wallet wallet) {
        return Single.fromCallable(() -> Web3jFactory
                .build(httpService)
                .ethGetBalance(wallet.address, DefaultBlockParameterName.LATEST)
                .send()
                .getBalance());
    }

    private Completable updateBalancesCache(Wallet wallet, TokenInfo[] tokenInfos) {
        return Completable.fromAction(() -> {
            NetworkInfo defaultNetwork = sessionRepository.getNetwork();
            String defEthAddress = defaultTokensRepository.getEthereumToken(defaultNetwork).address;

            for (TokenInfo tokenInfo : tokenInfos) {
                try {
                    BigDecimal cryptoBalance = getCryptoBalance(wallet, tokenInfo, defEthAddress, C.TOKEN_BALANCE_SCALE);
                    Ticker ticker = getTicker(tokenInfo, defEthAddress).blockingGet();
                    BigDecimal usdBalance = new BigDecimal(ticker.price).multiply(cryptoBalance).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal percentChange = ticker.percentChange24h == null || ticker.percentChange24h.isEmpty()
                            ? BigDecimal.ZERO
                            : new BigDecimal(ticker.percentChange24h);
                    Balance balance = new Balance(
                            DecimalUtil.stripZerosForZero(cryptoBalance),
                            DecimalUtil.stripZerosForZero(usdBalance),
                            DecimalUtil.stripZerosForZero(percentChange));

                    balanceTokenSource.put(defaultNetwork, wallet, balance, tokenInfo.address).blockingAwait();

                } catch (Exception e1) {
                    Log.d("TOKEN", "Err", e1);
                    /* Quietly */
                }
            }
        });
    }

    private Single<Ticker> getTicker(TokenInfo tokenInfo, String defEthAddress) {
        return tokenInfo.address.equalsIgnoreCase(defEthAddress)
                ? getEthTicker()
                : getTicker(tokenInfo.symbol.toUpperCase());
    }

    private Single<Ticker> getEthTicker() {
        return tickerService.fetchTickerPrice(sessionRepository.getNetwork().symbol);
    }

    private BigDecimal getCryptoBalance(Wallet wallet, TokenInfo tokenInfo, String defEthAddress, int scale) {
        return tokenInfo.address.equalsIgnoreCase(defEthAddress)
                ? getEthCryptoBalance(wallet, scale)
                : getCryptoBalance(wallet.address, tokenInfo, scale);
    }

    private BigDecimal getEthCryptoBalance(Wallet wallet, int scale) {
        BigInteger ethBalance = balanceInWei(wallet).blockingGet();
        try {
            String eth = weiToEth(ethBalance, scale);
            return new BigDecimal(eth);
        } catch (Exception ex) {
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal getCryptoBalance(String walletAddress, TokenInfo tokenInfo, int scale) {
        try {
            org.web3j.abi.datatypes.Function function = balanceOf(walletAddress);
            String responseValue = callSmartContractFunction(function, tokenInfo.address, walletAddress);

            List<Type> response = FunctionReturnDecoder.decode(
                    responseValue, function.getOutputParameters());
            if (response.size() == 1) {
                BigDecimal balance = new BigDecimal(((Uint256) response.get(0)).getValue());
                balance = balance.movePointLeft(tokenInfo.decimals).setScale(scale, BigDecimal.ROUND_HALF_UP);
                return balance;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return BigDecimal.ZERO;
    }

    private static org.web3j.abi.datatypes.Function balanceOf(String owner) {
        return new org.web3j.abi.datatypes.Function(
                "balanceOf",
                Collections.singletonList(new Address(owner)),
                Collections.singletonList(new TypeReference<Uint256>() {
                }));
    }

    private String callSmartContractFunction(
            org.web3j.abi.datatypes.Function function, String contractAddress, String walletAddress) throws Exception {
        String encodedFunction = FunctionEncoder.encode(function);

        org.web3j.protocol.core.methods.response.EthCall response = web3j.ethCall(
                Transaction.createEthCallTransaction(walletAddress, contractAddress, encodedFunction),
                DefaultBlockParameterName.LATEST)
                .sendAsync().get();

        return response.getValue();
    }
}
