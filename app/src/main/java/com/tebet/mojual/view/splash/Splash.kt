package com.tebet.mojual.view.splash

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.databinding.ActivitySplashScreenBinding
import com.tebet.mojual.view.home.HomeActivity
import com.tebet.mojual.view.login.Login
import com.tebet.mojual.view.registration.SignUpPassword
import com.tebet.mojual.view.base.BaseActivity
import com.tebet.mojual.view.signup.SignUpInfo
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

open class Splash : BaseActivity<ActivitySplashScreenBinding, SplashViewModel>(),
    SplashNavigator {
    companion object {
        private val duration = 3000
    }

    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: SplashViewModel
        get() = ViewModelProviders.of(this, factory).get(SplashViewModel::class.java)

    override val contentLayoutId: Int
        get() = R.layout.activity_splash_screen

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        viewModel.navigator = this
        enableBackButton = false
        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        try {
            val info = packageManager.getPackageInfo(
                "com.tebet.mojual",
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }
        supportActionBar?.hide()
        Handler().postDelayed({
            viewModel.loadProfile()
        }, duration.toLong())
        viewModel.profileError.observe(this, Observer<String> { error ->
            val builder = AlertDialog.Builder(this@Splash)
            builder.setMessage(getString(R.string.general_message_error))
            // add a button
            builder.setPositiveButton(getString(R.string.general_button_ok)) { dialog, which -> finish() }
            // create and show the alert dialog
            val dialog = builder.create()
            dialog.show()
        })
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

    override fun openSignUpInfoScreen() {
        finish()
        startActivity(Intent(this, SignUpInfo::class.java))
    }

    override fun openLoginScreen() {
        finish()
        startActivity(Intent(this, Login::class.java))
    }

    override fun openHomeScreen() {
        finish()
        startActivity(Intent(this, HomeActivity::class.java))
    }

    override fun openSetPasswordScreen() {
        finish()
        startActivity(Intent(this, SignUpPassword::class.java))
    }
}
