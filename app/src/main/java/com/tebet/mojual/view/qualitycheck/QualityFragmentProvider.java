package com.tebet.mojual.view.qualitycheck;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class QualityFragmentProvider {

    @ContributesAndroidInjector(modules = QualityFragmentModule.class)
    abstract QualityFragment provideQualityFragmentFactory();
}
