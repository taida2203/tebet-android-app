package com.tebet.mojual

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import co.squline.sdk.auth.AuthSdk
import co.squline.sdk.auth.network.ServiceHelper
import com.google.gson.Gson
import com.tebet.mojual.data.models.SensorData
import com.tebet.mojual.network.ApiService
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AuthSdk.init(this, "", "", "", "")
//        AuthSdk.instance.login(this, AuthAccountKitMethod(), LoginConfiguration(), object : ApiCallBack<Token>() {
//            override fun onSuccess(responeCode: Int, response: Token?) {
//            }
//
//            override fun onFailed(exeption: LoginException) {
//            }
//        })


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
                    Toast.makeText(this@MainActivity, Gson().toJson(sensorData), Toast.LENGTH_SHORT).show()
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
