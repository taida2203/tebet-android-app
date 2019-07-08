package com.tebet.mojual.common.rtc.util

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
        context = ct
        this.isEnabled = isAnalyticEnabled
        if (isAnalyticEnabled) {
            firebaseAnalytics = FirebaseAnalytics.getInstance(ct)
        }
    }

    fun page(name: String) {
        if (!isEnabled) return
        val params = Bundle()
        params.putString("screen_name", name)
        firebaseAnalytics?.logEvent("page", params)
    }

    fun action(name: String) {
        if (!isEnabled) {
            return
        }
        firebaseAnalytics?.logEvent(name, null)
    }

    fun action(name: String, timeSpent: Int) {
        if (!isEnabled) return
        val params = Bundle()
        params.putInt("timespent", timeSpent)
        firebaseAnalytics?.logEvent(name, params)
    }

    fun action(name: String, params: Bundle) {
        if (!isEnabled) return
        firebaseAnalytics?.logEvent(name, params)
    }
}
