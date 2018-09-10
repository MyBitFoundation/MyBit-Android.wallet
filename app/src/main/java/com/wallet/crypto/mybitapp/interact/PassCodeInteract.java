package com.wallet.crypto.mybitapp.interact;

import com.jakewharton.rxrelay2.BehaviorRelay;
import com.wallet.crypto.mybitapp.repository.PassCodeSource;

import java.util.Map;
import io.reactivex.Observable;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class PassCodeInteract {
    private final BehaviorRelay<Boolean> checkIsOptionedChanged = BehaviorRelay.create();
    private final BehaviorRelay<Object> pinCodeEnteredSuccessful = BehaviorRelay.create();

    private final PassCodeSource passCodeRepository;

    public PassCodeInteract(PassCodeSource passCodeRepository) {
        this.passCodeRepository = passCodeRepository;
    }

    public Single<String> fetchPassHash() {
        return passCodeRepository.fetch()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(resultMap -> {
                    if (resultMap.size() > 0) {
                        return getPassCodeHash(resultMap).getKey();
                    } else {
                        return "";
                    }
                });
    }

    public Completable put(String passCode, Boolean isOptioned) {
        return passCodeRepository.put(passCode, isOptioned)
                .andThen(Completable.fromAction(() -> acceptCheckIsOptionedChanged(true)))
                .subscribeOn(Schedulers.io());
    }

    public Single<Boolean> checkIsOptioned() {
        return passCodeRepository.fetch()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(resultMap -> {
                    if (resultMap.size() > 0) {
                        return getPassCodeHash(resultMap).getValue();
                    } else {
                        return false;
                    }
                });
    }

    public Completable remove() {
        return passCodeRepository.remove()
                .andThen(Completable.fromAction(() -> acceptCheckIsOptionedChanged(false)))
                .subscribeOn(Schedulers.io());
    }

    private Map.Entry<String, Boolean> getPassCodeHash(Map<String, Boolean> mapPass) {
        return mapPass.entrySet().iterator().next();
    }

    public void acceptCheckIsOptionedChanged(boolean checked) {
        checkIsOptionedChanged.accept(checked);
    }

    public void acceptPinCodeEnteredSuccessful() {
        pinCodeEnteredSuccessful.accept("");
    }

    public Observable<Boolean> checkIsOptionedChanged() {
        return checkIsOptionedChanged;
    }

    public Observable<Object> pinCodeEnteredSuccessful() {
        return pinCodeEnteredSuccessful;
    }
}
