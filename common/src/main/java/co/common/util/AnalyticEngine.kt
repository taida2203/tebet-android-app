package co.common.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics


@SuppressLint("StaticFieldLeak")
object AnalyticEngine {


    private var context: Context? = null
    private var firebaseAnalytics: FirebaseAnalytics? = null
    private var isEnabled: Boolean = false

    fun init(ct: Context, isAnalyticEnabled: Boolean) {
        co.common.util.AnalyticEngine.context = ct
        co.common.util.AnalyticEngine.isEnabled = isAnalyticEnabled
        if (isAnalyticEnabled) {
            co.common.util.AnalyticEngine.firebaseAnalytics = FirebaseAnalytics.getInstance(ct)
        }
    }

    fun page(name: String) {
        if (!co.common.util.AnalyticEngine.isEnabled) return
        val params = Bundle()
        params.putString("screen_name", name)
        co.common.util.AnalyticEngine.firebaseAnalytics?.logEvent("page", params)
    }

    fun action(name: String) {
        if (!co.common.util.AnalyticEngine.isEnabled) {
            return
        }
        co.common.util.AnalyticEngine.firebaseAnalytics?.logEvent(name, null)
    }

    fun action(name: String, timeSpent: Int) {
        if (!co.common.util.AnalyticEngine.isEnabled) return
        val params = Bundle()
        params.putInt("timespent", timeSpent)
        co.common.util.AnalyticEngine.firebaseAnalytics?.logEvent(name, params)
    }

    fun action(name: String, params: Bundle) {
        if (!co.common.util.AnalyticEngine.isEnabled) return
        co.common.util.AnalyticEngine.firebaseAnalytics?.logEvent(name, params)
    }
}
