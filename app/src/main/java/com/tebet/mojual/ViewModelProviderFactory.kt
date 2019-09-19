package com.tebet.mojual

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tebet.mojual.common.util.Sensor
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.view.bankconfirm.BankConfirmViewModel
import com.tebet.mojual.view.bankupdate.BankUpdateViewModel
import com.tebet.mojual.view.base.BaseActivityViewModel
import com.tebet.mojual.view.forgotpassword.ForgotPasswordViewModel
import com.tebet.mojual.view.help.QualityHelpViewModel
import com.tebet.mojual.view.history.HistoryViewModel
import com.tebet.mojual.view.home.HomeViewModel
import com.tebet.mojual.view.home.content.HomeContentViewModel
import com.tebet.mojual.view.login.LoginViewModel
import com.tebet.mojual.view.loginpassword.LoginWithPasswordViewModel
import com.tebet.mojual.view.historysearch.HistorySearchViewModel
import com.tebet.mojual.view.message.MessageViewModel
import com.tebet.mojual.view.profile.ProfileViewModel
import com.tebet.mojual.view.profilepin.PinCodeViewModel
import com.tebet.mojual.view.qualitycheck.QualityViewModel
import com.tebet.mojual.view.qualitycontainer.QualityAddContainerViewModel
import com.tebet.mojual.view.qualitydetail.OrderDetailViewModel
import com.tebet.mojual.view.qualityreject.OrderRejectViewModel
import com.tebet.mojual.view.sale.SaleViewModel
import com.tebet.mojual.view.saledetail.SaleDetailViewModel
import com.tebet.mojual.view.selectfuturedate.SelectFutureDateViewModel
import com.tebet.mojual.view.selectquantity.SelectQuantityViewModel
import com.tebet.mojual.view.signup.SignUpInfoViewModel
import com.tebet.mojual.view.signup.step1.SignUpInfoStep1Model
import com.tebet.mojual.view.signup.step2.SignUpInfoStep2Model
import com.tebet.mojual.view.signup.step2.map.GoogleMapViewModel
import com.tebet.mojual.view.signup.step3.SignUpInfoStep3Model
import com.tebet.mojual.view.splash.SplashViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class ViewModelProviderFactory
@Inject constructor(
    private val dataManager: DataManager,
    private val schedulerProvider: SchedulerProvider,
    private val sensorManager: Sensor
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
            modelClass.isAssignableFrom(GoogleMapViewModel::class.java) -> GoogleMapViewModel(dataManager, schedulerProvider) as T
            modelClass.isAssignableFrom(SelectQuantityViewModel::class.java) -> SelectQuantityViewModel(dataManager, schedulerProvider) as T
            modelClass.isAssignableFrom(SelectFutureDateViewModel::class.java) -> SelectFutureDateViewModel(dataManager, schedulerProvider) as T
            modelClass.isAssignableFrom(QualityViewModel::class.java) -> QualityViewModel(dataManager, schedulerProvider) as T
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> HistoryViewModel(dataManager, schedulerProvider) as T
            modelClass.isAssignableFrom(QualityAddContainerViewModel::class.java) -> QualityAddContainerViewModel(dataManager, schedulerProvider, sensorManager) as T
            modelClass.isAssignableFrom(OrderDetailViewModel::class.java) -> OrderDetailViewModel(dataManager, schedulerProvider) as T
            modelClass.isAssignableFrom(OrderRejectViewModel::class.java) -> OrderRejectViewModel(dataManager, schedulerProvider) as T
            modelClass.isAssignableFrom(MessageViewModel::class.java) -> MessageViewModel(dataManager, schedulerProvider) as T
            modelClass.isAssignableFrom(HistorySearchViewModel::class.java) -> HistorySearchViewModel(dataManager, schedulerProvider) as T
            modelClass.isAssignableFrom(BankConfirmViewModel::class.java) -> BankConfirmViewModel(dataManager, schedulerProvider) as T
            modelClass.isAssignableFrom(SelectQuantityViewModel::class.java) -> SelectQuantityViewModel(dataManager, schedulerProvider) as T
            modelClass.isAssignableFrom(BankUpdateViewModel::class.java) -> BankUpdateViewModel(dataManager, schedulerProvider) as T
            modelClass.isAssignableFrom(QualityHelpViewModel::class.java) -> QualityHelpViewModel(dataManager, schedulerProvider) as T
            modelClass.isAssignableFrom(PinCodeViewModel::class.java) -> PinCodeViewModel(dataManager, schedulerProvider) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}