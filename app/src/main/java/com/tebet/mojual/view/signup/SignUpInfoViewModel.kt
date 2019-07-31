package com.tebet.mojual.view.signup

import co.sdk.auth.core.models.AuthJson
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.EmptyResponse
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
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

    fun uploadAvatar(currentPhotoPath: String) {
        compositeDisposable.add(uploadImage(currentPhotoPath, "avatar").subscribeWith(object : CallbackWrapper<String>() {
            override fun onSuccess(dataResponse: String) {
                userProfile.avatar = dataResponse
            }

            override fun onFailure(error: String?) {
                handleError(error)
            }
        }))
    }

    fun uploadEKTP(currentPhotoPath: String) {
        compositeDisposable.add(uploadImage(currentPhotoPath, "ktp").subscribeWith(object : CallbackWrapper<String>() {
            override fun onSuccess(dataResponse: String) {
                userProfile.ktp = dataResponse

            }

            override fun onFailure(error: String?) {
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
        return dataManager.uploadImage(uploadText, uploadData).subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(400, TimeUnit.MILLISECONDS)
    }

    fun updateUserProfile() {
        compositeDisposable.add(
            dataManager.updateProfile(userProfile)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribeWith(object : CallbackWrapper<EmptyResponse>() {
                    override fun onSuccess(dataResponse: EmptyResponse) {
                        navigator.openHomeScreen()
                    }

                    override fun onFailure(error: String?) {
                        handleError(error)
                    }
                })
        )
    }
}