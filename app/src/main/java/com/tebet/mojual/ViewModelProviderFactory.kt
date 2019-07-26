package com.tebet.mojual

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.repository.ProfileRepository
import com.tebet.mojual.view.base.BaseActivityViewModel
import com.tebet.mojual.view.login.LoginViewModel
import com.tebet.mojual.view.loginpassword.LoginWithPasswordViewModel
import com.tebet.mojual.view.splash.SplashViewModel

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class ViewModelProviderFactory
@Inject constructor(
//    private val schedulerProvider: SchedulerProvider
    private val dataManager: DataManager
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel(dataManager) as T
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(dataManager) as T
            modelClass.isAssignableFrom(LoginWithPasswordViewModel::class.java) -> LoginWithPasswordViewModel(dataManager) as T
            modelClass.isAssignableFrom(BaseActivityViewModel::class.java) -> BaseActivityViewModel(dataManager) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}