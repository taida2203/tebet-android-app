package com.tebet.mojual.view.forgotpassword

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import co.sdk.auth.network.ServiceHelper
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.ViewModelProviderFactory
import com.tebet.mojual.common.base.BaseActivity
import com.tebet.mojual.data.models.UpdateProfileRequest
import com.tebet.mojual.data.remote.ApiInterface
import com.tebet.mojual.databinding.ActivityLoginBinding
import com.tebet.mojual.view.HomeActivity
import com.tebet.mojual.view.base.BaseActivityNew
import com.tebet.mojual.view.login.LoginViewModel
import kotlinx.android.synthetic.main.activity_sign_up_password.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

open class ForgotPassword : BaseActivityNew<ActivityLoginBinding, ForgotPasswordViewModel>(), ForgotPasswordNavigator {
    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: ForgotPasswordViewModel
        get() = ViewModelProviders.of(this, factory).get(ForgotPasswordViewModel::class.java)

    override fun openHomeScreen() {
        this@ForgotPassword.finish()
        startActivity(Intent(this@ForgotPassword, HomeActivity::class.java))
    }

    override val contentLayoutId: Int
        get() = R.layout.activity_sign_up_password

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
//        title = "Input new password"
        btnNext.setOnClickListener {
            val updateProfileRequest = UpdateProfileRequest()
            updateProfileRequest.password = tvPassword.text?.trim().toString()
//            ServiceHelper.createService(ApiInterface::class.java).updateProfile(updateProfileRequest)
//                .enqueue(object : retrofit2.Callback<ResponseBody> {
//                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                        handleError(t)
//                    }
//
//                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                    }
//                })
        }
    }
}
