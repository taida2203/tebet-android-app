package com.tebet.mojual.view.signup

import androidx.lifecycle.MutableLiveData
import co.sdk.auth.core.models.AuthJson
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.*
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function4
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.concurrent.TimeUnit

class SignUpInfoViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<SignUpNavigator>(dataManager, schedulerProvider) {
    var userProfile: UserProfile = UserProfile()
    var userProfileLiveData: MutableLiveData<UserProfile> = MutableLiveData()

    fun uploadAvatar(currentPhotoPath: String) {
        navigator.showLoading(true)
        compositeDisposable.add(uploadImage(currentPhotoPath, "avatar").subscribeWith(object :
            CallbackWrapper<String>() {
            override fun onSuccess(dataResponse: String) {
                navigator.showLoading(false)
                userProfile.avatar = dataResponse
            }

            override fun onFailure(error: String?) {
                navigator.showLoading(false)
                handleError(error)
            }
        }))
    }

    fun uploadEKTP(currentPhotoPath: String) {
        navigator.showLoading(true)
        compositeDisposable.add(uploadImage(currentPhotoPath, "ktp").subscribeWith(object : CallbackWrapper<String>() {
            override fun onSuccess(dataResponse: String) {
                navigator.showLoading(false)
                userProfile.ktp = dataResponse

            }

            override fun onFailure(error: String?) {
                navigator.showLoading(false)
                handleError(error)
            }
        }))
    }

    private fun uploadImage(imagePath: String?, folderName: String): Observable<AuthJson<String>> {
        val uploadText = MultipartBody.Part.createFormData("folder", folderName)
        val file = File(imagePath)
        val uploadData = MultipartBody.Part.createFormData(
            "file",
            file.name,
            RequestBody.create(MediaType.parse("image/png"), file)
        )
        return dataManager.uploadImage(uploadText, uploadData).subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .debounce(400, TimeUnit.MILLISECONDS)
    }

    fun updateUserProfile() {
        navigator.showLoading(true)
        compositeDisposable.add(
            dataManager.updateProfile(userProfile)
                .concatMap { dataManager.getProfile() }
                .observeOn(schedulerProvider.ui())
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribeWith(object : CallbackWrapper<UserProfile>() {
                    override fun onSuccess(dataResponse: UserProfile) {
                        navigator.showLoading(false)
                        navigator.openHomeScreen()
                    }

                    override fun onFailure(error: String?) {
                        navigator.showLoading(false)
                        handleError(error)
                    }
                })
        )
    }

    override fun loadData(isForceLoad: Boolean?) {
        navigator.showLoading(true)
        compositeDisposable.add(
            Observable.zip(
                dataManager.getCities(), dataManager.getRegions(), dataManager.getBanks(), dataManager.getUserProfileDB(),
                Function4<AuthJson<List<City>>, AuthJson<List<Region>>, AuthJson<List<Bank>>, AuthJson<UserProfile>, AuthJson<UserProfile>>
                { cities, regions, banks, profile -> profile })
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : CallbackWrapper<UserProfile>() {
                    override fun onFailure(error: String?) {
                        navigator.showLoading(false)
                        handleError(error)
                    }

                    override fun onSuccess(dataResponse: UserProfile) {
                        navigator.showLoading(false)
                        userProfile = dataResponse
                        userProfile.domicileAddress?.expanded = true
                        userProfile.pickupAddress?.expanded = false
                        userProfileLiveData.value = userProfile
                    }
                })
        )
    }
}