package com.tebet.mojual.data.remote

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET

interface ApiSensorHelper {
    @GET("data")
    fun scanSensorDataMock(): Observable<ResponseBody> // http://private-2087f-taidao.apiary-mock.com/data

    @GET("iSpindel")
    fun scanSensorData(): Observable<ResponseBody> // http://192.168.4.1/iSpindel?
}

