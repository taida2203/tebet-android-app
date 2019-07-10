package co.common.network

import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.json.JSONObject
import retrofit2.HttpException

abstract class SingleObserverApiCallback<T>(private val compositeDisposable: CompositeDisposable) : SingleObserver<T> {

    abstract fun onFailure(exception: co.common.network.ApiException)

    override fun onSubscribe(d: Disposable) {
        compositeDisposable.add(d)
    }

    override fun onError(e: Throwable) {
        when (e) {
            is HttpException -> {
                val errorBody = e.response().errorBody()?.string()
                if (errorBody != null) handleError(e.response().code(), errorBody)
            }
            is co.common.network.ApiException -> onFailure(e)
            else -> onFailure(co.common.network.ApiException())
        }
    }

    private fun handleError(errorCode: Int, responseBody: String) {
        val exception = co.common.network.ApiException().apply { this.errorCode = errorCode }

        if (responseBody.isBlank() || responseBody == "\"\"" || errorCode >= 500) {
            onFailure(exception)
            return
        }

        val jsonObject = JSONObject(responseBody)
        var message: String

        try {
            message = jsonObject.getString("message")
            exception.errorMessage = message

            onFailure(exception)
            return
        } catch (ignored: Exception) {
        }

        try {
            val messages = jsonObject.getJSONArray("messages")
            message = messages.getString(0)
            exception.errorMessage = message

            onFailure(exception)
            return
        } catch (ignored: Exception) {
        }

        onFailure(exception)

    }
}