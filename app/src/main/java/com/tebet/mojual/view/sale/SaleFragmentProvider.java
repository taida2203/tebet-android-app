package com.tebet.mojual.view.sale;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SaleFragmentProvider {

    @ContributesAndroidInjector(modules = SaleFragmentModule.class)
    abstract SaleFragment provideSaleFragmentFactory();
}
