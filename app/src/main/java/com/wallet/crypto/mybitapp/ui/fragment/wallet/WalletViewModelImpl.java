package com.wallet.crypto.mybitapp.ui.fragment.wallet;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.wallet.crypto.mybitapp.R;
import com.wallet.crypto.mybitapp.entity.Balance;
import com.wallet.crypto.mybitapp.entity.ErrorEnvelope;
import com.wallet.crypto.mybitapp.entity.Session;
import com.wallet.crypto.mybitapp.entity.Token;
import com.wallet.crypto.mybitapp.entity.Wallet;
import com.wallet.crypto.mybitapp.interact.FetchTokensInteract;
import com.wallet.crypto.mybitapp.interact.GetSessionInteract;
import com.wallet.crypto.mybitapp.repository.session.OnSessionChangeListener;
import com.wallet.crypto.mybitapp.repository.session.SessionRepositoryType;
import com.wallet.crypto.mybitapp.router.ReceiveRouter;
import com.wallet.crypto.mybitapp.router.SendTokenRouter;
import com.wallet.crypto.mybitapp.util.DecimalUtil;

import java.math.BigDecimal;

public class WalletViewModelImpl extends WalletViewModel implements OnSessionChangeListener {

    private final Balance emptyBalance = new Balance(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);

    private final MutableLiveData<Session> session = new MutableLiveData<>();
    private final MutableLiveData<BigDecimal> totalBalance = new MutableLiveData<>();
    private final MutableLiveData<Token[]> tokens = new MutableLiveData<>();

    private final FetchTokensInteract fetchTokensInteract;
    private final SendTokenRouter sendTokenRouter;
    private final ReceiveRouter receiveRouter;
    private final GetSessionInteract getSessionInteract;

    WalletViewModelImpl(
            FetchTokensInteract fetchTokensInteract,
            SendTokenRouter sendTokenRouter,
            ReceiveRouter receiveRouter,
            GetSessionInteract getSessionInteract,
            SessionRepositoryType sessionRepository) {
        this.fetchTokensInteract = fetchTokensInteract;
        this.sendTokenRouter = sendTokenRouter;
        this.receiveRouter = receiveRouter;
        this.getSessionInteract = getSessionInteract;
        sessionRepository.addOnSessionChangeListener(this);
    }

    @Override
    public void prepare() {
        progress.postValue(true);
        compositeDisposable.add(getSessionInteract
                .get()
                .subscribe(this::onSession, this::onError));
    }

    @Override
    public void onSessionChanged(Session session) {
        this.session.postValue(session);
        clearTokens();
        totalBalance.postValue(BigDecimal.ZERO);
        fetchTokens(session);
    }

    private void clearTokens() {
        Token[] tokens = this.tokens.getValue();
        if (tokens == null)
            return;

        for (Token token : tokens) {
            token.setBalance(emptyBalance);
        }
        this.tokens.postValue(tokens);
    }

    private void onSession(Session session) {
        this.session.postValue(session);
        fetchTokens(session);
    }

    @Override
    public LiveData<BigDecimal> totalBalance() {
        return totalBalance;
    }

    @Override
    public LiveData<Token[]> tokens() {
        return tokens;
    }

    @Override
    public void fetchTokens() {
        fetchTokens(session.getValue());
    }

    private void fetchTokens(Session session) {
        progress.postValue(true);
        compositeDisposable.clear();
        if (session != null) {
            compositeDisposable.add(fetchTokensInteract
                    .fetch(session.wallet)
                    .subscribe(this::onTokens, this::onError, this::onComplete));
        }
    }

    private void onTokens(Token[] tokens) {
        BigDecimal totalUsdBalance = BigDecimal.ZERO;

        for (Token token : tokens) {
            totalUsdBalance = totalUsdBalance.add(token.getBalance().usdPrice);
        }

        this.tokens.postValue(tokens);

        totalUsdBalance = DecimalUtil.stripZerosForZero(totalUsdBalance);
        totalBalance.postValue(totalUsdBalance);
    }

    private void onComplete() {
        progress.postValue(false);
    }

    public void showSend(Context context, String contractAddress) {
        if (session.getValue() == null)
            return;

        Wallet wallet = session.getValue().wallet;

        if (wallet.type == Wallet.WATCH) {
            error.postValue(new ErrorEnvelope(context.getString(R.string.error_send)));
        } else {
            sendEth(context, contractAddress);
        }
    }

    private void sendEth(Context context, String address) {
        if (tokens.getValue() != null && tokens.getValue().length > 0) {
            Token defToken = tokens.getValue()[0];
            sendTokenRouter.open(context,
                    address,
                    defToken.getTokenInfo().symbol,
                    defToken.getTokenInfo().decimals,
                    defToken.getBalance().cryptoBalance);
        }
    }

    @Override
    public void showSendToken(Context context, String address, String symbol, int decimals,
                              BigDecimal balance) {
        if (session.getValue() == null)
            return;

        Wallet wallet = session.getValue().wallet;

        if (wallet.type == Wallet.WATCH) {
            error.postValue(new ErrorEnvelope(context.getString(R.string.error_send)));
        } else {
            sendTokenRouter.open(context, address, symbol, decimals, balance);
        }
    }

    @Override
    public void showMyAddress(Context context) {
        if (session.getValue() != null) {
            receiveRouter.open(context, session.getValue().wallet);
        }
    }
}