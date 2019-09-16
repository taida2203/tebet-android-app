package com.tebet.mojual.data.models

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

class LocationData : Serializable {
    var timestamp: Long? = null
    var mAccuracy: Float? = null
    var mLongitude: Double? = null
    var mLatitude: Double? = null


    constructor (location: Location) {
        this.mLatitude = location.latitude
        this.mLongitude = location.longitude
        this.mAccuracy = location.accuracy
        this.timestamp = location.time
    }

    constructor(currentPlace: iPlace) {
        this.mLatitude = currentPlace.getPosition().latitude
        this.mLongitude = currentPlace.getPosition().longitude
    }

    constructor(latitude: String, longitude: String) {
        try {
            mLongitude = java.lang.Double.parseDouble(longitude)
            mLatitude = java.lang.Double.parseDouble(latitude)
        } catch (ignored: NumberFormatException) {
        }

    }

    interface iPlace {
        fun getId(): String
        fun getPosition(): LatLng
    }
}

