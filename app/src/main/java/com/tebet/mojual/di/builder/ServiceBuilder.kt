package com.tebet.mojual.di.builder

import com.tebet.mojual.common.services.DigitalFootPrintServices
import com.tebet.mojual.common.services.MyFirebaseMessagingService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceBuilder {
    @ContributesAndroidInjector
    abstract fun firebaseMessagingService(): MyFirebaseMessagingService

    @ContributesAndroidInjector
    abstract fun digitalFootPrintServices(): DigitalFootPrintServices
}