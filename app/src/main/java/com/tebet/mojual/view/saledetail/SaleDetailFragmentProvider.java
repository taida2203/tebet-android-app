package com.tebet.mojual.view.saledetail;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SaleDetailFragmentProvider {

    @ContributesAndroidInjector(modules = SaleDetailFragmentModule.class)
    abstract SaleDetailFragment provideSaleDetailFragmentFactory();
}
