package com.tebet.mojual.view.sale;

import com.tebet.mojual.view.salenow.SaleNowFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SaleFragmentProvider {

    @ContributesAndroidInjector(modules = SaleFragmentModule.class)
    abstract SaleFragment provideSaleFragmentFactory();

    @ContributesAndroidInjector(modules = SaleFragmentModule.class)
    abstract SaleNowFragment provideSaleNowFragmentFactory();
}
