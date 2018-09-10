package com.wallet.crypto.mybitapp.interact;

import com.wallet.crypto.mybitapp.entity.Address;
import com.wallet.crypto.mybitapp.entity.exception.WalletException;
import com.wallet.crypto.mybitapp.repository.WalletRepositoryType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;

/**
 * Created by Dasha on 11.06.2018
 */
public class WalletValidationsInteract {

    public Single<String> isValidMnemonic(String mnemonic) {
        return Single.create(emitter -> {
            String clearedMnemonic = mnemonic.trim()
                    .replaceAll("[\n\t\r]", "")
                    .replaceAll("\\s+", " ")
                    .replaceAll("[\\p{Cf}]", "");

            emitter.onSuccess(clearedMnemonic);
        });
    }

    public Single<String> isValidEthAddress(String address) {
        return Single.create(emitter -> {

            if (!Address.isAddress(address)) {
                emitter.onError(new WalletException("Address is not valid."));
            }

            emitter.onSuccess(address);
        });
    }
}
