package com.tebet.mojual.view.historysearch;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class HistorySearchFragmentProvider {

    @ContributesAndroidInjector(modules = HistorySearchFragmentModule.class)
    abstract HistorySearchFragment provideHistorySearchFragmentFactory();
}
