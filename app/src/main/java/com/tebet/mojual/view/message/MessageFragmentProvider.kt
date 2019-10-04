package com.tebet.mojual.view.message

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MessageFragmentProvider {

    @ContributesAndroidInjector(modules = [MessageFragmentModule::class])
    internal abstract fun provideMessageFragmentFactory(): MessageFragment
}
