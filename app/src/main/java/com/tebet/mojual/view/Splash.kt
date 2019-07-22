package com.tebet.mojual.view

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import co.sdk.auth.AuthSdk
import co.sdk.auth.core.models.AuthJson
import co.sdk.auth.core.models.Token
import co.sdk.auth.network.ServiceHelper
import com.tebet.mojual.R
import com.tebet.mojual.common.base.BaseActivity
import com.tebet.mojual.common.util.checkConnectivity
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.network.ApiService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class Splash : BaseActivity() {
    private val TAG = javaClass.simpleName

    private var referralCode = ""

    override val contentLayoutId: Int
        get() = R.layout.activity_splash_screen

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        try {
            val info = packageManager.getPackageInfo(
                    "com.tebet.mojual",
                    PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }

        supportActionBar!!.hide()
        Handler().postDelayed({
            if (applicationContext.checkConnectivity()) {
                if (AuthSdk.instance.currentToken?.appToken != null) {
                    ServiceHelper.createService(ApiService::class.java).getProfile()
                        .enqueue(object : retrofit2.Callback<AuthJson<UserProfile>> {
                            override fun onResponse(call: Call<AuthJson<UserProfile>>, response: Response<AuthJson<UserProfile>>) {
                                if (response.body()?.data?.status.equals("INIT")) {
                                    finish()
                                    startActivity(Intent(this@Splash, SignUpPassword::class.java))
                                } else {
                                    finish()
                                    startActivity(Intent(this@Splash, HomeActivity::class.java))
                                }
                            }

                            override fun onFailure(call: Call<AuthJson<UserProfile>>, t: Throwable) {
                                finish()
                                startActivity(Intent(this@Splash, Login::class.java))
                            }
                        })
                } else {
                    finish()
                    startActivity(Intent(this@Splash, Login::class.java))
                }

            } else {
                val builder = AlertDialog.Builder(this@Splash)
                builder.setMessage(getString(R.string.general_message_error))
                // add a button
                builder.setPositiveButton(getString(R.string.general_button_ok)) { dialog, which -> finish() }
                // create and show the alert dialog
                val dialog = builder.create()
                dialog.show()
            }
        }, duration.toLong())

//        FirebaseDynamicLinks.getInstance()
//                .getDynamicLink(intent)
//                .addOnSuccessListener(this) { pendingDynamicLinkData ->
//                    // Get deep link from result (may be null if no link is found)
//                    val deepLink: Uri
//                    if (pendingDynamicLinkData != null) {
//                        deepLink = pendingDynamicLinkData.link
//                        try {
//                            referralCode = deepLink.toString().split("referrerId=".toRegex())
//                                    .dropLastWhile { it.isEmpty() }
//                                    .toTypedArray()[1]
//                                    .split("&".toRegex())
//                                    .dropLastWhile { it.isEmpty() }
//                                    .toTypedArray()[0]
//                            PreferenceUtils.saveLong(StudentDTO.PREF_STUDENT_REFERRAL_ID, referralCode.toLong())
//                        } catch (ignored: Exception) {
//                        }
//
//                    }
//
//                    // Handle the deep link. For example, open the linked
//                    // content, or apply promotional credit to the user's
//                    // account.
//                    // ...
//
//                    // ...
//                }
//                .addOnFailureListener(this) { e -> Log.w(TAG, "getDynamicLink:onFailure", e) }
    }

    companion object {
        private val duration = 3000
    }
}
