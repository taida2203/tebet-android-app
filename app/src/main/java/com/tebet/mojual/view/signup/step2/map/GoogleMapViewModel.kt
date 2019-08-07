package com.tebet.mojual.view.signup.step2.map

import com.google.android.gms.maps.model.LatLng
import com.tebet.mojual.common.constant.ConfigEnv
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.EmptyResponse
import com.tebet.mojual.data.models.google_map.GeoCodeResponse
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseActivityNavigator
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class GoogleMapViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<BaseActivityNavigator>(dataManager, schedulerProvider) {
    var selectedLocation: LatLng? = null
    var selectedAddress: String? = null
    fun getAddress(selectedLocation: LatLng?) {
        navigator.showLoading(true)
        compositeDisposable.add(
            dataManager.getReserveGeoLocation(
                "${selectedLocation?.latitude},${selectedLocation?.longitude}")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribeWith(object : DisposableObserver<GeoCodeResponse>() {
                    override fun onComplete() {
                    }

                    override fun onNext(geoCode: GeoCodeResponse) {
                        if (geoCode.status.toLowerCase() == "ok") {
                            selectedAddress = geoCode.results.firstOrNull()?.formatted_address
                        }
                        navigator.showLoading(false)
                        navigator.finish()
                    }

                    override fun onError(e: Throwable) {
                        navigator.showLoading(false)
                        navigator.finish()
                    }
                })
        )
    }
}