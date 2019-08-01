package com.tebet.mojual

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.view.base.BaseActivityViewModel
import com.tebet.mojual.view.forgotpassword.ForgotPasswordViewModel
import com.tebet.mojual.view.home.HomeViewModel
import com.tebet.mojual.view.home.content.HomeContentViewModel
import com.tebet.mojual.view.profile.ProfileViewModel
import com.tebet.mojual.view.login.LoginViewModel
import com.tebet.mojual.view.loginpassword.LoginWithPasswordViewModel
import com.tebet.mojual.view.sale.SaleViewModel
import com.tebet.mojual.view.saledetail.SaleDetailViewModel
import com.tebet.mojual.view.signup.SignUpInfoViewModel
import com.tebet.mojual.view.signup.step1.SignUpInfoStep1Model
import com.tebet.mojual.view.signup.step2.SignUpInfoStep2Model
import com.tebet.mojual.view.signup.step3.SignUpInfoStep3Model
import com.tebet.mojual.view.splash.SplashViewModel

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class ViewModelProviderFactory
@Inject constructor(
    private val dataManager: DataManager,
    private val schedulerProvider: SchedulerProvider
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel(dataManager, schedulerProvider) as T
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(dataManager, schedulerProvider) as T
            modelClass.isAssignableFrom(LoginWithPasswordViewModel::class.java) -> LoginWithPasswordViewModel(dataManager, schedulerProvider) as T
            modelClass.isAssignableFrom(BaseActivityViewModel::class.java) -> BaseActivityViewModel(dataManager, schedulerProvider) as T
            modelClass.isAssignableFrom(ForgotPasswordViewModel::class.java) -> ForgotPasswordViewModel(dataManager, schedulerProvider) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(dataManager, schedulerProvider) as T
            modelClass.isAssignableFrom(SignUpInfoViewModel::class.java) -> SignUpInfoViewModel(dataManager, schedulerProvider) as T
            modelClass.isAssignableFrom(HomeContentViewModel::class.java) -> HomeContentViewModel(dataManager, schedulerProvider) as T
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> ProfileViewModel(dataManager, schedulerProvider) as T
            modelClass.isAssignableFrom(SignUpInfoStep1Model::class.java) -> SignUpInfoStep1Model(dataManager, schedulerProvider) as T
            modelClass.isAssignableFrom(SignUpInfoStep2Model::class.java) -> SignUpInfoStep2Model(dataManager, schedulerProvider) as T
            modelClass.isAssignableFrom(SignUpInfoStep3Model::class.java) -> SignUpInfoStep3Model(dataManager, schedulerProvider) as T
            modelClass.isAssignableFrom(SaleViewModel::class.java) -> SaleViewModel(dataManager, schedulerProvider) as T
            modelClass.isAssignableFrom(SaleDetailViewModel::class.java) -> SaleDetailViewModel(dataManager, schedulerProvider) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}