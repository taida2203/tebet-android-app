package com.tebet.mojual.common.util

import android.content.Context
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Handler
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.databinding.library.baseAdapters.BR
import com.isupatches.wisefy.WiseFy
import com.isupatches.wisefy.callbacks.SearchForSSIDCallbacks
import com.isupatches.wisefy.constants.MISSING_PARAMETER
import com.isupatches.wisefy.constants.NETWORK_ALREADY_CONFIGURED
import com.tebet.mojual.R
import com.tebet.mojual.data.models.SensorData
import io.reactivex.Observable
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
    val listType = object : TypeToken<List<SensorData>>() {}.type
    return Gson().fromJson<ArrayList<SensorData>>(this, listType)
}

fun ArrayList<SensorData>.toJson(): String {
    val listType = object : TypeToken<List<SensorData>>() {}.type
    val target = this
    return Gson().toJson(target, listType)
}

enum class SensorStatus {
    OFF,
    ON,
    CONNECTED,
    CONNECTING
}

class Sensor(var wifiManager: WiseFy, var applicationContext: Context) : BaseObservable() {
    private var currentInternetSSID: String? = null

    companion object {
        const val sensorSSID = "iSpindel"
        const val delayWait = 5000
    }

    fun connectIOTWifi(): Observable<Boolean> {
        status = SensorStatus.CONNECTING
        return refreshStatus().concatMap {
            if (isEnabled && !isConnected) {
                status = SensorStatus.CONNECTING
                connectIOT().concatMap {
                    Observable.fromCallable<Boolean> {
                        isConnected = wifiManager.getCurrentNetwork()?.ssid?.contains(sensorSSID) ?: false
                        isConnected
                    }
                }
            } else Observable.just(true)
        }
    }

    fun refreshStatus(): Observable<Boolean> {
        isEnabled = false
        isConnected = false
        status = SensorStatus.CONNECTING
        return Observable.fromCallable {
            wifiManager.searchForSSID(sensorSSID, delayWait) ?: ""
        }.concatMap {
            isEnabled = it.isNotEmpty()
            if (isEnabled) isConnected = wifiManager.getCurrentNetwork()?.ssid?.contains(sensorSSID) == true
            Observable.just(true)
        }
    }

    private fun connectIOT(): Observable<Boolean> {
        val wfMng = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return Observable.fromCallable<Boolean> {
            wifiManager.removeNetwork(sensorSSID)
            Thread.sleep(delayWait.toLong()/2)
            var resultSSID = wifiManager.getCurrentNetwork()?.ssid
            resultSSID?.let {
                currentInternetSSID = if(resultSSID.startsWith("\"")) resultSSID.substring(1, resultSSID.length -1) else resultSSID
            }
            val listDeviceWifiSaved = wifiManager.searchForSavedNetworks(sensorSSID)
            listDeviceWifiSaved?.forEach { wfMng.removeNetwork(it.networkId) }
            Thread.sleep(delayWait.toLong()/2)
            true
        }.concatMap {
            Observable.fromCallable<Boolean> {
                val result = wifiManager.addOpenNetwork(sensorSSID)
                Thread.sleep(delayWait.toLong()/2)
                result != NETWORK_ALREADY_CONFIGURED && result != MISSING_PARAMETER
            }
        }.concatMap {
            if (!it) {
                Observable.fromCallable<Boolean> {
                    val wc = WifiConfiguration()
                    wc.SSID = "\"" + sensorSSID + "\""
                    wc.preSharedKey = ""
                    wc.status = WifiConfiguration.Status.ENABLED
                    wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE)


                    wfMng.isWifiEnabled = true
                    val netId = wfMng.addNetwork(wc)
                    Thread.sleep(delayWait.toLong() / 2)
                    netId != -1
                }
            } else {
                Observable.just(it)
            }
        }.concatMap {
            Observable.fromCallable<Boolean> {
                val listDeviceWifiSaved = wifiManager.searchForSavedNetworks(sensorSSID)
                var selectedItem = listDeviceWifiSaved?.firstOrNull { wifi ->
                    wifi.SSID.contains(sensorSSID) && wifi.SSID.contains("\"")
                }
                if (selectedItem == null) {
                    selectedItem = listDeviceWifiSaved?.firstOrNull()
                }
                if ((listDeviceWifiSaved != null && listDeviceWifiSaved.size > 1) || !it) {
                    selectedItem?.networkId?.let { it1 ->
                        wfMng.disconnect()
                        wfMng.enableNetwork(it1, true)
                        wfMng.reconnect()
                        Thread.sleep(delayWait.toLong())
                        true
                    }
                } else wifiManager.connectToNetwork(sensorSSID, delayWait)
                false
            }
        }
    }

    fun connectNetworkWifi(): Observable<Boolean> {
        return Observable.fromCallable<Boolean> {
            if (wifiManager.getCurrentNetwork()?.ssid?.contains(sensorSSID) == true) {
                wifiManager.connectToNetwork(currentInternetSSID, delayWait)
            } else true
        }
    }

    var isConnected: Boolean = false
        @Bindable get
        set(value) {
            field = value
            when {
                value -> status = SensorStatus.CONNECTED
                else -> if (isEnabled) status = SensorStatus.ON
            }
            notifyPropertyChanged(BR.connected)
        }

    var isEnabled: Boolean = false
        @Bindable get
        set(value) {
            field = value
            when {
                value -> if (status == SensorStatus.OFF) status = SensorStatus.ON
                else -> status = SensorStatus.OFF
            }
            notifyPropertyChanged(BR.enabled)
        }

    var status: SensorStatus = SensorStatus.OFF
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.status)
            notifyPropertyChanged(BR.statusDisplay)
        }

    val statusDisplay: String?
        @Bindable get (){
            return when (status) {
                SensorStatus.CONNECTING -> Utility.getInstance().getString(R.string.check_quality_iot_connecting)
                SensorStatus.OFF -> Utility.getInstance().getString(R.string.check_quality_iot_not_turn_off)
                SensorStatus.ON -> Utility.getInstance().getString(R.string.check_quality_iot_not_connect)
                SensorStatus.CONNECTED -> Utility.getInstance().getString(R.string.check_quality_iot_connected)
            }
        }
}