package com.tebet.mojual.di.builder

import com.tebet.mojual.view.forgotpassword.ForgotPassword
import com.tebet.mojual.view.home.Home
import com.tebet.mojual.view.home.content.HomeFragmentProvider
import com.tebet.mojual.view.login.Login
import com.tebet.mojual.view.loginpassword.LoginWithPassword
import com.tebet.mojual.view.profile.ProfileFragmentProvider
import com.tebet.mojual.view.qualitycontainer.QualityAddContainer
import com.tebet.mojual.view.signup.step0.SignUpPassword
import com.tebet.mojual.view.sale.SaleFragmentProvider
import com.tebet.mojual.view.saledetail.SaleDetailFragmentProvider
import com.tebet.mojual.view.selectfuturedate.SelectFutureDate
import com.tebet.mojual.view.bankupdate.BankUpdate
import com.tebet.mojual.view.help.QualityHelp
import com.tebet.mojual.view.profilechangepass.ChangePassword
import com.tebet.mojual.view.profilepin.PinCode
import com.tebet.mojual.view.selectquantity.SelectQuantity
import com.tebet.mojual.view.signup.SignUpInfo
import com.tebet.mojual.view.signup.SignUpInfoFragmentProvider
import com.tebet.mojual.view.signup.step2.map.GoogleMapActivity
import com.tebet.mojual.view.splash.Splash
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = [HomeFragmentProvider::class, ProfileFragmentProvider::class, SaleFragmentProvider::class, SaleDetailFragmentProvider::class])
    abstract fun homeActivity(): Home

    @ContributesAndroidInjector
    abstract fun loginActivity(): Login

    @ContributesAndroidInjector
    abstract fun splashActivity(): Splash

    @ContributesAndroidInjector
    abstract fun loginWithPasswordActivity(): LoginWithPassword

    @ContributesAndroidInjector
    abstract fun forgotPasswordActivity(): ForgotPassword

    @ContributesAndroidInjector
    abstract fun signUpPasswordActivity(): SignUpPassword

    @ContributesAndroidInjector
    abstract fun googleMapActivityActivity(): GoogleMapActivity

    @ContributesAndroidInjector(modules = [SignUpInfoFragmentProvider::class])
    abstract fun signUpInfoActivity(): SignUpInfo

    @ContributesAndroidInjector
    abstract fun selectFutureDateActivity(): SelectFutureDate

    @ContributesAndroidInjector
    abstract fun selectQuantityActivity(): SelectQuantity

    @ContributesAndroidInjector
    abstract fun qualityAddContainerActivity(): QualityAddContainer

    @ContributesAndroidInjector(modules = [SignUpInfoFragmentProvider::class])
    abstract fun BankUpdateActivity(): BankUpdate

    @ContributesAndroidInjector
    abstract fun changePasswordActivity(): ChangePassword

    @ContributesAndroidInjector
    abstract fun qualityHelpActivity(): QualityHelp

    @ContributesAndroidInjector
    abstract fun pinCodeActivity(): PinCode
}