package com.tebet.mojual.di.module

import com.tebet.mojual.view.HomeActivity
import com.tebet.mojual.view.splash.view.Splash
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Ege Kuzubasioglu on 9.06.2018 at 21:04.
 * Copyright (c) 2018. All rights reserved.
 */
@Module
abstract class BuildersModule {
  @ContributesAndroidInjector
  abstract fun homeActivity(): HomeActivity
  @ContributesAndroidInjector
  abstract fun splashActivity(): Splash
}