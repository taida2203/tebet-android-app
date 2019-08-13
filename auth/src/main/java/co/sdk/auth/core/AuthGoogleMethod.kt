package co.sdk.auth.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient

import java.util.concurrent.TimeUnit

import co.sdk.auth.core.models.ApiCallBack
import co.sdk.auth.core.models.GoogleTokenAuthResponse
import co.sdk.auth.core.models.LoginConfiguration
import co.sdk.auth.core.models.LoginException
import co.sdk.auth.network.api.ApiService
import co.sdk.auth.utils.Utility
import com.tebet.mojual.sdk.auth.BuildConfig
import com.tebet.mojual.sdk.auth.R
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthGoogleMethod : AuthMethod, GoogleApiClient.OnConnectionFailedListener {
    private var callback: ApiCallBack<LoginConfiguration>? = null
    private var configuration: LoginConfiguration? = null
    private var finalToken: String? = null

    fun isLoggedIn(context: Activity): Boolean {
        val googleAccount = GoogleSignIn.getLastSignedInAccount(context)
        return googleAccount != null && googleAccount.serverAuthCode != null && !TextUtils.isEmpty(finalToken)
    }

    override fun brandedLogin(context: Activity, configuration: LoginConfiguration, callback: ApiCallBack<LoginConfiguration>) {
        this.callback = callback
        this.configuration = configuration

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(configuration.googleClientId)
                .requestServerAuthCode(configuration.googleClientId, false)
                .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(context, gso)
        if (isLoggedIn(context)) {
            this.configuration!!.token = finalToken
            this.configuration?.let { callback.onSuccess(200, it) }
        } else {
            if (GoogleSignIn.getLastSignedInAccount(context) != null && GoogleSignIn.getLastSignedInAccount(context)!!.serverAuthCode != null && finalToken == null) {
                val googleAccount = GoogleSignIn.getLastSignedInAccount(context)
                handleSignInResult(context, googleAccount)
            } else {
                val signInIntent = mGoogleSignInClient.signInIntent
                context.startActivityForResult(signInIntent, REQUEST_CODE_LOGIN_GOOGLE)
            }
        }
    }

    fun getId(context: Activity): String? {
        return if (isLoggedIn(context)) {
            GoogleSignIn.getLastSignedInAccount(context)!!.id
        } else null
    }

    fun getEmail(context: Activity): String? {
        return if (isLoggedIn(context)) {
            GoogleSignIn.getLastSignedInAccount(context)!!.email
        } else null
    }

    fun getFirstName(context: Activity): String? {
        return if (isLoggedIn(context)) {
            GoogleSignIn.getLastSignedInAccount(context)!!.givenName
        } else null
    }

    fun getLastName(context: Activity): String? {
        return if (isLoggedIn(context)) {
            GoogleSignIn.getLastSignedInAccount(context)!!.familyName
        } else null
    }
    override fun logout(context: Context, forceLogout: Boolean, callback: ApiCallBack<*>?) {
        if (GoogleSignIn.getLastSignedInAccount(context) != null) {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestIdToken(configuration!!.googleClientId)
                    .requestServerAuthCode(configuration!!.googleClientId)
                    .build()
            val mGoogleSignInClient = GoogleSignIn.getClient(context, gso)
            mGoogleSignInClient.signOut()
        }
        finalToken = null
        callback?.onSuccess(200, null)
    }

    private fun handleSignInResult(context: Context?, account: GoogleSignInAccount?) {
        val httpClient = OkHttpClient()
        val client = httpClient.newBuilder()

        client.readTimeout(60, TimeUnit.SECONDS)
        client.writeTimeout(60, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            client.addInterceptor(logging)
        }

        val retrofit = Retrofit.Builder()
                .baseUrl("https://www.googleapis.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build()
                .create(ApiService::class.java)
        configuration!!.googleClientId?.let {
            account!!.serverAuthCode?.let { it1 ->
                account.idToken?.let { it2 ->
                    retrofit.convertGoogleToken("authorization_code",
                            it,
                            "BEvEg32q3MJQObE1KPNdDcUs", null,
                            it1,
                            it2)
                            .enqueue(object : retrofit2.Callback<GoogleTokenAuthResponse> {
                                override fun onResponse(call: Call<GoogleTokenAuthResponse>, response: retrofit2.Response<GoogleTokenAuthResponse>) {
                                    finalToken = null
                                    if (response.isSuccessful && response.body() != null && response.body()!!.accessToken != null) {
                                        finalToken = response.body()!!.accessToken
                                        if (callback != null) {
                                            configuration!!.token = response.body()!!.accessToken
                                            configuration?.let { callback!!.onSuccess(200, it) }
                                        }
                                    } else {
                                        context?.let { it3 ->
                                            logout(it3, true, object : ApiCallBack<Any>() {
                                                override fun onSuccess(responeCode: Int, response: Any?) {
                                                    if (callback != null) {
                                                        callback!!.onFailed(LoginException(502, Utility.instance.getString(
                                                            R.string.general_message_error)))
                                                    }
                                                }

                                                override fun onFailed(exeption: LoginException) {
                                                    if (callback != null) {
                                                        callback!!.onFailed(LoginException(502, exeption.errorMessage))
                                                    }
                                                }
                                            })
                                        }
                                    }
                                }

                                override fun onFailure(call: Call<GoogleTokenAuthResponse>, t: Throwable) {
                                    finalToken = null
                                    if (callback != null) {
                                        callback!!.onFailed(LoginException(502, t.message ?: ""))
                                    }
                                }
                            })
                }
            }
        }

    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_CODE_LOGIN_GOOGLE) {
            if (resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    handleSignInResult(null, task.result)
                } catch (e: Exception) {
                    if (callback != null) {
                        callback!!.onFailed(LoginException(502, e.message ?: ""))
                    }
                }

            } else {
                if (callback != null) {
                    callback!!.onFailed(LoginException(502, ""))
                }
            }
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        if (callback != null) {
            callback!!.onFailed(LoginException(502, connectionResult.errorMessage ?: ""))
        }
    }

    companion object {
        val REQUEST_CODE_LOGIN_GOOGLE = 100
    }
}
