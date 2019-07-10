package co.common.network

import co.common.view.BaseView
import retrofit2.Call
import java.lang.ref.WeakReference

abstract class ApiCallbackV2<T> protected constructor(protected var context: WeakReference<out BaseView>?) : co.common.network.ApiCallback<T>() {

    override fun onFail(call: Call<*>?, e: co.common.network.ApiException) {
        if (context == null || context?.get() == null) return
    }

    override fun onSuccess(response: T?) {
        if (context == null || context?.get() == null) return
    }
}
