package com.tebet.mojual.view.base

import android.app.Activity

interface BaseActivityNavigator {
    fun onBackPressed()
    fun showLoading(isLoading: Boolean)
    fun activity(): Activity?
    fun finish()
}
