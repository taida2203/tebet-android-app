package com.tebet.mojual

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import co.sdk.auth.AuthSdk
import co.sdk.auth.core.AuthAccountKitMethod
import co.sdk.auth.core.LoginConfiguration
import co.sdk.auth.core.models.ApiCallBack
import co.sdk.auth.core.models.LoginException
import co.sdk.auth.core.models.Token
import co.sdk.auth.network.ServiceHelper
import com.tebet.mojual.data.models.SensorData
import com.tebet.mojual.network.ApiService
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // "http://private-2087f-taidao.apiary-mock.com"
        // "http://192.168.4.1"
        AuthSdk.init(this, "http://192.168.4.1", "", "", "")


        btnLogin.setOnClickListener {
            AuthSdk.instance.login(this, AuthAccountKitMethod(), LoginConfiguration(), object : ApiCallBack<Token>() {
                override fun onSuccess(responeCode: Int, response: Token?) {
                }

                override fun onFailed(exeption: LoginException) {
                }
            })
        }
        getData()

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let { AuthSdk.instance.onActivityResult(requestCode, resultCode, it) }
    }
}
