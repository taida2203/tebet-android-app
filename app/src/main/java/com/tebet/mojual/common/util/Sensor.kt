package com.tebet.mojual.common.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tebet.mojual.data.models.SensorData
import org.jsoup.Jsoup
import kotlin.collections.ArrayList

fun String.toSensor(): SensorData {
    val resultString = this
    val document = Jsoup.parse(resultString).select("tr")
    val sensorData = SensorData()
    document.forEach { info ->
        when {
            info.select("td")[0].html().toLowerCase().contains("tilt") -> sensorData.tilt =
                info.select("td")[1].html()
            info.select("td")[0].html().toLowerCase().contains("temp") -> sensorData.temperature =
                info.select("td")[1].html()
            info.select("td")[0].html().toLowerCase().contains("battery") -> sensorData.battery =
                info.select("td")[1].html()
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