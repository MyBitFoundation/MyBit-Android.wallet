package com.wallet.crypto.mybitapp.ui.activity.wallets;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.crashlytics.android.Crashlytics;
import com.wallet.crypto.mybitapp.C;
import com.wallet.crypto.mybitapp.entity.ErrorEnvelope;
import com.wallet.crypto.mybitapp.entity.Wallet;
import com.wallet.crypto.mybitapp.interact.CreateWalletInteract;
import com.wallet.crypto.mybitapp.interact.DeleteWalletInteract;
import com.wallet.crypto.mybitapp.interact.ExportWalletInteract;
import com.wallet.crypto.mybitapp.interact.FetchWalletsInteract;
import com.wallet.crypto.mybitapp.interact.FindDefaultWalletInteract;
import com.wallet.crypto.mybitapp.interact.PassCodeInteract;
import com.wallet.crypto.mybitapp.interact.DefaultWalletInteract;
import com.wallet.crypto.mybitapp.router.ImportWalletRouter;
import com.wallet.crypto.mybitapp.router.MainRouter;
import com.wallet.crypto.mybitapp.ui.widget.receiver.NetworkChangeReceiver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.wallet.crypto.mybitapp.C.IMPORT_REQUEST_CODE;

public class WalletsViewModelImpl extends WalletsViewModel {
    private final CreateWalletInteract createWalletInteract;
    private final DefaultWalletInteract defaultWalletInteract;
    private final DeleteWalletInteract deleteWalletInteract;
    private final FetchWalletsInteract fetchWalletsInteract;
    private final FindDefaultWalletInteract findDefaultWalletInteract;
    private final ExportWalletInteract exportWalletInteract;
    private final PassCodeInteract passCodeInteract;
    private final ImportWalletRouter importWalletRouter;
    private final MainRouter mainRouter;

    private final MutableLiveData<Wallet[]> wallets = new MutableLiveData<>();
    private final MutableLiveData<Object> noWallets = new MutableLiveData<>();
    private final MutableLiveData<Wallet> defaultWallet = new MutableLiveData<>();
    private final MutableLiveData<Wallet> createdWallet = new MutableLiveData<>();
    private final MutableLiveData<ErrorEnvelope> createWalletError = new MutableLiveData<>();
    private final MutableLiveData<String> exportedStore = new MutableLiveData<>();
    private final MutableLiveData<ErrorEnvelope> exportWalletError = new MutableLiveData<>();

    WalletsViewModelImpl(
            CreateWalletInteract createWalletInteract,
            DefaultWalletInteract defaultWalletInteract,
            DeleteWalletInteract deleteWalletInteract,
            FetchWalletsInteract fetchWalletsInteract,
            FindDefaultWalletInteract findDefaultWalletInteract,
            ExportWalletInteract exportWalletInteract,
            ImportWalletRouter importWalletRouter,
            MainRouter mainRouter,
            PassCodeInteract passCodeInteract,
            NetworkChangeReceiver networkChangeReceiver) {
        super(networkChangeReceiver);
        this.createWalletInteract = createWalletInteract;
        this.defaultWalletInteract = defaultWalletInteract;
        this.deleteWalletInteract = deleteWalletInteract;
        this.fetchWalletsInteract = fetchWalletsInteract;
        this.findDefaultWalletInteract = findDefaultWalletInteract;
        this.importWalletRouter = importWalletRouter;
        this.exportWalletInteract = exportWalletInteract;
        this.mainRouter = mainRouter;
        this.passCodeInteract = passCodeInteract;
        fetchWallets();
    }

    @Override
    public LiveData<Wallet[]> wallets() {
        return wallets;
    }

    @Override
    public LiveData<Object> noWallets() {
        return noWallets;
    }

    @Override
    public LiveData<Wallet> defaultWallet() {
        return defaultWallet;
    }

    @Override
    public LiveData<Wallet> createdWallet() {
        return createdWallet;
    }

    @Override
    public LiveData<String> exportedStore() {
        return exportedStore;
    }

    @Override
    public void setDefaultWallet(Wallet wallet) {
        compositeDisposable.add(defaultWalletInteract
                .set(wallet)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> onDefaultWalletChanged(wallet), this::onError));
    }

    @Override
    public void deleteWallet(Wallet wallet) {
        progress.setValue(true);

        compositeDisposable.add(findDefaultWalletInteract
                .find()
                .flatMap(defaultWallet -> {
                    if (defaultWallet.sameAddress(wallet.address)) {
                        Wallet[] wallets = fetchWalletsInteract.fetch().blockingGet();

                        if (wallets.length > 1) {
                            setDefaultWallet(wallets[0]);
                        }
                    }

                    return deleteWalletInteract.delete(wallet);
                })
                .subscribeOn(Schedulers.io())
                .subscribe(this::onFetchWallets, this::onError));
    }

    private void onFetchWallets(Wallet[] items) {
        progress.postValue(false);
        initWallets(items);
        compositeDisposable.add(findDefaultWalletInteract
                .find()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onDefaultWalletChanged, t -> { }));
    }

    private void initWallets(Wallet[] items) {
        if (items == null || items.length == 0) {
            removePin();
            noWallets.postValue(new Object());
        } else {
            wallets.postValue(items);
        }
    }

    private void onDefaultWalletChanged(Wallet wallet) {
        progress.postValue(false);
        defaultWallet.postValue(wallet);
    }

    @Override
    public void fetchWallets() {
        progress.postValue(true);
        compositeDisposable.add(fetchWalletsInteract
                .fetch()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onFetchWallets, this::onError));
    }

    @Override
    public void newWallet() {
        progress.setValue(true);
        compositeDisposable.add(createWalletInteract
                .create()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(account -> {
                    fetchWallets();
                    createdWallet.postValue(account);
                }, this::onCreateWalletError));
    }

    @Override
    public void exportWallet(Wallet wallet, String storePassword) {
        compositeDisposable.add(exportWalletInteract
                .export(wallet, storePassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(exportedStore::postValue, this::onExportError));
    }

    private void onExportError(Throwable throwable) {
        Crashlytics.logException(throwable);
        exportWalletError.postValue(new ErrorEnvelope(C.ErrorCode.UNKNOWN, null));
    }

    private void onCreateWalletError(Throwable throwable) {
        Crashlytics.logException(throwable);
        createWalletError.postValue(new ErrorEnvelope(C.ErrorCode.UNKNOWN, null));
    }

    @Override
    public void importWallet(Activity activity) {
        importWalletRouter.openForResult(activity, IMPORT_REQUEST_CODE);
    }

    private void removePin() {
        passCodeInteract.remove()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    public void showMain(Activity activity) {
        mainRouter.open(activity, true);
    }
}