package com.tebet.mojual.data.remote

import co.sdk.auth.core.models.AuthJson
import io.reactivex.observers.DisposableObserver
import okhttp3.ResponseBody
import org.json.JSONObject

abstract class CallbackWrapper<T> : DisposableObserver<AuthJson<T>>() {
    protected abstract fun onSuccess(dataResponse: T)
    protected abstract fun onFailure(error: String?)

    final override fun onNext(t: AuthJson<T>) {
        t.data?.let { onSuccess(it) }
    }
    final override fun onError(e: Throwable) {
        onFailure(e.message)
        //        if (e instanceof HttpException) {
        //            ResponseBody responseBody = ((HttpException)e).response().errorBody();
        //            view.onUnknownError(getErrorMessage(responseBody));
        //        } else if (e instanceof SocketTimeoutException) {
        //            view.onTimeout();
        //        } else if (e instanceof IOException) {
        //            view.onNetworkError();
        //        } else {
        //            view.onUnknownError(e.getMessage());
        //        }
    }

    override fun onComplete() {

    }

    private fun getErrorMessage(responseBody: ResponseBody): String {
        try {
            val jsonObject = JSONObject(responseBody.string())
            return jsonObject.getString("message")
        } catch (e: Exception) {
            return e.message!!
        }

    }
}
