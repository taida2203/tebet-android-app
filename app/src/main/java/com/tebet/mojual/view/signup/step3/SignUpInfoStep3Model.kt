package com.tebet.mojual.view.signup.step3

import androidx.lifecycle.MutableLiveData
import co.sdk.auth.core.models.AuthJson
import com.tebet.mojual.common.util.BindingUtils
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.Bank
import com.tebet.mojual.data.models.Region
import com.tebet.mojual.view.base.BaseActivityNavigator
import com.tebet.mojual.view.signup.step.SignUpInfoStepViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class SignUpInfoStep3Model(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    SignUpInfoStepViewModel<SignUpInfoStep3Navigator>(dataManager, schedulerProvider) {
    var bankNameLiveData = MutableLiveData<List<Bank>>()
    var bankRegionLiveData = MutableLiveData<List<Region>>()

    var onOkEditText: BindingUtils.OnOkInSoftKeyboardListener = object : BindingUtils.OnOkInSoftKeyboardListener() {
        override fun onOkInSoftKeyboard() {
            navigator.submit()
        }
    }

    fun loadData() {
        navigator.showLoading(true)
        compositeDisposable.add(
            Observable.zip(
                dataManager.getRegionDB(),
                dataManager.getBankDB(),
                BiFunction<AuthJson<List<Region>>, AuthJson<List<Bank>>, Pair<List<Region>?, List<Bank>?>>
                { regions, banks ->
                    Pair(regions.data, banks.data)
                })
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : DisposableObserver<Pair<List<Region>?, List<Bank>?>>() {

                    override fun onNext(t: Pair<List<Region>?, List<Bank>?>) {
                        navigator.showLoading(false)
                        bankRegionLiveData.value = t.first
                        bankNameLiveData.value = t.second
                    }

                    override fun onError(e: Throwable) {
                        navigator.showLoading(false)
                        handleError(e.message)
                    }
                    override fun onComplete() {
                    }
                })
        )
    }
}