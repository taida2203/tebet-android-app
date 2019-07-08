package com.tebet.mojual.common.rtc.network

import com.tebet.mojual.common.rtc.view.BaseView
import retrofit2.Call
import java.lang.ref.WeakReference

abstract class ApiCallbackV2<T> protected constructor(protected var context: WeakReference<out BaseView>?) : ApiCallback<T>() {

    override fun onFail(call: Call<*>?, e: ApiException) {
        if (context == null || context?.get() == null) return
    }

    override fun onSuccess(response: T?) {
        if (context == null || context?.get() == null) return
    }
}
