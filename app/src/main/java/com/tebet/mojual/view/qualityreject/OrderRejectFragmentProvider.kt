package com.tebet.mojual.view.qualityreject

import com.tebet.mojual.view.qualitydetail.OrderDetailFragmentModule

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class OrderRejectFragmentProvider {

    @ContributesAndroidInjector(modules = [OrderDetailFragmentModule::class])
    internal abstract fun provideQualityFragmentFactory(): OrderRejectFragment
}
