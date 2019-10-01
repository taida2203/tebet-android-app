package com.tebet.mojual.view.profilepin

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProviders
import co.common.constant.AppConstant
import co.common.util.PreferenceUtils
import com.tebet.mojual.R
import com.tebet.mojual.common.util.afterTextChanged
import com.tebet.mojual.databinding.ActivityPinCodeBinding
import com.tebet.mojual.databinding.ItemHomeIconBinding
import com.tebet.mojual.view.base.BaseActivity
import kotlinx.android.synthetic.main.activity_pin_code.*

open class PinCode : BaseActivity<ActivityPinCodeBinding, PinCodeViewModel>(),
    PinCodeNavigator {
    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: PinCodeViewModel
        get() = ViewModelProviders.of(this, factory).get(PinCodeViewModel::class.java)

    override val contentLayoutId: Int
        get() = R.layout.activity_pin_code

    private var topRightViewBinding: ItemHomeIconBinding? = null

    private var edtCodes = listOf<TextView>()

    companion object {
        val CHECK_PIN = 0
        val ADD_PIN = 1
        val VERIFY_PIN = 2
    }


    private var screenMode = CHECK_PIN
    private var tempPin: String = ""
    private var retryCount = 0

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        viewModel.navigator = this
        title = getString(R.string.pin_enter)
        viewModel.loadData()
        screenMode = intent.getIntExtra(AppConstant.PIN_TYPE_INPUT, CHECK_PIN)
        edtCodes = listOf<TextView>(et_code_1, et_code_2, et_code_3, et_code_4)
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
            if (it.trim().isNotEmpty()) {
                for (i in it.indices) {
                    edtCodes[i].text = it.substring(i, i + 1)
                }
                for (i in it.length..3) {
                    edtCodes[i].text = ""
                }
                if (it.length >= 4) {
                    when (screenMode) {
                        CHECK_PIN -> {
                            title = getString(R.string.pin_enter)
                            val currentCode =
                                PreferenceUtils.getString(AppConstant.PIN_CODE, tempPin)
                            if (it == currentCode) {
                                setResult(RESULT_OK)
                                openHomeScreen()
                            } else {
                                retryCount++
                                if (retryCount > 5) {
                                    resetInputPin()
                                    tv_error.setText(R.string.pin_message_not_matched)
                                    btn_close.performClick()
                                } else {
                                    resetInputPin()
                                    tv_error.setText(R.string.pin_message_wrong)
                                }
                            }
                        }
                        ADD_PIN -> {
                            title = getString(R.string.pin_confirm)
                            tempPin = it
                            screenMode = VERIFY_PIN
                            tv_title.text = getString(R.string.pin_confirm)
                            resetInputPin()
                        }
                        VERIFY_PIN -> if (it == tempPin) {
                            hideKeyboard()
                            PreferenceUtils.saveString(AppConstant.PIN_CODE, tempPin)
                            setResult(RESULT_OK)
                            openHomeScreen()
                        } else {
                            resetInputPin()
                            tv_error.setText(R.string.pin_message_not_matched)
                        }
                    }
                }
            }
        }

    }

    override fun openHomeScreen() {
        hideKeyboard()
        finish()
    }

    private fun showSoftKeyboard(view: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        view.requestFocus()
        inputMethodManager.showSoftInput(view, 0)
    }

    private fun resetInputPin() {
        tv_error.text = ""
        et_code.setText("")

    }
}
