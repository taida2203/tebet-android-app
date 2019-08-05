package com.tebet.mojual.view.signup.step3

import androidx.lifecycle.MutableLiveData
import co.sdk.auth.core.models.AuthJson
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.Bank
import com.tebet.mojual.data.models.City
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseActivityNavigator
import com.tebet.mojual.view.base.BaseNavigator
import com.tebet.mojual.view.signup.step.SignUpInfoStepViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class SignUpInfoStep3Model(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    SignUpInfoStepViewModel<BaseActivityNavigator>(dataManager, schedulerProvider) {
    var bankLiveData = MutableLiveData<List<Bank>>()

    fun getBanks() {
        compositeDisposable.add(
            dataManager.getBankDB()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackWrapper<List<Bank>>() {
                    override fun onSuccess(dataResponse: List<Bank>) {
                        bankLiveData.value = dataResponse
                    }

                    override fun onFailure(error: String?) {
                        handleError(error)
                    }
                })
        )
    }
}