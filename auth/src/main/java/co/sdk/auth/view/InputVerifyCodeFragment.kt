package co.sdk.auth.view

import android.content.Context
import android.os.Bundle
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        edtCodes = listOf<TextView>(et_code_1, et_code_2, et_code_3, et_code_4, et_code_5, et_code_6)
        edtCodes.forEach { textView ->
            textView.setOnClickListener {
                if (et_code.requestFocus()) {
                    et_code.clearFocus()
                }
                it.clearFocus()
                et_code.requestFocus()
                showSoftKeyboard(et_code)
            }
        }
        et_code.afterTextChanged {
            for (i in it.indices) {
                edtCodes[i].text = it.substring(i, i + 1)
            }
            for (i in it.length..5) {
                edtCodes[i].text = ""
            }
            if (it.length >= 6) {
                (activity as LoginOTPActivity).verifyCode(et_code.text.toString())
            }
        }

    }

    private fun showSoftKeyboard(view: View) {
        val inputMethodManager =
            activity?.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        view.requestFocus()
        inputMethodManager.showSoftInput(view, 0)
    }

    internal fun resetInputPin() {
        tv_error.text = ""
        et_code.setText("")

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
            btn_close.isEnabled = it
        }
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