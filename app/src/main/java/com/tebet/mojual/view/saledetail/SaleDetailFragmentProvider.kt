package com.tebet.mojual.view.saledetail

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SaleDetailFragmentProvider {

    @ContributesAndroidInjector(modules = [SaleDetailFragmentModule::class])
    internal abstract fun provideSaleDetailFragmentFactory(): SaleDetailFragment
}
