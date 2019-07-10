package com.tebet.mojual.notification

import android.app.ActivityManager
import android.content.Context
import android.text.TextUtils
import co.sdk.auth.AuthSdk
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tebet.mojual.data.models.CustomerDevice
import com.tebet.mojual.data.remote.NotificationNewResponse
import org.greenrobot.eventbus.EventBus
import timber.log.Timber

/**
 * Created by hacked on 7/18/2017.
 */

class MyFireBaseMessagingService : FirebaseMessagingService() {
    companion object {
        private val TAG = "MyFirebaseMsgService"
        fun registerCustomerDeviceCode(deviceCode: String?) {
            if (AuthSdk.instance.currentToken != null) {
                val customerDevice = CustomerDevice()
                customerDevice.fcmToken = deviceCode
//                ApiService.createServiceNew(PushService::class.java).postRegisterCustomerDeviceCode(customerDevice).enqueue(object : ApiCallbackV2<String>(null) {
//                })
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        val data = remoteMessage?.data
        Timber.d("MESSAGE RECEIVED!!")
        if (data != null && !TextUtils.isEmpty(AuthSdk.instance?.currentToken?.appToken)) {
            if (isAppOnForeground(this)) {
                EventBus.getDefault().post(NotificationNewResponse(data))
            }
        }
        // Qiscus will handle incoming message
//        if (QiscusFirebaseMessagingUtil.handleMessageReceived(remoteMessage)) {
//            return
//        }

    }

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        registerCustomerDeviceCode(token)
    }

    private fun isAppOnForeground(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses ?: return false
        val packageName = context.packageName
        for (appProcess in appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName == packageName) {
                return true
            }
        }
        return false
    }
}
