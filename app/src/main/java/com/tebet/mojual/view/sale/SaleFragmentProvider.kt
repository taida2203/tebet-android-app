package com.tebet.mojual.view.sale

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SaleFragmentProvider {

    @ContributesAndroidInjector(modules = [SaleFragmentModule::class])
    internal abstract fun provideSaleFragmentFactory(): SaleFragment
}
