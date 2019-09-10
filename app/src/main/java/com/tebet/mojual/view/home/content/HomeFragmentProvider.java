package com.tebet.mojual.view.home.content;

import com.tebet.mojual.view.history.HistoryFragment;
import com.tebet.mojual.view.history.HistoryFragmentModule;
import com.tebet.mojual.view.historysearch.HistorySearchFragmentModule;
import com.tebet.mojual.view.historysearch.HistorySearchFragment;
import com.tebet.mojual.view.message.MessageFragment;
import com.tebet.mojual.view.message.MessageFragmentModule;
import com.tebet.mojual.view.qualitycheck.QualityFragment;
import com.tebet.mojual.view.qualitycheck.QualityFragmentModule;
import com.tebet.mojual.view.qualitydetail.OrderDetailFragment;
import com.tebet.mojual.view.qualitydetail.OrderDetailFragmentModule;
import com.tebet.mojual.view.qualityreject.OrderRejectFragment;
import com.tebet.mojual.view.qualityreject.OrderRejectFragmentModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class HomeFragmentProvider {

    @ContributesAndroidInjector(modules = HomeFragmentModule.class)
    abstract HomeFragment provideHomeFragmentFactory();

    @ContributesAndroidInjector(modules = QualityFragmentModule.class)
    abstract QualityFragment provideQualityFragmentFactory();

    @ContributesAndroidInjector(modules = HistoryFragmentModule.class)
    abstract HistoryFragment provideHistoryFragmentFactory();

    @ContributesAndroidInjector(modules = OrderDetailFragmentModule.class)
    abstract OrderDetailFragment provideOrderDetailFragmentFactory();

    @ContributesAndroidInjector(modules = OrderRejectFragmentModule.class)
    abstract OrderRejectFragment provideOrderRejectFragmentFactory();

    @ContributesAndroidInjector(modules = MessageFragmentModule.class)
    abstract MessageFragment provideMessageFragmentFactory();

    @ContributesAndroidInjector(modules = HistorySearchFragmentModule.class)
    abstract HistorySearchFragment provideHistorySearchFragmentFactory();
}
