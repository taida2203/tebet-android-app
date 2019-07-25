package com.tebet.mojual

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tebet.mojual.data.repository.ProfileRepository
import com.tebet.mojual.view.splash.viewmodel.SplashViewModel

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by jyotidubey on 22/02/19.
 */
@Singleton
open class ViewModelProviderFactory
@Inject
public constructor(
//    private val dataManager: DataManager,
//    private val schedulerProvider: SchedulerProvider
    val profileRepository: ProfileRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(profileRepository) as T
        }
//        if (modelClass.isAssignableFrom(AboutViewModel::class.java!!)) {
//
//            return AboutViewModel(dataManager, schedulerProvider) as T
//        } else if (modelClass.isAssignableFrom(FeedViewModel::class.java!!)) {
//
//            return FeedViewModel(dataManager, schedulerProvider) as T
//        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java!!)) {
//
//            return LoginViewModel(dataManager, schedulerProvider) as T
//        } else if (modelClass.isAssignableFrom(MainViewModel::class.java!!)) {
//
//            return MainViewModel(dataManager, schedulerProvider) as T
//        } else if (modelClass.isAssignableFrom(BlogViewModel::class.java!!)) {
//
//            return BlogViewModel(dataManager, schedulerProvider) as T
//        } else if (modelClass.isAssignableFrom(RateUsViewModel::class.java!!)) {
//
//            return RateUsViewModel(dataManager, schedulerProvider) as T
//        } else if (modelClass.isAssignableFrom(OpenSourceViewModel::class.java!!)) {
//
//            return OpenSourceViewModel(dataManager, schedulerProvider) as T
//        } else if (modelClass.isAssignableFrom(SplashViewModel::class.java!!)) {
//
//            return SplashViewModel(dataManager, schedulerProvider) as T
//        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}