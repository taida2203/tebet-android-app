package co.sdk.auth.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

import com.tebet.mojual.sdk.auth.R
import kotlinx.android.synthetic.main.fragment_input_phone.*

class InputPhoneFragment : Fragment() , IFragmentAction{
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_input_phone, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ccp.registerCarrierNumberEditText(etPhone)

        btnGetOTP.setOnClickListener {
            if (ccp.isValidFullNumber) {
                (activity as LoginOTPActivity).requestCode(ccp.fullNumberWithPlus)
            } else {
                Toast.makeText(activity, "Wrong phone number", Toast.LENGTH_SHORT).show()
            }
        }
        showSoftKeyboard(etPhone)
    }

    private fun showSoftKeyboard(view: View) {
        val inputMethodManager =
            activity?.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        view.requestFocus()
        inputMethodManager.showSoftInput(view, 0)
    }

    override fun disableUI(isDisable: Boolean?) {
        isDisable?.let {
            btnGetOTP.isEnabled = it
        }
    }
}
