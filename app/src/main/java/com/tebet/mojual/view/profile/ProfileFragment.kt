package com.tebet.mojual.view.profile

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProviders
import co.common.constant.AppConstant
import co.common.util.LanguageUtil
import co.common.util.PreferenceUtils
import co.common.view.dialog.RoundedCancelOkDialog
import co.common.view.dialog.RoundedDialog
import co.common.view.dialog.SingleChoiceDialog
import com.tebet.mojual.R
import com.tebet.mojual.common.view.LanguageChoiceDialog
import com.tebet.mojual.data.models.Language
import com.tebet.mojual.databinding.FragmentProfileBinding
import com.tebet.mojual.view.base.BaseFragment
import com.tebet.mojual.view.help.QualityHelp
import com.tebet.mojual.view.login.Login
import com.tebet.mojual.view.myasset.MyAsset
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
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun openTerm() {
        val intent = Intent(activity, QualityHelp::class.java)
        intent.putExtra(QualityHelp.EXTRA_URL, "https://mo-jual.com/terms-and-conditions")
        startActivity(intent)
    }

    override fun openPrivacy() {
        val intent = Intent(activity, QualityHelp::class.java)
        intent.putExtra(QualityHelp.EXTRA_URL, "https://mo-jual.com/privacy-policy")
        startActivity(intent)
    }

    override fun openContact() {
        val intent = Intent(activity, QualityHelp::class.java)
        intent.putExtra(QualityHelp.EXTRA_URL, "https://mo-jual.com/contact-us")
        startActivity(intent)
    }

    override fun openFAQ() {
        val intent = Intent(activity, QualityHelp::class.java)
        intent.putExtra(QualityHelp.EXTRA_URL, "https://mo-jual.com/tips")
        startActivity(intent)
    }

    override fun openChangeLanguageDialog() {
        fragmentManager?.let {
            LanguageChoiceDialog().setItems(
                arrayListOf(
                    Language(
                        LanguageUtil.LANGUAGE_INDEX_ENGLISH,
                        getString(R.string.support_language_english)
                    ),
                    Language(
                        LanguageUtil.LANGUAGE_INDEX_BAHASA,
                        getString(R.string.support_language_bahasa)
                    )
                )
            ).setCallback(object :
                SingleChoiceDialog.SingleChoiceDialogCallback<Language> {
                override fun onCancel() {
                }

                override fun onOk(selectedItem: Language?) {
                    viewModel.doChangeLanguage(selectedItem)
                }
            }).show(it, "")
        }
    }

    override fun openChangePasswordScreen() {
        startActivity(Intent(activity, ChangePassword::class.java))
    }

    override fun openMyAssetScreen() {
        startActivity(Intent(activity, MyAsset::class.java))
    }

    override fun changeLanguage(selectedItem: Language?) {
        when (selectedItem?.languageId) {
            LanguageUtil.LANGUAGE_INDEX_ENGLISH -> {
                activity?.applicationContext?.let {
                    LanguageUtil.instance.changeEnglish(activity)
                    activity?.recreate()
                }
            }
            LanguageUtil.LANGUAGE_INDEX_BAHASA -> {
                activity?.applicationContext?.let {
                    LanguageUtil.instance.changeBahasa(activity)
                    activity?.recreate()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            val pInfo = context?.packageManager?.getPackageInfo(activity?.packageName, 0)
            viewModel.version.set(pInfo?.versionName)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
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
