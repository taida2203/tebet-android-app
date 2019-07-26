package com.tebet.mojual.data

import co.sdk.auth.core.LoginConfiguration
import co.sdk.auth.core.models.AuthJson
import com.tebet.mojual.data.models.EmptyResponse
import com.tebet.mojual.data.models.UpdateProfileRequest
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.remote.ApiInterface
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import javax.inject.Inject

class AppDataManger @Inject constructor(private var api: ApiInterface) : DataManager {

    override fun getDataMock(): Call<ResponseBody> {
        return api.getDataMock()
    }

    override fun getData(): Call<ResponseBody> {
        return api.getData()
    }

    override fun register(loginConfiguration: LoginConfiguration): Observable<AuthJson<EmptyResponse>> {
        return api.register(loginConfiguration)
    }

    override fun getProfile(): Observable<AuthJson<UserProfile>> {
        return api.getProfile()
    }

    override fun updateProfile(updateProfileRequest: UpdateProfileRequest): Observable<AuthJson<EmptyResponse>> {
        return api.updateProfile(updateProfileRequest)
    }
}

