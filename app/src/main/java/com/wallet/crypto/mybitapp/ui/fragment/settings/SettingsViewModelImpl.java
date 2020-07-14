package com.wallet.crypto.mybitapp.ui.fragment.settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import com.wallet.crypto.mybitapp.entity.Session;
import com.wallet.crypto.mybitapp.entity.Wallet;
import com.wallet.crypto.mybitapp.interact.FindDefaultWalletInteract;
import com.wallet.crypto.mybitapp.interact.PassCodeInteract;
import com.wallet.crypto.mybitapp.repository.session.OnSessionChangeListener;
import com.wallet.crypto.mybitapp.repository.session.SessionRepositoryType;
import com.wallet.crypto.mybitapp.router.EmailRouter;
import com.wallet.crypto.mybitapp.router.ManageWalletsRouter;
import com.wallet.crypto.mybitapp.router.RateUsRouter;
import com.wallet.crypto.mybitapp.router.SocialRouter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SettingsViewModelImpl extends SettingsViewModel implements OnSessionChangeListener {

    private final MutableLiveData<Wallet> defaultWallet = new MutableLiveData<>();
    private final MutableLiveData<String> defaultNetworkSymbol = new MutableLiveData<>();
    private final MutableLiveData<Boolean> pinIsOptioned = new MutableLiveData<>();

    private final FindDefaultWalletInteract findDefaultWalletInteract;
    private final PassCodeInteract passCodeInteract;
    private final ManageWalletsRouter manageWalletsRouter;
    private final SocialRouter socialRouter;
    private final EmailRouter emailRouter;
    private final RateUsRouter rateUsRouter;

    private final SessionRepositoryType sessionRepository;

    SettingsViewModelImpl(FindDefaultWalletInteract findDefaultWalletInteract,
                          PassCodeInteract passCodeInteract,
                          ManageWalletsRouter manageWalletsRouter,
                          SocialRouter socialRouter,
                          EmailRouter emailRouter,
                          RateUsRouter rateUsRouter,
                          SessionRepositoryType sessionRepository) {
        this.findDefaultWalletInteract = findDefaultWalletInteract;
        this.passCodeInteract = passCodeInteract;
        this.manageWalletsRouter = manageWalletsRouter;
        this.socialRouter = socialRouter;
        this.emailRouter = emailRouter;
        this.rateUsRouter = rateUsRouter;
        this.sessionRepository = sessionRepository;
        this.sessionRepository.addOnSessionChangeListener(this);
    }

    @Override
    public void prepare() {
        reInitDefaultWallet();

        compositeDisposable.add(passCodeInteract
                .checkIsOptioned()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pinIsOptioned::postValue, t -> {
                }));

        compositeDisposable.add(passCodeInteract
                .checkIsOptionedChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pinIsOptioned::postValue, t -> {
                })
        );
    }

    @Override
    public void reInitDefaultWallet() {
        compositeDisposable.add(findDefaultWalletInteract
                .find()
                .subscribe(defaultWallet::postValue, this::onError));
    }

    @Override
    public LiveData<Wallet> defaultWallet() {
        return defaultWallet;
    }

    @Override
    public void showManageWallets(Context context) {
        manageWalletsRouter.openForResult(context, false);
    }

    @Override
    public void showSocial(Context context, String uri) {
        socialRouter.open(context, uri);
    }

    @Override
    public void showContactUs(Context context) {
        emailRouter.open(context);
    }

    @Override
    public void rateUs(Context context) {
        rateUsRouter.open(context);
    }

    @Override
    public void onSessionChanged(Session session) {
        prepare();
    }

    @Override
    public LiveData<Boolean> pinIsOptioned() {
        return pinIsOptioned;
    }
}