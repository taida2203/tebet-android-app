package com.tebet.mojual.data.remote

import co.sdk.auth.core.LoginConfiguration
import co.sdk.auth.core.models.AuthJson
import com.tebet.mojual.data.models.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiHelper {
    /*
    Retrofit get annotation with our URL
    And our method that will return us the List of ContactList
    */
    @GET("data")
    fun getDataMock(): Call<ResponseBody> // http://private-2087f-taidao.apiary-mock.com/data

    @GET("iSpindel")
    fun getData(): Call<ResponseBody> // http://192.168.4.1/iSpindel?

    @POST("profile/register")
    fun register(@Body loginConfiguration: LoginConfiguration): Observable<AuthJson<EmptyResponse>>

    @GET("profile/profile")
    fun getProfile(): Observable<AuthJson<UserProfile>>

    @GET("common/bank")
    fun getBanks(): Observable<AuthJson<List<Bank>>>

    @GET("common/city")
    fun getCities(): Observable<AuthJson<List<City>>>

    @GET("common/region")
    fun getRegions(): Observable<AuthJson<List<Region>>>

    @PUT("profile/profile")
    fun updateProfile(@Body updateProfileRequest: UserProfile): Observable<AuthJson<UserProfile>>

    @PUT("profile/profile")
    fun updatePassword(@Body updateProfileRequest: UpdatePasswordRequest): Observable<AuthJson<EmptyResponse>>

    @Multipart
    @POST("storage/file")
    fun uploadImage(@Part folder: MultipartBody.Part, @Part file: MultipartBody.Part): Observable<AuthJson<String>>

    @POST("order/order")
    fun createOrder(@Body createOrderRequest: CreateOrderRequest): Observable<AuthJson<EmptyResponse>>

    @GET("order/asset")
    fun getAsserts(
        @Query(value = "profileId") profileId: String, @Query(value = "offset") offset: Int? = null, @Query(
            value = "limit"
        ) limit: Int? = null
    ): Observable<AuthJson<List<Asset>>>
}

