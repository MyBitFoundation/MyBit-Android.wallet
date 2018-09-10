package com.wallet.crypto.mybitapp.repository.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.crypto.mybitapp.entity.Wallet;
import com.wallet.crypto.mybitapp.entity.exception.WalletAlreadyExistsException;

import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicHierarchy;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDUtils;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.ethereum.geth.KeyStore;
import org.web3j.crypto.Credentials;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import rx.functions.Action1;
import rx.functions.Func1;

public class AddMnemonicDataSource implements AddDataSourceType {

    /**
     * CPU/Memory cost parameter. Must be larger than 1, a power of 2 and less than 2^(128 * r / 8).
     */
    private static final int N = 1 << 9;

    /**
     * Parallelization parameter. Must be a positive integer less than or equal to Integer.MAX_VALUE / (128 * r * 8).
     */
    private static final int P = 1;

    private static final String BIP44_PATH = "M/44H/60H/0H/0/0";
    private static final int PRIVATE_KEY_RADIX = 16;

    @Override
    public Single<Wallet> add(String value, String password, String newPassword, Func1<String, Boolean> hasAddress, Action1<Wallet> putWalletInfo, KeyStore keyStore) {
        return Single
                .fromCallable(() -> {
                    DeterministicSeed seed = new DeterministicSeed(value, null, password, DeterministicHierarchy.BIP32_STANDARDISATION_TIME_SECS);
                    seed.check();

                    DeterministicKeyChain chain = DeterministicKeyChain.builder().seed(seed).build();
                    List<ChildNumber> keyPath = HDUtils.parsePath(BIP44_PATH);
                    DeterministicKey key = chain.getKeyByPath(keyPath, true);
                    BigInteger privKey = key.getPrivKey();

                    Credentials credentials = Credentials.create(privKey.toString(PRIVATE_KEY_RADIX));
                    if (hasAddress.call(credentials.getAddress())) {
                        throw new WalletAlreadyExistsException();
                    }

                    return credentials;
                })
                .map(credentials -> org.web3j.crypto.Wallet.create(newPassword, credentials.getEcKeyPair(), N, P))
                .map(walletFile -> new ObjectMapper().writeValueAsString(walletFile))
                .map(store -> {
                    org.ethereum.geth.Account account = keyStore
                            .importKey(store.getBytes(Charset.forName("UTF-8")), newPassword, newPassword);
                    return new Wallet(account.getAddress().getHex().toLowerCase(), getType());
                })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public int getType() {
        return Wallet.MNEMONIC;
    }
}
