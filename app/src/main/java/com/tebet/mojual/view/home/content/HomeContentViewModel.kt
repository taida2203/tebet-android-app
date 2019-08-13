package com.tebet.mojual.view.home.content

import androidx.databinding.ObservableField
import com.tebet.mojual.R
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers

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
        navigator.show(R.string.general_error_feature_disabled)
    }

    fun onBorrowClick() {
        navigator.show(R.string.general_error_feature_disabled)
    }

    fun onTipsClick() {
        navigator.show(R.string.general_error_feature_disabled)
    }

    fun loadData() {
        navigator.showLoading(true)
        compositeDisposable.add(
            dataManager.getUserProfileDB()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackWrapper<UserProfile>() {
                    override fun onFailure(error: String?) {
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