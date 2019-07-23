package com.tebet.mojual.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import co.sdk.auth.AuthSdk
import co.sdk.auth.core.models.ApiCallBack
import co.sdk.auth.core.models.LoginException
import co.sdk.auth.network.ServiceHelper
import com.tebet.mojual.R
import com.tebet.mojual.common.base.BaseActivity
import com.tebet.mojual.data.models.SensorData
import com.tebet.mojual.network.ApiService
import kotlinx.android.synthetic.main.activity_home.*
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Response

class HomeActivity : BaseActivity() {
    override val contentLayoutId: Int
        get() = R.layout.activity_home

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        title = "Home"
//        getData()
        btnLogout.setOnClickListener {
            AuthSdk.instance.logout(false, object : ApiCallBack<Any>() {
                override fun onSuccess(responeCode: Int, response: Any?) {
                    finish()
                    startActivity(Intent(this@HomeActivity, Login::class.java))
                }

                override fun onFailed(exeption: LoginException) {
                }
            })
        }
    }

    private fun getData() {
        Handler().postDelayed({
            ServiceHelper.createService(ApiService::class.java).getData().enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    val resultString = response.body()?.string()
                    val document = Jsoup.parse(resultString).select("tr")
                    val sensorData = SensorData()
                    document.forEach { info ->
                        when {
                            info.select("td")[0].html().toLowerCase().contains("tilt") -> sensorData.tilt = info.select("td")[1].html()
                            info.select("td")[0].html().toLowerCase().contains("temp") -> sensorData.temperature = info.select("td")[1].html()
                            info.select("td")[0].html().toLowerCase().contains("battery") -> sensorData.battery = info.select("td")[1].html()
                            info.select("td")[0].html().toLowerCase().contains("gravity") -> sensorData.gravity = info.select("td")[1].html()
                        }
                    }
                    tvTilt.text = sensorData.tilt
                    tvTemp.text = sensorData.temperature
                    tvBattery.text = sensorData.battery
                    tvGravity.text = sensorData.gravity
                }
            })
            getData()
        }, 5000)
    }
}
