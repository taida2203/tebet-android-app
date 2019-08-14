package com.tebet.mojual.view.home.content;

import com.tebet.mojual.view.history.HistoryFragment;
import com.tebet.mojual.view.history.HistoryFragmentModule;
import com.tebet.mojual.view.qualitycheck.QualityFragment;
import com.tebet.mojual.view.qualitycheck.QualityFragmentModule;
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
}
