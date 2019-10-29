package com.tebet.mojual.view.signup.step2.map

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.google.android.gms.maps.model.LatLng
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.google_map.GeoCodeResponse
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.observers.DisposableObserver
import java.util.concurrent.TimeUnit

class GoogleMapViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<GoogleMapNavigator>(dataManager, schedulerProvider) {
    var selectedLocation: LatLng? = null
    var selectedAddress: ObservableField<String> = ObservableField("")

    var isShowTutorial = ObservableBoolean(true)

    fun getAddress(selectedLocation: LatLng?, callback: (() -> Unit)? = null) {
        if(selectedLocation == null) return
        navigator.showLoading(true)
        compositeDisposable.add(
            dataManager.getReserveGeoLocation(
                "${selectedLocation.latitude},${selectedLocation.longitude}"
            )
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribeWith(object : DisposableObserver<GeoCodeResponse>() {
                    override fun onComplete() {
                    }

                    override fun onNext(geoCode: GeoCodeResponse) {
                        if (geoCode.status.toLowerCase() == "ok") {
                            selectedAddress.set(geoCode.results.firstOrNull()?.formatted_address)
                        }
                        navigator.showLoading(false)
                        callback?.let { it() }
//                        navigator.finish()
                    }

                    override fun onError(e: Throwable) {
                        navigator.showLoading(false)
                        callback?.let { it() }
//                        navigator.finish()
                    }
                })
        )
    }

    fun hideTutorial() {
        isShowTutorial.set(false)
        dataManager.isShowTutorialShowed = true
    }

    fun onAddressSearchClick() {
        navigator.showAddressSearch()
    }

    override fun loadData(isForceLoad: Boolean?) {
        isShowTutorial.set(!dataManager.isShowTutorialShowed)
    }
}