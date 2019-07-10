package co.common.view

import android.content.Context

interface BaseView {
    fun getContext(): Context?
    fun isActive(): Boolean
}
