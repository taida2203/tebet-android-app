package com.tebet.mojual.view.signup.step1

import com.google.gson.Gson
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.EmptyResponse
import com.tebet.mojual.data.models.UpdateProfileRequest
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.concurrent.TimeUnit

class SignUpInfoStep1Model(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<SignUpInfoStep1Navigator>(dataManager, schedulerProvider) {

    fun onCaptureAvatar() {
        navigator.captureAvatar()
    }

    fun onCaptureEKTP() {
        navigator.captureEKTP()
    }

    fun uploadImage(imagePath: String?) {
        val uploadText = MultipartBody.Part.createFormData("folder", "avatar")
        val file = File(imagePath)
        val uploadData = MultipartBody.Part.createFormData(
            "file",
            file.name,
            RequestBody.create(MediaType.parse("image/png"), file)
        )
        compositeDisposable.add(
            dataManager.uploadImage(uploadText, uploadData).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribeWith(object : CallbackWrapper<EmptyResponse>() {
                    override fun onSuccess(dataResponse: EmptyResponse) {
                    }

                    override fun onFailure(error: String?) {
                    }
                })
        )
    }
}