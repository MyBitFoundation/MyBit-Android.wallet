package com.wallet.crypto.mybitapp.repository.wallet;

import com.wallet.crypto.mybitapp.entity.Wallet;

/**
 * Created by Dasha on 18.06.2018
 */
abstract class BaseKeyStoreDataSource implements WalletDataSourceType {

    AddDataSourceType addDataSourceTypeStrategy;

    void createDataSourceType(int type) {
        try {
            addDataSourceTypeStrategy = getDataSourceType(type);
        } catch (ClassNotFoundException classNotFoundException) {
            // bad type of wallet
        }
    }

    private AddDataSourceType getDataSourceType(int type) throws ClassNotFoundException {
        switch (type) {
            case Wallet.KEY_STORE:
                return new AddKeyStoreDataSource();
            case Wallet.MNEMONIC:
                return new AddMnemonicDataSource();
            case Wallet.WATCH:
                return new AddWatchDataSource();
            default:
                throw new ClassNotFoundException();
        }
    }
}
