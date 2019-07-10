package com.tebet.mojual.common.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

class NetworkUtilReceiver(var listener: NetworkListener? = null) : BroadcastReceiver(){

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            listener?.onNetWorkStateChange(isNetworkAvailable(it))
        }
    }

    private fun isNetworkAvailable(context: Context)  : Boolean{
        val connectionManager = (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
        val activeNetwork = connectionManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }

    interface NetworkListener{
        fun onNetWorkStateChange(isNetworkAvailable : Boolean)
    }
}