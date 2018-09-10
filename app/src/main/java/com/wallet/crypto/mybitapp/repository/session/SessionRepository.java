package com.wallet.crypto.mybitapp.repository.session;

import com.wallet.crypto.mybitapp.BuildConfig;
import com.wallet.crypto.mybitapp.C;
import com.wallet.crypto.mybitapp.entity.ErrorEnvelope;
import com.wallet.crypto.mybitapp.entity.NetworkInfo;
import com.wallet.crypto.mybitapp.entity.Session;
import com.wallet.crypto.mybitapp.entity.Wallet;
import com.wallet.crypto.mybitapp.entity.exception.ApiErrorException;
import com.wallet.crypto.mybitapp.repository.PreferenceRepositoryType;

import java.util.HashSet;
import java.util.Set;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by Dasha on 20.06.2018
 */
public class SessionRepository implements SessionRepositoryType {

    private final Object listenersLock = new Object();

    private final NetworkInfo NETWORK = new NetworkInfo(
            BuildConfig.NETWORK_NAME,
            C.ETH_SYMBOL,
            BuildConfig.RPC_SERVER_URL,
            BuildConfig.BACKEND_URL,
            BuildConfig.ETHERSCAN_URL,
            BuildConfig.CHAIN_ID,
            BuildConfig.USE_MAIN_NET);

    private final Set<OnSessionChangeListener> onSessionChangeListeners = new HashSet<>();
    private final PreferenceRepositoryType preferenceRepository;

    public SessionRepository(PreferenceRepositoryType preferenceRepository) {
        this.preferenceRepository = preferenceRepository;
    }

    @Override
    public void addOnSessionChangeListener(OnSessionChangeListener onSessionChangeListener) {
        this.onSessionChangeListeners.add(onSessionChangeListener);
    }

    @Override
    public Single<Session> getSession() {
        return Single.fromCallable(() -> {
            Wallet defaultWallet = this.preferenceRepository.getDefaultWallet();
            if (defaultWallet != null) {
                return new Session(getNetwork(), defaultWallet);
            }
            throw new ApiErrorException(new ErrorEnvelope(C.ErrorCode.NO_SESSION, null));
        });
    }

    @Override
    public Completable clearDefaultWallet() {
        return Completable.fromAction(preferenceRepository::clearDefaultWallet)
                .andThen(getSession())
                .flatMapCompletable(this::notifySessionChanged);
    }

    @Override
    public Completable setWallet(Wallet wallet) {
        return Completable.fromAction(() -> this.preferenceRepository.setDefaultWallet(wallet))
                .andThen(getSession())
                .flatMapCompletable(this::notifySessionChanged);
    }

    @Override
    public NetworkInfo getNetwork() {
        return NETWORK;
    }

    private Completable notifySessionChanged(Session session) {
        return Completable.fromAction(() -> {
            synchronized (this.listenersLock) {
                for (OnSessionChangeListener onSessionChanged : this.onSessionChangeListeners) {
                    onSessionChanged.onSessionChanged(session);
                }
            }
        });
    }

}
