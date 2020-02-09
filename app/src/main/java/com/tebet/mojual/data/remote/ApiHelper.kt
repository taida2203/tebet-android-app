package com.tebet.mojual.data.remote

import co.sdk.auth.core.models.AuthJson
import co.sdk.auth.core.models.LoginConfiguration
import com.tebet.mojual.data.models.*
import com.tebet.mojual.data.models.request.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiHelper {
    /*
    Retrofit get annotation with our URL
    And our method that will return us the List of ContactList
    */
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
    fun createOrder(@Body createOrderRequest: CreateOrderRequest): Observable<AuthJson<Order>>

    @POST("order/order/document")
    fun createOrderDocument(@Body createOrderDocumentRequests: List<CreateOrderDocumentRequest>): Observable<AuthJson<EmptyResponse>>

    @PUT("order/confirm/{orderId}")
    fun confirmOrder(@Path(value = "orderId") orderId: Long, @Body qualityList: List<OrderContainer>): Observable<AuthJson<Order>>

    @PUT("order/quality/{orderId}")
    fun updateOrderQuality(@Path(value = "orderId") orderId: Long, @Body qualityList: List<Quality>): Observable<AuthJson<Order>>

    @GET("order/asset")
    fun getAsserts(
        @Query(value = "profileId") profileId: String, @Query(value = "offset") offset: Int? = null, @Query(
            value = "limit"
        ) limit: Int? = null
    ): Observable<AuthJson<List<Asset>>>

    @GET("order/price")
    fun getNext7DaysPrice(): Observable<AuthJson<List<Price>>>

    @GET("order/order")
    fun getOrderDetail(@Query(value = "orderId") orderId: Long, @Query(value = "loadCustomer") loadCustomer: Boolean? = null, @Query(value = "loadContainers") loadContainers: Boolean? = null, @Query(value = "loadDocuments") loadDocuments: Boolean? = null): Observable<AuthJson<OrderDetail>>

    @POST("order/search")
    fun searchOrders(@Body searchOrderRequest: SearchOrderRequest): Observable<AuthJson<Paging<Order>>>
    @POST("communication/device/register")
    fun registerDevice(@Body deviceRegisterRequest: DeviceRegisterRequest): Observable<AuthJson<EmptyResponse>>

    @POST("communication/device/unregister")
    fun unRegisterDevice(@Body deviceRegisterRequest: DeviceRegisterRequest): Observable<AuthJson<EmptyResponse>>

    @PUT("communication/notification/read/{notificationId}")
    fun markRead(@Path(value = "notificationId") notificationId: Long): Observable<AuthJson<Message>>

    @GET("communication/notification/read")
    fun getUnreadCount(): Observable<AuthJson<Long>>

    @POST("communication/notification/history")
    fun getMessages(@Body messageRequest: MessageRequest): Observable<AuthJson<Paging<Message>>>

    @POST("order/mobile-info-scan")
    fun scanLocation(@Body scanLocationRequest: ScanLocationRequest): Observable<AuthJson<EmptyResponse>>
}

