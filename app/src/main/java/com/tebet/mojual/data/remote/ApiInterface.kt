package com.tebet.mojual.data.remote

import co.sdk.auth.core.LoginConfiguration
import co.sdk.auth.core.models.AuthJson
import co.sdk.auth.core.models.Token
import com.tebet.mojual.data.models.UpdateProfileRequest
import com.tebet.mojual.data.models.UserProfile
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiInterface {
    /*
    Retrofit get annotation with our URL
    And our method that will return us the List of ContactList
    */
    @GET("data")
    fun getDataMock(): Call<ResponseBody> // http://private-2087f-taidao.apiary-mock.com/data

    @GET("iSpindel")
    fun getData(): Call<ResponseBody> // http://192.168.4.1/iSpindel?

    @POST("profile/register")
    fun register(@Body endChatSessionRequest: LoginConfiguration): Call<ResponseBody>

    @GET("profile/profile")
    fun getProfile(): Observable<AuthJson<UserProfile>>

    @PUT("profile/profile")
    fun updateProfile(@Body updateProfileRequest: UpdateProfileRequest): Call<ResponseBody>
}

