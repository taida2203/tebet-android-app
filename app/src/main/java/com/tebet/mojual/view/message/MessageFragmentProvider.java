package com.tebet.mojual.view.message;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MessageFragmentProvider {

    @ContributesAndroidInjector(modules = MessageFragmentModule.class)
    abstract MessageFragment provideMessageFragmentFactory();
}
