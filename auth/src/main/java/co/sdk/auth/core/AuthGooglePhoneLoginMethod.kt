package co.sdk.auth.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import co.sdk.auth.core.models.ApiCallBack
import co.sdk.auth.core.models.LoginConfiguration
import co.sdk.auth.core.models.LoginException
import co.sdk.auth.view.InputVerifyCodeFragment
import co.sdk.auth.view.LoginOTPActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider

class AuthGooglePhoneLoginMethod : AuthMethod {
    private var callback: ApiCallBack<LoginConfiguration>? = null
    private var configuration: LoginConfiguration? = null

    val isLoggedIn: Boolean
        get() {
            return FirebaseAuth.getInstance().currentUser != null
        }

    companion object {
        const val REQUEST_CODE_LOGIN_FIRE_BASE = 4953
    }

    override fun brandedLogin(
        context: Activity,
        configuration: LoginConfiguration,
        callback: ApiCallBack<LoginConfiguration>
    ) {
        this.callback = callback
        this.configuration = configuration
        if (isLoggedIn) {
            if (FirebaseAuth.getInstance().currentUser != null) {
                configuration.phone = FirebaseAuth.getInstance().currentUser?.phoneNumber
                FirebaseAuth.getInstance().currentUser!!.getIdToken(false)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful && task.result?.token?.isNotEmpty() == true) {
                            configuration.token = task.result?.token
                            configuration.let { callback.onSuccess(200, it) }
                        } else {
                            configuration.let { callback.onFailed(LoginException(400, "")) }
                        }
                    }.addOnFailureListener {
                        configuration.let { callback.onFailed(LoginException(400, "")) }
                    }
            } else {
                configuration.let { callback.onSuccess(500, it) }
            }
        } else {
            val intent = Intent(context, LoginOTPActivity::class.java)
            context.startActivityForResult(intent, REQUEST_CODE_LOGIN_FIRE_BASE)
        }
    }

    override fun logout(context: Context, forceLogout: Boolean, callback: ApiCallBack<*>?) {
        FirebaseAuth.getInstance().signOut()
        callback?.onSuccess(200, null)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_LOGIN_FIRE_BASE) { // confirm that this response matches your request
            when (resultCode) {
                Activity.RESULT_OK -> {
                    configuration?.token = data?.getStringExtra("EXTRA_ID_TOKEN")
                    configuration?.let { callback?.onSuccess(200, it) }
                }
                else -> callback?.onFailed(LoginException(502, ""))
            }
        }
    }
}
