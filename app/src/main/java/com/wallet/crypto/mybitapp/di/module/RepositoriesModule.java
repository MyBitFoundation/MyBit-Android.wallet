package com.wallet.crypto.mybitapp.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.wallet.crypto.mybitapp.entity.Wallet;
import com.wallet.crypto.mybitapp.interact.PassCodeInteract;
import com.wallet.crypto.mybitapp.repository.DefaultTokensRepository;
import com.wallet.crypto.mybitapp.repository.DefaultTokensRepositoryType;
import com.wallet.crypto.mybitapp.repository.PassCodeSource;
import com.wallet.crypto.mybitapp.repository.PreferenceRepositoryType;
import com.wallet.crypto.mybitapp.repository.RealmPassCodeSource;
import com.wallet.crypto.mybitapp.repository.SharedPreferenceRepository;
import com.wallet.crypto.mybitapp.repository.TokenRepository;
import com.wallet.crypto.mybitapp.repository.TokenRepositoryType;
import com.wallet.crypto.mybitapp.repository.TransactionInMemorySource;
import com.wallet.crypto.mybitapp.repository.TransactionLocalSource;
import com.wallet.crypto.mybitapp.repository.TransactionRepository;
import com.wallet.crypto.mybitapp.repository.TransactionRepositoryType;
import com.wallet.crypto.mybitapp.repository.WalletRepository;
import com.wallet.crypto.mybitapp.repository.WalletRepositoryType;
import com.wallet.crypto.mybitapp.repository.local.BalanceTokenSource;
import com.wallet.crypto.mybitapp.repository.local.RealmBalanceTokenSource;
import com.wallet.crypto.mybitapp.repository.local.RealmTokenSource;
import com.wallet.crypto.mybitapp.repository.local.RealmWalletInfoSource;
import com.wallet.crypto.mybitapp.repository.local.TokenLocalSource;
import com.wallet.crypto.mybitapp.repository.local.WalletInfoSource;
import com.wallet.crypto.mybitapp.repository.session.SessionRepository;
import com.wallet.crypto.mybitapp.repository.session.SessionRepositoryType;
import com.wallet.crypto.mybitapp.repository.wallet.KeyStoreDataSource;
import com.wallet.crypto.mybitapp.repository.wallet.WalletDataSourceType;
import com.wallet.crypto.mybitapp.repository.wallet.WatchDataSource;
import com.wallet.crypto.mybitapp.service.BlockExplorerClient;
import com.wallet.crypto.mybitapp.service.BlockExplorerClientType;
import com.wallet.crypto.mybitapp.service.EthplorerTokenService;
import com.wallet.crypto.mybitapp.service.TickerService;
import com.wallet.crypto.mybitapp.service.TokenExplorerClientType;
import com.wallet.crypto.mybitapp.service.TrustWalletTickerService;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class RepositoriesModule {
    @Singleton
    @Provides
    PreferenceRepositoryType providePreferenceRepository(Context context) {
        return new SharedPreferenceRepository(context);
    }

    @Singleton
    @Provides
    WalletDataSourceType provideAccountKeyStoreService(Context context) {
        File file = new File(context.getFilesDir(), "keystore/keystore");
        return new KeyStoreDataSource(file);
    }

    @Singleton
    @Provides
    TickerService provideTickerService(OkHttpClient httpClient, Gson gson) {
        return new TrustWalletTickerService(httpClient, gson);
    }

    @Singleton
    @Provides
    WalletRepositoryType provideWalletRepository(Context context,
                                                 WalletInfoSource walletInfoSource,
                                                 PreferenceRepositoryType preferenceRepositoryType) {

        File file = new File(context.getFilesDir(), "keystore/keystore");
        WalletDataSourceType keyStoreDataSource = new KeyStoreDataSource(file);
        WalletDataSourceType watchStoreDataSource = new WatchDataSource(walletInfoSource);

        return new WalletRepository(
                Wallet.KEY_STORE,
                preferenceRepositoryType,
                keyStoreDataSource, watchStoreDataSource);
    }

    @Singleton
    @Provides
    TransactionRepositoryType provideTransactionRepository(
            SessionRepositoryType sessionRepository,
            WalletDataSourceType accountKeystoreService,
            BlockExplorerClientType blockExplorerClient,
            DefaultTokensRepositoryType defaultTokensRepository) {
        TransactionLocalSource inMemoryCache = new TransactionInMemorySource();
        return new TransactionRepository(
                sessionRepository,
                accountKeystoreService,
                inMemoryCache,
                blockExplorerClient,
                defaultTokensRepository);
    }

    @Singleton
    @Provides
    BlockExplorerClientType provideBlockExplorerClient(
            OkHttpClient httpClient,
            Gson gson,
            SessionRepositoryType sessionRepository) {
        return new BlockExplorerClient(httpClient, gson, sessionRepository);
    }

    @Singleton
    @Provides
    TokenRepositoryType provideTokenRepository(
            TokenLocalSource tokenLocalSource,
            BalanceTokenSource balanceTokenSource,
            DefaultTokensRepositoryType defaultTokensRepository,
            OkHttpClient okHttpClient,
            TickerService tickerService,
            SessionRepositoryType sessionRepository) {
        return new TokenRepository(
                tokenLocalSource,
                balanceTokenSource,
                defaultTokensRepository,
                okHttpClient,
                tickerService,
                sessionRepository);
    }

    @Singleton
    @Provides
    TokenExplorerClientType provideTokenService(OkHttpClient okHttpClient,
                                                Gson gson,
                                                SessionRepositoryType sessionRepository) {
        return new EthplorerTokenService(okHttpClient, gson, sessionRepository);
    }

    @Singleton
    @Provides
    TokenLocalSource provideRealmTokenSource() {
        return new RealmTokenSource();
    }

    @Singleton
    @Provides
    BalanceTokenSource provideBalanceTokenSource() {
        return new RealmBalanceTokenSource();
    }

    @Singleton
    @Provides
    DefaultTokensRepositoryType provideDefaultTokensRepository() {
        return new DefaultTokensRepository();
    }

    @Provides
    @Singleton
    WalletInfoSource provideWalletInfoSource() {
        return new RealmWalletInfoSource();
    }

    @Provides
    @Singleton
    SessionRepositoryType provideSesstionRepository(PreferenceRepositoryType preferenceRepository) {
        return new SessionRepository(preferenceRepository);
    }

    @Singleton
    @Provides
    PassCodeSource providePassCodeRepository() {
        return new RealmPassCodeSource();
    }

    @Singleton
    @Provides
    PassCodeInteract providePassCodeInteract(PassCodeSource passCodeRepository) {
        return new PassCodeInteract(passCodeRepository);
    }
}
