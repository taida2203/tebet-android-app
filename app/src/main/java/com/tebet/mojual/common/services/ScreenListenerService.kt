package com.tebet.mojual.common.services

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.IBinder
import com.tebet.mojual.view.base.BaseActivity


class ScreenListenerService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        filter.addAction(Intent.ACTION_USER_PRESENT)
        val mReceiver = BaseActivity.ScreenReceiver()
        registerReceiver(mReceiver, filter)
        return super.onStartCommand(intent, flags, startId)
    }

    inner class LocalBinder : Binder() {
        internal val service: ScreenListenerService
            get() = this@ScreenListenerService
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}
