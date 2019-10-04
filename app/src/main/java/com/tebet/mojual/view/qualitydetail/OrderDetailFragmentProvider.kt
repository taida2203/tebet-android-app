package com.tebet.mojual.view.qualitydetail

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class OrderDetailFragmentProvider {

    @ContributesAndroidInjector(modules = [OrderDetailFragmentModule::class])
    internal abstract fun provideQualityFragmentFactory(): OrderDetailFragment
}
