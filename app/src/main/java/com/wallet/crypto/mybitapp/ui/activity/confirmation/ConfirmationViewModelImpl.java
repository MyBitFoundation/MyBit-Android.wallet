package com.wallet.crypto.mybitapp.ui.activity.confirmation;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.wallet.crypto.mybitapp.C;
import com.wallet.crypto.mybitapp.entity.ErrorEnvelope;
import com.wallet.crypto.mybitapp.entity.GasModel;
import com.wallet.crypto.mybitapp.entity.GasSettings;
import com.wallet.crypto.mybitapp.entity.Session;
import com.wallet.crypto.mybitapp.entity.Wallet;
import com.wallet.crypto.mybitapp.interact.CreateTransactionInteract;
import com.wallet.crypto.mybitapp.interact.FetchGasSettingsInteract;
import com.wallet.crypto.mybitapp.interact.GetSessionInteract;
import com.wallet.crypto.mybitapp.repository.TokenRepository;
import com.wallet.crypto.mybitapp.router.GasSettingsRouter;
import com.wallet.crypto.mybitapp.ui.widget.receiver.NetworkChangeReceiver;
import com.wallet.crypto.mybitapp.util.BalanceUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class ConfirmationViewModelImpl extends ConfirmationViewModel {
    private final MutableLiveData<String> newTransaction = new MutableLiveData<>();
    private final MutableLiveData<Session> session = new MutableLiveData<>();
    private final MutableLiveData<GasSettings> gasSettings = new MutableLiveData<>();
    private final MutableLiveData<GasModel> gasModel = new MutableLiveData<>();

    private final GetSessionInteract getSessionInteract;
    private final FetchGasSettingsInteract fetchGasSettingsInteract;
    private final CreateTransactionInteract createTransactionInteract;

    private final GasSettingsRouter gasSettingsRouter;

    private boolean confirmationForTokenTransfer = false;

    public ConfirmationViewModelImpl(GetSessionInteract getSessionInteract,
                                     FetchGasSettingsInteract fetchGasSettingsInteract,
                                     CreateTransactionInteract createTransactionInteract,
                                     GasSettingsRouter gasSettingsRouter,
                                     NetworkChangeReceiver networkChangeReceiver) {
        super(networkChangeReceiver);
        this.getSessionInteract = getSessionInteract;
        this.fetchGasSettingsInteract = fetchGasSettingsInteract;
        this.createTransactionInteract = createTransactionInteract;
        this.gasSettingsRouter = gasSettingsRouter;
    }

    @Override
    public void sendAmount(String fromAddress, String toAddress, String contractAddress, BigInteger amount, BigDecimal balance) {
        progress.postValue(true);
        GasSettings gasSettings = gasSettings().getValue();

        if (gasSettings == null) {
            error.postValue(new ErrorEnvelope(C.ErrorCode.UNKNOWN, "Gas settings is null"));
            return;

        } else if (!isFundsSufficient(amount, balance, calculateFee(gasSettings))) {
            error.postValue(new ErrorEnvelope(C.ErrorCode.INSUFFICIENT_FUNDS, "Insufficient funds"));
            return;
        }

        if (confirmationForTokenTransfer) {
            createTokenTransfer(fromAddress, toAddress, gasSettings, contractAddress, amount);
        } else {
            createTransaction(fromAddress, toAddress, gasSettings, amount);
        }
    }

    @Override
    public LiveData<Session> session() {
        return session;
    }

    @Override
    public MutableLiveData<GasModel> gasModel() {
        return gasModel;
    }

    @Override
    public MutableLiveData<GasSettings> gasSettings() {
        return gasSettings;
    }

    @Override
    public LiveData<String> sendTransaction() {
        return newTransaction;
    }

    @Override
    public void prepare(boolean confirmationForTokenTransfer) {
        this.confirmationForTokenTransfer = confirmationForTokenTransfer;
        compositeDisposable.add(getSessionInteract
                .get()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSession, this::onError));
    }

    @Override
    public void openGasSettings(Activity context) {
        gasSettingsRouter.open(context, gasSettings.getValue());
    }

    private void createTransaction(String from, String to, GasSettings gasSettings, BigInteger amount) {
        compositeDisposable.add(createTransactionInteract
                .create(new Wallet(from), to, amount, gasSettings.gasPrice, gasSettings.gasLimit, null)
                .subscribe(this::onCreateTransaction, this::onError));
    }

    private void createTokenTransfer(String from, String to, GasSettings gasSettings, String contractAddress, BigInteger amount) {
        final byte[] data = TokenRepository.createTokenTransferData(to, amount);
        compositeDisposable.add(createTransactionInteract
                .create(new Wallet(from), contractAddress, BigInteger.valueOf(0), gasSettings.gasPrice, gasSettings.gasLimit, data)
                .subscribe(this::onCreateTransaction, this::onError));
    }

    private void onCreateTransaction(String transaction) {
        progress.postValue(false);
        newTransaction.postValue(transaction);
    }

    private void onSession(Session session) {
        this.session.postValue(session);
        if (gasSettings.getValue() == null) {
            onGasSettings(fetchGasSettingsInteract.fetch(confirmationForTokenTransfer));
        }
    }

    private void onGasSettings(GasSettings gasSettings) {
        String gasPrice = BalanceUtils.weiToGwei(gasSettings.gasPrice);
        BigDecimal networkFee = calculateFee(gasSettings);

        GasModel gasModel = new GasModel(gasPrice, gasSettings.gasLimit.toString(), networkFee.toPlainString());
        this.gasModel.postValue(gasModel);

        this.gasSettings.postValue(gasSettings);
    }

    private BigDecimal calculateFee(GasSettings gasSettings) {
        return BalanceUtils.weiToEth(gasSettings.gasPrice.multiply(gasSettings.gasLimit));
    }

    private boolean isFundsSufficient(BigInteger amount, BigDecimal balance, BigDecimal fee) {
        if (balance.compareTo(BigDecimal.ZERO) == 0) {
            return false;
        }

        try {
            BigDecimal value = new BigDecimal(amount.toString());
            return balance.add(fee).compareTo(value) >= 0;
        } catch (Exception ex) {
            return false;
        }
    }
}