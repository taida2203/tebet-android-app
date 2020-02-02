package com.tebet.mojual.view.home.content

import androidx.databinding.ObservableField
import com.tebet.mojual.R
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.NetworkError
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.Observable

class HomeContentViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<HomeContentNavigator>(dataManager, schedulerProvider) {
    var userProfile: ObservableField<UserProfile> = ObservableField()

    fun onSellClick() {
        if (userProfile.get()?.isUserVerified() != true) {
            navigator.show(R.string.general_error_feature_permission)
            return
        }
        navigator.openSellScreen()
    }

    fun onCheckQualityClick() {
        if (userProfile.get()?.isUserVerified() != true) {
            navigator.show(R.string.general_error_feature_permission)
            return
        }
        navigator.openQualityCheckScreen()
    }

    fun onBorrowClick() {
        navigator.showFeatureDisabled()
    }

    fun onTipsClick() {
        navigator.openTipsScreen()
    }

    override fun loadData(isForceLoad: Boolean?) {
        navigator.showLoading(true)
        compositeDisposable.add(
            dataManager.getUserProfileDB().concatMap {
                when (isForceLoad) {
                    true -> dataManager.getProfile()
                    else -> Observable.just(it)
                }
            }
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : CallbackWrapper<UserProfile>() {
                    override fun onFailure(error: NetworkError) {
                        navigator.showLoading(false)
                        handleError(error)
                    }

                    override fun onSuccess(dataResponse: UserProfile) {
                        navigator.showLoading(false)
                        userProfile.set(dataResponse)
                    }
                })
        )
    }
}