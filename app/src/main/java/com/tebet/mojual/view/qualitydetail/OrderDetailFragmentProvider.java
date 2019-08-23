package com.tebet.mojual.view.qualitydetail;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class OrderDetailFragmentProvider {

    @ContributesAndroidInjector(modules = OrderDetailFragmentModule.class)
    abstract OrderDetailFragment provideQualityFragmentFactory();
}
