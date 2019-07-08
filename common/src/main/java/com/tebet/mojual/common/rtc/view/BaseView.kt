package com.tebet.mojual.common.rtc.view

import android.content.Context

interface BaseView {
    fun getContext(): Context?
    fun isActive(): Boolean
}
