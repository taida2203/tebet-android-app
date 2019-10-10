package com.tebet.mojual.common.services

import android.os.Handler
import android.os.Looper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tebet.mojual.App
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.Message
import com.tebet.mojual.data.models.request.DeviceRegisterRequest
import dagger.android.AndroidInjection
import timber.log.Timber
import javax.inject.Inject


/**
 * NOTE: There can only be one service in each app that receives FCM messages. If multiple
 * are declared in the Manifest then the first one will be chosen.
 *
 *
 * In order to make this Java sample functional, you must remove the following from the Kotlin messaging
 * service in the AndroidManifest.xml:
 *
 *
 * <intent-filter>
 * <action android:name="com.google.firebase.MESSAGING_EVENT"></action>
</intent-filter> *
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var dataManager: DataManager

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Timber.e("DOLPHIN Notification Received")
        remoteMessage.notification?.let {
            val convertedMessage = Message(message = it.body)
            remoteMessage.data.let { extraData ->
                convertedMessage.data = extraData
            }
            when {
                convertedMessage.data?.get("templateCode") == "WELCOME" -> Handler(Looper.getMainLooper()).postDelayed({
                    (application as App).notificationHandlerData.postValue(convertedMessage)
                }, 1000)
                convertedMessage.data?.get("templateCode") == "CUSTOMER_VERIFIED"
                        || convertedMessage.data?.get("templateCode") == "CUSTOMER_BLOCKED"
                        || convertedMessage.data?.get("templateCode") == "CUSTOMER_REJECTED"
                        || convertedMessage.data?.get("templateCode") == "CUSTOMER_UNBLOCKED"
                -> dataManager.getProfile().subscribe({
                    (application as App).notificationHandlerData.postValue(convertedMessage)
                }, { error ->
                    Timber.e(error)
                    (application as App).notificationHandlerData.postValue(convertedMessage)
                })
                else -> (application as App).notificationHandlerData.postValue(convertedMessage)
            }
        }
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        dataManager.registerDevice(DeviceRegisterRequest(token)).subscribe({}, {error-> Timber.e(error)})
    }
}