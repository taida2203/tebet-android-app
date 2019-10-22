package co.sdk.auth.view

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tebet.mojual.sdk.auth.R
import kotlinx.android.synthetic.main.fragment_input_verify_code.*

class InputVerifyCodeFragment : Fragment(), IFragmentAction {
    private var edtCodes = listOf<TextView>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_input_verify_code, container, false)
    }

    companion object {
        const val PROGRESS_SPEED = 50
        const val TIMEOUT: Long = 60 * 1000
    }

    internal var timeLeft = TIMEOUT

    internal var handler = Handler()
    private var isActivityRunning: Boolean = false
    private val runnable: Runnable = object : Runnable {
        override fun run() {
            updateCountDownStatus()
            if (!isTimeout()) {
                timeLeft -= PROGRESS_SPEED
                handler.postDelayed(this, PROGRESS_SPEED.toLong())
            }
        }
    }

    private fun updateCountDownStatus() {
        if (isActivityRunning) {
            if (!isTimeout()) {
                when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> progressBar?.setProgress(timeLeft.toInt(), true)
                    else -> progressBar?.progress = timeLeft.toInt()
                }
            } else {
                showResend()
            }
        }
    }

    private fun showResend() {
        btn_resend.visibility = View.VISIBLE
        progressBar?.visibility = View.GONE
        error.text = ""
        disableInputOTP(true)
    }

    private fun showCountdown() {
        btn_resend?.visibility = View.GONE
        progressBar?.visibility = View.VISIBLE
        progressBar?.max = TIMEOUT.toInt()
        timeLeft = TIMEOUT
        et_code?.setText("")
        disableInputOTP(false)
        requestFocus()
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, 500)
    }

    private fun requestFocus() {
        if (et_code.requestFocus()) {
            et_code.clearFocus()
        }
        edtCodes.forEach(TextView::clearFocus)
        et_code.requestFocus()
        showSoftKeyboard(et_code)
    }

    private fun disableInputOTP(isDisable: Boolean) {
        et_code.isEnabled = !isDisable
        for (i in edtCodes.indices) {
            edtCodes[i].isEnabled = !isDisable
        }
    }

    fun isTimeout(): Boolean {
        return timeLeft <= 0
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        edtCodes =
            listOf<TextView>(et_code_1, et_code_2, et_code_3, et_code_4, et_code_5, et_code_6)
        edtCodes.forEach {
            it.setOnClickListener {
                requestFocus()
            }
        }
        et_code?.afterTextChanged {
            for (i in it.indices) {
                edtCodes[i].text = it.substring(i, i + 1)
            }
            for (i in it.length..5) {
                edtCodes[i].text = ""
            }
            if (it.length >= 6) {
                hideKeyboard()
                (activity as LoginOTPActivity).verifyCode(et_code.text.toString())
            }
        }
        btn_resend.setOnClickListener {
            (activity as LoginOTPActivity).requestCode()
            showCountdown()
        }
        showCountdown()
    }

    private fun showSoftKeyboard(view: View) {
        val inputMethodManager =
            activity?.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        view.requestFocus()
        inputMethodManager.showSoftInput(view, 0)
    }

    internal fun resetInputPin() {
        et_code?.setText("")
    }

    internal fun showError(err: String) {
        error.text = err
    }

    fun hideKeyboard() {
        val view = activity?.currentFocus
        if (view != null) {
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun disableUI(isDisable: Boolean?) {
        isDisable?.let {
        }
    }

    override fun onStart() {
        super.onStart()
        isActivityRunning = true
    }

    override fun onStop() {
        super.onStop()
        isActivityRunning = false
    }
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

    })
}