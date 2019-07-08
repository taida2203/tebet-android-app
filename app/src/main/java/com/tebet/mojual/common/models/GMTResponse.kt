package com.tebet.mojual.common.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class GMTResponse {

    @SerializedName("timezone")
    @Expose
    private var timezone: String? = null
    @SerializedName("gmtOffset")
    @Expose
    private var gmtOffset: String? = null
    @SerializedName("utcOffset")
    @Expose
    private var utcOffset: String? = null
    @SerializedName("location")
    @Expose
    private var location: String? = null

    fun getTimezone(): String? {
        return timezone
    }

    fun setTimezone(timezone: String) {
        this.timezone = timezone
    }

    fun getGmtOffset(): String? {
        return gmtOffset
    }

    fun setGmtOffset(gmtOffset: String) {
        this.gmtOffset = gmtOffset
    }

    fun getUtcOffset(): String? {
        return utcOffset
    }

    fun setUtcOffset(utcOffset: String) {
        this.utcOffset = utcOffset
    }

    fun getLocation(): String? {
        return location
    }

    fun setLocation(location: String) {
        this.location = location
    }

}
