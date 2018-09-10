package com.wallet.crypto.mybitapp.repository;

import com.wallet.crypto.mybitapp.entity.Session;
import com.wallet.crypto.mybitapp.entity.TokenInfo;
import com.wallet.crypto.mybitapp.entity.Transaction;
import com.wallet.crypto.mybitapp.entity.Wallet;
import com.wallet.crypto.mybitapp.entity.exception.ServiceException;
import com.wallet.crypto.mybitapp.repository.session.SessionRepositoryType;
import com.wallet.crypto.mybitapp.repository.wallet.WalletDataSourceType;
import com.wallet.crypto.mybitapp.service.BlockExplorerClientType;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class TransactionRepository implements TransactionRepositoryType {

    private final SessionRepositoryType sessionRepository;
    private final WalletDataSourceType accountKeystoreService;
    private final TransactionLocalSource transactionLocalSource;
    private final BlockExplorerClientType blockExplorerClient;
    private final DefaultTokensRepositoryType defaultTokensRepository;

    private static final int START_PAGE_IDX = 1;

    public TransactionRepository(
            SessionRepositoryType sessionRepository,
            WalletDataSourceType accountKeystoreService,
            TransactionLocalSource inMemoryCache,
            BlockExplorerClientType blockExplorerClient,
            DefaultTokensRepositoryType defaultTokensRepository) {
        this.sessionRepository = sessionRepository;
        this.accountKeystoreService = accountKeystoreService;
        this.blockExplorerClient = blockExplorerClient;
        this.transactionLocalSource = inMemoryCache;
        this.defaultTokensRepository = defaultTokensRepository;

        this.sessionRepository.addOnSessionChangeListener(session -> onSessionChanged());
    }

    @Override
    public Observable<Transaction[]> fetchTransactions(final Session session) {
        return fetchTransactions(session, START_PAGE_IDX, true);
    }

    @Override
    public Observable<Transaction[]> fetchTransactions(Session session, int page) {
        return fetchTransactions(session, page, false);
    }

    private Observable<Transaction[]> fetchTransactions(Session session, int page, boolean clear) {
        return Observable.create(e -> {
            Transaction[] transactions = fetchFromCache(session, page).blockingGet();

            if (transactions != null && transactions.length > 0) {
                e.onNext(transactions);
            }

            try {
                transactions = fetchFromNetwork(session, page, clear)
                        .andThen(fetchFromCache(session, page)).blockingGet();
                e.onNext(transactions);
            } catch (Exception ex) {
                //ignore
            }
            e.onComplete();
        });
    }

    private Single<Transaction[]> fetchFromCache(Session session, int page) {
        return transactionLocalSource.fetchTransaction(session, page);
    }

    private Completable fetchFromNetwork(Session session, int page, boolean clear) {
        return Completable.fromAction(() -> {
            Transaction[] transactions = blockExplorerClient.fetchTransactions(session, page).blockingFirst();
            TokenInfo myBitToken = defaultTokensRepository.getMybitToken(session.networkInfo);

            List<Transaction> filtered = Observable.just(Arrays.asList(transactions))
                    .subscribeOn(Schedulers.io())
                    .flatMapIterable(items -> items)
                    .filter(t -> t.operations.length == 0 ||
                            t.operations[0].contract == null ||
                            t.operations[0].contract.address.equalsIgnoreCase(myBitToken.address))
                    .toList()
                    .blockingGet();

            transactions = new Transaction[filtered.size()];
            transactions = filtered.toArray(transactions);
            if (clear) {
                transactionLocalSource.clear();
            }
            transactionLocalSource.putTransactions(session, transactions, page);
        });
    }

    @Override
    public Single<String> createTransaction(Wallet from, String toAddress, BigInteger subunitAmount, BigInteger gasPrice, BigInteger gasLimit, byte[] data, String password) {
        final Web3j web3j = Web3jFactory.build(new HttpService(sessionRepository.getNetwork().rpcServerUrl));

        return Single.fromCallable(() -> {
            EthGetTransactionCount ethGetTransactionCount = web3j
                    .ethGetTransactionCount(from.address, DefaultBlockParameterName.LATEST)
                    .send();
            return ethGetTransactionCount.getTransactionCount();
        })
                .flatMap(nonce ->
                        accountKeystoreService.signTransaction(from, password, toAddress, subunitAmount, gasPrice,
                                gasLimit, nonce.longValue(), data, sessionRepository.getNetwork().chainId))
                .flatMap(signedMessage -> Single.fromCallable(() -> {
                    EthSendTransaction raw = web3j
                            .ethSendRawTransaction(Numeric.toHexString(signedMessage))
                            .send();
                    if (raw.hasError()) {
                        throw new ServiceException(raw.getError().getMessage());
                    }
                    return raw.getTransactionHash();
                }));
    }

    @Override
    public Completable clearPending() {
        return Completable.fromAction(transactionLocalSource::clear);
    }

    private void onSessionChanged() {
        transactionLocalSource.clear();
    }
}
