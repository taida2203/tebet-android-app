package com.tebet.mojual

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tebet.mojual.data.repository.ProfileRepository
import com.tebet.mojual.view.login.LoginViewModel
import com.tebet.mojual.view.splash.SplashViewModel

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by jyotidubey on 22/02/19.
 */
@Singleton
open class ViewModelProviderFactory
@Inject constructor(
//    private val dataManager: DataManager,
//    private val schedulerProvider: SchedulerProvider
    private val profileRepository: ProfileRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(profileRepository) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(profileRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}