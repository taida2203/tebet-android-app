package com.tebet.mojual.di.component

import com.tebet.mojual.App
import com.tebet.mojual.di.builder.ActivityBuilder
import dagger.Component
import dagger.android.AndroidInjectionModule
import com.tebet.mojual.di.module.AppModule
import com.tebet.mojual.di.module.NetModule
import javax.inject.Singleton


/**
 * Created by Ege Kuzubasioglu on 9.06.2018 at 21:07.
 * Copyright (c) 2018. All rights reserved.
 */
@Singleton
@Component(
    modules = [(AndroidInjectionModule::class), (ActivityBuilder::class), (AppModule::class), (NetModule::class)]
)
interface AppComponent {
  fun inject(app: App)
}
