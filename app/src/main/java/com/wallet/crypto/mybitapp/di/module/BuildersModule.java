package com.wallet.crypto.mybitapp.di.module;

import com.wallet.crypto.mybitapp.di.scope.ActivityScope;
import com.wallet.crypto.mybitapp.di.scope.FragmentScope;
import com.wallet.crypto.mybitapp.ui.activity.confirmation.ConfirmationActivity;
import com.wallet.crypto.mybitapp.ui.activity.gassettings.GasSettingsActivity;
import com.wallet.crypto.mybitapp.ui.activity.importwallet.ImportWalletActivity;
import com.wallet.crypto.mybitapp.ui.activity.main.MainActivity;
import com.wallet.crypto.mybitapp.ui.fragment.pin.PinFragment;
import com.wallet.crypto.mybitapp.ui.activity.receive.ReceiveActivity;
import com.wallet.crypto.mybitapp.ui.activity.send.SendActivity;
import com.wallet.crypto.mybitapp.ui.activity.splash.SplashActivity;
import com.wallet.crypto.mybitapp.ui.activity.transactiondetail.TransactionDetailActivity;
import com.wallet.crypto.mybitapp.ui.activity.wallets.WalletsActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BuildersModule {
	@ActivityScope
	@ContributesAndroidInjector(modules = SplashModule.class)
	abstract SplashActivity bindSplashModule();

	@ActivityScope
	@ContributesAndroidInjector(modules = AccountsManageModule.class)
	abstract WalletsActivity bindManageWalletsModule();

	@ActivityScope
	@ContributesAndroidInjector(modules = ImportModule.class)
	abstract ImportWalletActivity bindImportWalletModule();

	@ActivityScope
	@ContributesAndroidInjector(modules = { MainModule.class, TransactionsModule.class, SettingsModule.class, TokensModule.class })
	abstract MainActivity bindMainModule();

    @ActivityScope
    @ContributesAndroidInjector(modules = TransactionDetailModule.class)
    abstract TransactionDetailActivity bindTransactionDetailModule();

	@ActivityScope
	@ContributesAndroidInjector(modules = SendModule.class)
	abstract SendActivity bindSendModule();

	@ActivityScope
	@ContributesAndroidInjector(modules = ConfirmationModule.class)
	abstract ConfirmationActivity bindConfirmationModule();

	@ActivityScope
	@ContributesAndroidInjector(modules = { ReceiveModule.class })
	abstract ReceiveActivity bindMyAddressModule();

	@ActivityScope
	@ContributesAndroidInjector(modules = GasSettingsModule.class)
	abstract GasSettingsActivity bindGasSettingsModule();

	@FragmentScope
	@ContributesAndroidInjector(modules = { PinFragmentModule.class })
	abstract PinFragment pinFragment();
}
