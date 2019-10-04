package com.tebet.mojual.view.history

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class HistoryFragmentProvider {

    @ContributesAndroidInjector(modules = [HistoryFragmentModule::class])
    internal abstract fun provideHistoryFragmentFactory(): HistoryFragment
}
