package com.tebet.mojual.view

import android.content.Intent
import android.os.Bundle
import co.sdk.auth.network.ServiceHelper
import com.tebet.mojual.R
import com.tebet.mojual.common.base.BaseActivity
import com.tebet.mojual.data.models.UpdateProfileRequest
import com.tebet.mojual.network.ApiService
import kotlinx.android.synthetic.main.activity_sign_up_password.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class SignUpPassword : BaseActivity() {
    override val contentLayoutId: Int
        get() = R.layout.activity_sign_up_password

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        btnNext.setOnClickListener {
            showLoading(true)
            val updateProfileRequest = UpdateProfileRequest()
            updateProfileRequest.password = tvPassword.text.toString().trim()
            ServiceHelper.createService(ApiService::class.java).updateProfile(updateProfileRequest).enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    showLoading(false)
                    handleError(t)
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    showLoading(false)
                    this@SignUpPassword.finish()
                    startActivity(Intent(this@SignUpPassword, SignUpInfo::class.java))
                }
            })
        }
    }
}
