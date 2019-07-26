package com.tebet.mojual.data.repository

import co.sdk.auth.core.LoginConfiguration
import co.sdk.auth.core.models.AuthJson
import com.tebet.mojual.data.models.EmptyResponse
import com.tebet.mojual.data.models.UpdateProfileRequest
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.remote.ApiInterface
import com.tebet.mojual.persistance.dao.UserProfileDao
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by Ege Kuzubasioglu on 10.06.2018 at 00:37.
 * Copyright (c) 2018. All rights reserved.
 */
class ProfileRepository @Inject constructor(
    private val apiInterface: ApiInterface,
    private val userProfileDao: UserProfileDao
//    , private val utils: Utils
) {

    fun getProfile(): Observable<AuthJson<UserProfile>> {
//        val hasConnection = utils.isConnectedToInternet()
        val hasConnection = true
        var observableFromApi: Observable<AuthJson<UserProfile>> = getProfileFromApi()
//        if (hasConnection) {
//            observableFromApi = getProfileFromApi()
//        }
        val observableFromDb = getProfileFromDB()

        return observableFromApi
//        if (hasConnection) Observable.concatArrayEager(observableFromApi, observableFromDb)
//        else observableFromDb
    }

    private fun getProfileFromApi(): Observable<AuthJson<UserProfile>> {
        return apiInterface.getProfile()
            .doOnNext {
                it.data?.let { it1 -> userProfileDao.insertUserProfile(it1) }
            }
    }

    private fun getProfileFromDB(): Observable<UserProfile> {
        return userProfileDao.queryUserProfile()
            .toObservable()
            .doOnNext {
            }
    }

    fun registerApi(loginConfiguration: LoginConfiguration): Observable<AuthJson<EmptyResponse>> {
        return apiInterface.register(loginConfiguration)
    }

    fun forgotPasswordApi(updateProfileRequest: UpdateProfileRequest): Observable<AuthJson<EmptyResponse>> {
        return apiInterface.updateProfile(updateProfileRequest)
    }
}