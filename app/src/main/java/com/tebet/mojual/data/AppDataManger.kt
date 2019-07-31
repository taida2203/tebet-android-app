package com.tebet.mojual.data

import co.sdk.auth.core.LoginConfiguration
import co.sdk.auth.core.models.AuthJson
import com.tebet.mojual.data.local.db.DbHelper
import com.tebet.mojual.data.local.db.dao.UserProfileDao
import com.tebet.mojual.data.local.prefs.PreferencesHelper
import com.tebet.mojual.data.models.EmptyResponse
import com.tebet.mojual.data.models.UpdatePasswordRequest
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.remote.ApiHelper
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import javax.inject.Inject

class AppDataManger @Inject constructor(private var api: ApiHelper, private var room: DbHelper, private var preferences: PreferencesHelper) : DataManager {
    override fun insertUserProfile(userProfile: UserProfile?): Observable<Boolean> {
        return room.insertUserProfile(userProfile)
    }

    override fun getUserProfile(): Observable<UserProfileDao> {
        return room.userProfile
    }

    override fun getAccessToken(): String {
        return preferences.accessToken
    }

    override fun setAccessToken(accessToken: String?) {
        preferences.accessToken = accessToken
    }

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
        return api.getProfile().doOnNext { userProfile ->
            room.insertUserProfile(userProfile.data)
        }
    }

    override fun updateProfile(updateProfileRequest: UserProfile): Observable<AuthJson<EmptyResponse>> {
        return api.updateProfile(updateProfileRequest)
    }

    override fun updatePassword(updateProfileRequest: UpdatePasswordRequest): Observable<AuthJson<EmptyResponse>> {
        return api.updatePassword(updateProfileRequest)
    }

    override fun uploadImage(
        folder: MultipartBody.Part,
        file: MultipartBody.Part
    ): Observable<AuthJson<String>> {
        return api.uploadImage(folder, file)
    }
}

