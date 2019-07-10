package co.sdk.auth.network.api

import co.sdk.auth.core.models.GoogleTokenAuthResponse
import co.sdk.auth.core.models.LoginInput
import co.sdk.auth.core.models.AuthJson
import co.sdk.auth.core.models.LogoutInput
import co.sdk.auth.core.models.Token
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    /*
    Retrofit get annotation with our URL
    And our method that will return us the List of ContactList
    */
    @POST("v2/auth/login")
    fun login(@Body loginInput: LoginInput): Call<AuthJson<Token>>

    @POST("oauth2/v4/token")
    @FormUrlEncoded
    fun convertGoogleToken(@Field("grant_type") grantType: String,
                           @Field("client_id") client_id: String,
                           @Field("client_secret") client_secret: String,
                           @Field("redirect_uri") redirect_uri: String?,
                           @Field("code") code: String,
                           @Field("id_token") id_token: String): Call<GoogleTokenAuthResponse>

    @POST("v1/mobile/logout")
    fun logout(@Body logoutInput: LogoutInput): Call<AuthJson<Any>>
}
