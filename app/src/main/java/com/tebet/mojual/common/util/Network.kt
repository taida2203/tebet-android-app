package com.tebet.mojual.common.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import com.tebet.mojual.R
import pub.devrel.easypermissions.EasyPermissions

fun Context.checkConnectivity(): Boolean {
    val perms = Manifest.permission.INTERNET
    if (!EasyPermissions.hasPermissions(this, perms)) {
        EasyPermissions.requestPermissions(this as Activity,
                this.resources.getString(R.string.package_restrict_permission), 99, perms)
    }
    val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}