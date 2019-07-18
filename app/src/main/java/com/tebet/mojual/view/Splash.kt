package com.tebet.mojual.view

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.view.View
import com.tebet.mojual.R
import com.tebet.mojual.common.base.BaseActivity
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
            startActivity(Intent(this, Login::class.java))
            finish()
//            if (applicationContext.checkConnectivity()) {
//                versionCheck()
//                ApiService.createServiceNew(ProfileService::class.java).getTimeZone().enqueue(object : ApiCallbackV2<List<GMTResponse>>(mView) {
//                    override fun onSuccess(response: List<GMTResponse>?) {
//                        AppController.gmtList = response
//                    }
//
//                    override fun onFail(call: Call<*>?, e: ApiException) {}
//                })
//            } else {
//                val builder = AlertDialog.Builder(this@Splash)
//                builder.setMessage(getString(R.string.general_message_error))
//                // add a button
//                builder.setPositiveButton(getString(R.string.general_button_ok)) { dialog, which -> finish() }
//                // create and show the alert dialog
//                val dialog = builder.create()
//                dialog.show()
//            }
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

//    private fun versionCheck() {
//        ApiService.createServiceNew(UpdateService::class.java).getUpdateV2(BuildConfig.VERSION_NAME, "ANDROID", "STUDENT").enqueue(object : ApiCallbackV2<Boolean>(mView) {
//            override fun onSuccess(response: Boolean?) {
//                super.onSuccess(response)
//                if (response != null && !response) {
//                    showForceUpdate()
//                    return
//                }
//                startLoginFlow()
//            }
//
//            override fun onFail(call: Call<*>?, e: ApiException) {
//                super.onFail(call, e)
//                showError(e)
//            }
//        })
//    }
//
//    private fun showForceUpdate() {
//        val forceUpdateIntent = Intent(this@Splash, ForceUpdate::class.java)
//        startActivity(forceUpdateIntent)
//        this@Splash.finish()
//    }
//
//    private fun startLoginFlow() {
//        val skipIntro = PreferenceUtils.getBoolean(ActivityIntroSlider.SKIP_INTRO, false)
//        if (!skipIntro) {
//            val mainIntent = Intent(this@Splash, ActivityIntroSliderV2::class.java)
//            mainIntent.putExtra(ActivityLogin.EXTRA_REFERRAL, referralCode)
//            startActivity(mainIntent)
//            this@Splash.finish()
//        } else {
//            if (AuthSdk.instance.currentToken == null || TextUtils.isEmpty(AuthSdk.instance.currentToken!!.appToken)) {
//                val mainIntent = Intent(this@Splash, ActivityLogin::class.java)
//                mainIntent.putExtra(ActivityLogin.EXTRA_REFERRAL, referralCode)
//                startActivity(mainIntent)
//                this@Splash.finish()
//            } else {
//                TokenAuth(this@Splash).mobileAuth(mView, AuthSdk.instance.currentToken!!.appToken)
//            }
//        }
//    }

    companion object {
        private val duration = 3000
    }
}
