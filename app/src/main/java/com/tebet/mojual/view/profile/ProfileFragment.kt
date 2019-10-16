package com.tebet.mojual.view.profile

import android.content.Intent
import android.os.Bundle
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProviders
import co.common.constant.AppConstant
import co.common.util.LanguageUtil
import co.common.util.PreferenceUtils
import co.common.view.dialog.LanguageChoiceDialog
import co.common.view.dialog.RoundedCancelOkDialog
import co.common.view.dialog.RoundedDialog
import co.common.view.dialog.SingleChoiceDialog
import com.tebet.mojual.R
import com.tebet.mojual.databinding.FragmentProfileBinding
import com.tebet.mojual.view.base.BaseFragment
import com.tebet.mojual.view.login.Login
import com.tebet.mojual.view.profilechangepass.ChangePassword
import com.tebet.mojual.view.profilepin.PinCode

class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>(), ProfileNavigator {

    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: ProfileViewModel
        get() = ViewModelProviders.of(this, factory).get(ProfileViewModel::class.java)

    override val layoutId: Int
        get() = R.layout.fragment_profile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigator = this
        viewModel.loadData()
    }

    override fun openLoginScreen() {
        activity?.finish()
        val intent = Intent(activity, Login::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    override fun openChangeLanguageDialog() {
        fragmentManager?.let {
            LanguageChoiceDialog().setCallback(object :
                SingleChoiceDialog.SingleChoiceDialogCallback<String> {
                override fun onCancel() {
                }

                override fun onOk(selectedItem: String?) {
                    viewModel.doChangeLanguage(selectedItem)
                }
            }).show(it, "")
        }
    }

    override fun openChangePasswordScreen() {
        startActivity(Intent(activity, ChangePassword::class.java))
    }

    override fun changeLanguage(selectedItem: String?) {
        when (selectedItem) {
            getString(R.string.support_language_english) -> {
                activity?.applicationContext?.let {
                    LanguageUtil.instance.changeEnglish(it)
                    activity?.recreate()
                }
            }
            getString(R.string.support_language_bahasa) -> {
                activity?.applicationContext?.let {
                    LanguageUtil.instance.changeBahasa(it)
                    activity?.recreate()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.pin.set(PreferenceUtils.getString(AppConstant.PIN_CODE, ""))
    }
    override fun openPinCodeScreen() {
        viewModel.pin.set(PreferenceUtils.getString(AppConstant.PIN_CODE, ""))
        if (viewModel.pin.get()?.isEmpty()!!) {
            startActivity(
                Intent(activity, PinCode::class.java).putExtra(
                    AppConstant.PIN_TYPE_INPUT,
                    PinCode.ADD_PIN
                )
            )
        } else {
            RoundedCancelOkDialog(this.getString(R.string.pin_message_remove)).setRoundedDialogCallback(object : RoundedDialog.RoundedDialogCallback {
                override fun onFirstButtonClicked(selectedValue: Any?) {
                    // No need handle cancel this case
                }

                override fun onSecondButtonClicked(selectedValue: Any?) {
                    PreferenceUtils.saveString(AppConstant.PIN_CODE, "")
                    viewModel.pin.set(PreferenceUtils.getString(AppConstant.PIN_CODE, ""))
                }
            }).show(this.childFragmentManager, "activity")
        }
    }
}
