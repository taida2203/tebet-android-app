package com.tebet.mojual.common.util

import android.content.Context
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tebet.mojual.BR
import com.tebet.mojual.data.models.SensorData
import org.jsoup.Jsoup
import timber.log.Timber
import kotlin.collections.ArrayList

fun String.toSensor(): SensorData {
    val resultString = this
    val document = Jsoup.parse(resultString).select("tr")
    val sensorData = SensorData()

    document.forEach { info ->
        when {
            info.select("td")[0].html().toLowerCase().contains("tilt") -> sensorData.tilt =
                info.select("td")[1].html().replace("°", "")
            info.select("td")[0].html().toLowerCase().contains("temp") -> sensorData.temperature =
                info.select("td")[1].html().replace("°C", "")
            info.select("td")[0].html().toLowerCase().contains("battery") -> sensorData.battery =
                info.select("td")[1].html().replace("V", "")
            info.select("td")[0].html().toLowerCase().contains("gravity") -> sensorData.gravity =
                info.select("td")[1].html()
        }
    }
    return sensorData
}

fun String.toSensors(): ArrayList<SensorData> {
    if (this.isBlank()) return arrayListOf()
    val listType = object : TypeToken<List<SensorData>>() {}.getType()
    return Gson().fromJson<ArrayList<SensorData>>(this, listType)
}

fun ArrayList<SensorData>.toJson(): String {
    val listType = object : TypeToken<List<SensorData>>() {}.getType()
    val target = this
    return Gson().toJson(target, listType)
}

class Sensor(var applicationContext: Context) : BaseObservable() {
    val sensorSSID = "iSpindel"
    fun connect() {
        checkSensorStatus()
        if (!isConnected) {
            try {
                val wifiManager =
                    applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val wc = WifiConfiguration()
                wc.SSID = sensorSSID
                wc.preSharedKey = ""
                wc.status = WifiConfiguration.Status.ENABLED
                wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE)

                wifiManager.isWifiEnabled = true
                var netId = wifiManager.addNetwork(wc)
                if (netId == -1) {
                    getExistingNetworkId(wc.SSID)?.let {
                        netId = it
                    }
                }
                var result = wifiManager.scanResults
                if (netId >= 0) {
                    wifiManager.disconnect()
                    wifiManager.enableNetwork(netId, true)
                    wifiManager.reconnect()
                    isConnected = true
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun getExistingNetworkId(SSID: String): Int? {
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        var currentNetwork: WifiConfiguration? = null
        wifiManager.configuredNetworks?.let {
            currentNetwork = it.firstOrNull { wifi -> wifi.SSID.contains(SSID) }
        }
        return currentNetwork?.networkId
    }

    var isConnected: Boolean = false
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.connected)
        }

    var isEnabled: Boolean = false
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.enabled)
        }

    fun checkSensorStatus(): Sensor {
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        isConnected = wifiManager.connectionInfo.ssid.contains(sensorSSID)
        return this
    }

}