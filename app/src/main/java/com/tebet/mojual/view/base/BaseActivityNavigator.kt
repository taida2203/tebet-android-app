package com.tebet.mojual.view.base

import android.app.Activity

interface BaseActivityNavigator {
    fun onBackPressed()
    fun showLoading(isLoading: Boolean)
    fun show(message: String)
    fun show(messageResId: Int)
    fun activity(): Activity?
    fun finish()
}
