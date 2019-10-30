package com.tebet.mojual.view.qualitycheck

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class QualityFragmentProvider {

    @ContributesAndroidInjector(modules = [QualityFragmentModule::class])
    internal abstract fun provideQualityFragmentFactory(): QualityFragment
}
