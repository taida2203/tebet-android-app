package com.tebet.mojual.view.signup.step2

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.Address
import com.tebet.mojual.data.models.City
import com.tebet.mojual.data.models.NetworkError
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.signup.step.SignUpInfoStepViewModel

class SignUpInfoStep2Model(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    SignUpInfoStepViewModel<SignUpInfoStep2Navigator>(dataManager, schedulerProvider) {
    var address: ObservableField<Address>? = null

    var cityLiveData = MutableLiveData<List<City>>()

    override fun loadData(isForceLoad: Boolean?) {
        navigator.showLoading(true)
        compositeDisposable.add(
            dataManager.getCityDB()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : CallbackWrapper<List<City>>() {
                    override fun onSuccess(dataResponse: List<City>) {
                        navigator.showLoading(false)
                        cityLiveData.value = dataResponse
                    }

                    override fun onFailure(error: NetworkError) {
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

    fun onDomicileClick() {
        userProfile.get()?.domicileAddress?.let {
            it.expanded = !it.expanded
            userProfile.get()?.pickupAddress?.expanded = !it.expanded
        }
    }

    fun onPickupClick() {
        userProfile.get()?.pickupAddress?.let {
            it.expanded = !it.expanded
            userProfile.get()?.domicileAddress?.expanded = !it.expanded
        }
    }

    fun onCopyAddressClick() {
        userProfile.get()?.pickupAddress?.let {
            it.address = userProfile.get()?.domicileAddress?.address
            it.city = userProfile.get()?.domicileAddress?.city
            it.kecamatan = userProfile.get()?.domicileAddress?.kecamatan
            it.kelurahan = userProfile.get()?.domicileAddress?.kelurahan
            it.country = userProfile.get()?.domicileAddress?.country
            it.postalCode = userProfile.get()?.domicileAddress?.postalCode
            it.mapLocation = userProfile.get()?.domicileAddress?.mapLocation
            it.rtrw = userProfile.get()?.domicileAddress?.rtrw
            it.latitude = userProfile.get()?.domicileAddress?.latitude
            it.longitude = userProfile.get()?.domicileAddress?.longitude
        }
        userProfile.get()?.pickupAddress?.notifyChange()
//        userProfile.get()?.domicileAddress?.notifyChange()
    }

    fun hideKeyboard() {
        navigator.hideKeyboard()
    }
}