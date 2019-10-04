package com.tebet.mojual.view.home.content

import com.tebet.mojual.view.bankconfirm.BankConfirmFragment
import com.tebet.mojual.view.bankconfirm.BankConfirmFragmentModule
import com.tebet.mojual.view.history.HistoryFragment
import com.tebet.mojual.view.history.HistoryFragmentModule
import com.tebet.mojual.view.historysearch.HistorySearchFragmentModule
import com.tebet.mojual.view.historysearch.HistorySearchFragment
import com.tebet.mojual.view.message.MessageFragment
import com.tebet.mojual.view.message.MessageFragmentModule
import com.tebet.mojual.view.qualitycheck.QualityFragment
import com.tebet.mojual.view.qualitycheck.QualityFragmentModule
import com.tebet.mojual.view.qualitydetail.OrderDetailFragment
import com.tebet.mojual.view.qualitydetail.OrderDetailFragmentModule
import com.tebet.mojual.view.qualityreject.OrderRejectFragment
import com.tebet.mojual.view.qualityreject.OrderRejectFragmentModule

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class HomeFragmentProvider {

    @ContributesAndroidInjector(modules = [HomeFragmentModule::class])
    internal abstract fun provideHomeFragmentFactory(): HomeFragment

    @ContributesAndroidInjector(modules = [QualityFragmentModule::class])
    internal abstract fun provideQualityFragmentFactory(): QualityFragment

    @ContributesAndroidInjector(modules = [HistoryFragmentModule::class])
    internal abstract fun provideHistoryFragmentFactory(): HistoryFragment

    @ContributesAndroidInjector(modules = [OrderDetailFragmentModule::class])
    internal abstract fun provideOrderDetailFragmentFactory(): OrderDetailFragment

    @ContributesAndroidInjector(modules = [OrderRejectFragmentModule::class])
    internal abstract fun provideOrderRejectFragmentFactory(): OrderRejectFragment

    @ContributesAndroidInjector(modules = [MessageFragmentModule::class])
    internal abstract fun provideMessageFragmentFactory(): MessageFragment

    @ContributesAndroidInjector(modules = [HistorySearchFragmentModule::class])
    internal abstract fun provideHistorySearchFragmentFactory(): HistorySearchFragment

    @ContributesAndroidInjector(modules = [BankConfirmFragmentModule::class])
    internal abstract fun provideBankConfirmFragmentFactory(): BankConfirmFragment
}
