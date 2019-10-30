package co.sdk.auth.core.models

abstract class ApiCallBack<T> {
    fun onPrepare() {}

    abstract fun onSuccess(code: Int, response: T?)

    abstract fun onFailed(exeption: LoginException)

    fun onCompleted() {}
}