package com.tebet.mojual.view.historysearch

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class HistorySearchFragmentProvider {

    @ContributesAndroidInjector(modules = [HistorySearchFragmentModule::class])
    internal abstract fun provideHistorySearchFragmentFactory(): HistorySearchFragment
}
