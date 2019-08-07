package com.tebet.mojual.data.remote

import com.tebet.mojual.common.constant.ConfigEnv
import com.tebet.mojual.data.models.google_map.GeoCodeResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiGoogleHelper {
    //https://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452&key=YOUR_API_KEY
    @GET("geocode/json")
    fun getReserveGeoLocation(@Query(value = "latlng") latlng: String, @Query(value = "key") key: String = ConfigEnv.googleApiKey): Observable<GeoCodeResponse>
}

