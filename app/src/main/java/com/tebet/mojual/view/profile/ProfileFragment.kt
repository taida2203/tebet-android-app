package com.tebet.mojual.view.profile

import android.content.Intent
import android.os.Bundle
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProviders
import co.common.util.LanguageUtil
import co.common.view.dialog.LanguageChoiceDialog
import co.common.view.dialog.SingleChoiceDialog
import com.tebet.mojual.R
import com.tebet.mojual.databinding.FragmentProfileBinding
import com.tebet.mojual.view.base.BaseFragment
import com.tebet.mojual.view.login.Login

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
        val dialog = LanguageChoiceDialog()
        dialog.setCallback(object :
            SingleChoiceDialog.SingleChoiceDialogCallback<String> {
            override fun onCancel() {
            }

            override fun onOk(selectedItem: String?) {
                when (selectedItem) {
                    getString(R.string.support_language_english) -> {
                        activity?.applicationContext?.let { LanguageUtil.instance.changeEnglish(it) }
                    }
                    getString(R.string.support_language_bahasa) -> {
                        activity?.applicationContext?.let { LanguageUtil.instance.changeBahasa(it) }
                    }
                }
                dialog.dismiss()
                activity?.recreate()
            }
        }).show(fragmentManager, "")
    }
}
