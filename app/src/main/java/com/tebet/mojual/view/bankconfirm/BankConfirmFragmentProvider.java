package com.tebet.mojual.view.bankconfirm;

import com.tebet.mojual.view.qualitydetail.OrderDetailFragmentModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BankConfirmFragmentProvider {

    @ContributesAndroidInjector(modules = OrderDetailFragmentModule.class)
    abstract BankConfirmFragment provideQualityFragmentFactory();
}
