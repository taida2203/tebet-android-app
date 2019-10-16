package co.sdk.auth.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.tebet.mojual.sdk.auth.R
import kotlinx.android.synthetic.main.activity_login_otp.*
import java.lang.Exception
import java.util.concurrent.TimeUnit

class LoginOTPActivity : AppCompatActivity() {
    private var verificationId: String? = null
    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_otp)
        currentFragment = InputPhoneFragment()
        openFragment(currentFragment as InputPhoneFragment, R.id.placeHolderChild)
    }

    fun verifyCode(text: String) {
        verificationId?.let {
            val credential = PhoneAuthProvider.getCredential(it, text)
            signInWithPhoneAuthCredential(credential)
        }
    }

    private fun openFragment(fragment: Fragment, placeHolder: Int, tag: String = "") {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !isDestroyed || Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.replace(placeHolder, fragment, tag)
            transaction.commitAllowingStateLoss()
        }
    }

    fun requestCode(phone: String) {
        showLoading(true)
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                showLoading(false)
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                showLoading(false)
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }
                handleError(e)
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                this@LoginOTPActivity.verificationId = verificationId

                showLoading(false)

                currentFragment = InputVerifyCodeFragment()
                openFragment(currentFragment as InputVerifyCodeFragment, R.id.placeHolderChild)

            }
        }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phone, // Phone number to verify
            60, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            this, // Activity (for callback binding)
            callbacks
        ) // OnVerificationStateChangedCallbacks
    }

    private fun handleError(err: Exception) {
        Toast.makeText(this@LoginOTPActivity, err.message, Toast.LENGTH_SHORT).show()
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        showLoading(true)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .continueWithTask { it.result?.user?.getIdToken(true) }
            .addOnCompleteListener { task ->
                showLoading(false)
                (currentFragment as InputVerifyCodeFragment).resetInputPin()
                task.result?.token?.let {
                    val intent = Intent()
                    intent.putExtra("EXTRA_ID_TOKEN", it)
                    setResult(Activity.RESULT_OK, intent)
                    hideKeyboard()
                    finish()
                }
            }.addOnFailureListener {
                handleError(it)
                showLoading(false)
                (currentFragment as InputVerifyCodeFragment).resetInputPin()
            }
    }

    override fun onBackPressed() {
        if (currentFragment is InputVerifyCodeFragment) {
            currentFragment = InputPhoneFragment()
            openFragment(currentFragment as InputPhoneFragment, R.id.placeHolderChild)
            return
        }
        super.onBackPressed()
    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun showLoading(isLoading: Boolean) {
        progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
        if (currentFragment is IFragmentAction) (currentFragment as IFragmentAction).disableUI(isLoading)
    }
}
