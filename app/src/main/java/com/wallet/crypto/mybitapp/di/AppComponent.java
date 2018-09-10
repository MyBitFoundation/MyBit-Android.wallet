package com.wallet.crypto.mybitapp.di;

import com.wallet.crypto.mybitapp.App;
import com.wallet.crypto.mybitapp.di.module.BuildersModule;
import com.wallet.crypto.mybitapp.di.module.RepositoriesModule;
import com.wallet.crypto.mybitapp.di.module.ToolsModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
		AndroidSupportInjectionModule.class,
		ToolsModule.class,
		RepositoriesModule.class,
		BuildersModule.class })
interface AppComponent extends AndroidInjector<App> {
	@Component.Builder
	abstract class Builder extends AndroidInjector.Builder<App> { }
}
