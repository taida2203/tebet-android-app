package com.tebet.mojual.view.signup.step2

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import co.sdk.auth.core.models.AuthJson
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.*
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.signup.step.SignUpInfoStepViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class SignUpInfoStep2Model(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    SignUpInfoStepViewModel<SignUpInfoStep2Navigator>(dataManager, schedulerProvider) {
    var address: ObservableField<Address>? = null

    var cityLiveData = MutableLiveData<List<City>>()

    fun loadData() {
        navigator.showLoading(true)
        compositeDisposable.add(
            dataManager.getCityDB()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackWrapper<List<City>>() {
                    override fun onSuccess(dataResponse: List<City>) {
                        navigator.showLoading(false)
                        cityLiveData.value = dataResponse
                    }

                    override fun onFailure(error: String?) {
                        navigator.showLoading(false)
                        handleError(error)
                    }
                })
        )
    }


    fun onChooseMapClick(address: ObservableField<Address>) {
        this.address = address
        navigator.selectLocation(address.get())
    }
}