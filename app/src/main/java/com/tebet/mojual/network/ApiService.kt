package com.tebet.mojual.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    /*
    Retrofit get annotation with our URL
    And our method that will return us the List of ContactList
    */
    @GET("data")
    fun getDataMock(): Call<ResponseBody> // http://private-2087f-taidao.apiary-mock.com/data

    @GET("iSpindel")
    fun getData(): Call<ResponseBody> // http://192.168.4.1/iSpindel?
}

