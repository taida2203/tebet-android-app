package com.tebet.mojual.view.qualityreject;

import com.tebet.mojual.view.qualitydetail.OrderDetailFragmentModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class OrderRejectFragmentProvider {

    @ContributesAndroidInjector(modules = OrderDetailFragmentModule.class)
    abstract OrderRejectFragment provideQualityFragmentFactory();
}
