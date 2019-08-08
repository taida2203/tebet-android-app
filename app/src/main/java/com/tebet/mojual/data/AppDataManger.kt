package com.tebet.mojual.data

import androidx.room.EmptyResultSetException
import co.sdk.auth.core.LoginConfiguration
import co.sdk.auth.core.models.AuthJson
import com.tebet.mojual.App
import com.tebet.mojual.common.util.checkConnectivity
import com.tebet.mojual.data.local.db.DbHelper
import com.tebet.mojual.data.local.db.dao.BankDao
import com.tebet.mojual.data.local.db.dao.CityDao
import com.tebet.mojual.data.local.db.dao.RegionDao
import com.tebet.mojual.data.local.db.dao.UserProfileDao
import com.tebet.mojual.data.local.prefs.PreferencesHelper
import com.tebet.mojual.data.models.*
import com.tebet.mojual.data.models.google_map.GeoCodeResponse
import com.tebet.mojual.data.remote.ApiGoogleHelper
import com.tebet.mojual.data.remote.ApiHelper
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import javax.inject.Inject

class AppDataManger @Inject constructor(
    private var api: ApiHelper,
    private var apiGoogle: ApiGoogleHelper,
    private var room: DbHelper,
    private var preferences: PreferencesHelper
) : DataManager {
    override fun isShowTutorialShowed(isShowTutorialShowed: Boolean?) {
        return preferences.isShowTutorialShowed(isShowTutorialShowed)
    }

    override fun isShowTutorialShowed(): Boolean {
        return preferences.isShowTutorialShowed
    }

    override fun clearAllTables(): Observable<Boolean>? {
        return room.clearAllTables()
    }

    override fun getRegionDB(): Observable<AuthJson<List<Region>>> {
        return room.region.concatMap { bankDao ->
            bankDao.queryRegion().doOnError { message -> EmptyResultSetException("") }
                .toObservable().map { regions -> AuthJson(null, "", regions) }
        }
    }

    override fun getRegion(): Observable<RegionDao> {
        return room.region
    }

    override fun getRegions(): Observable<AuthJson<List<Region>>> {
        if (App.instance.checkConnectivity()) {
            var regionsTemp: AuthJson<List<Region>>? = null
            return api.getRegions().concatMap { regionResponse ->
                regionsTemp = regionResponse
                room.insertRegions(regionResponse.data)
            }.concatMap {
                Observable.just(regionsTemp)
            }
        }
        return getRegionDB()
    }

    override fun insertRegions(regions: MutableList<Region>?): Observable<Boolean> {
        return room.insertRegions(regions)
    }

    override fun insertCities(cities: MutableList<City>?): Observable<Boolean> {
        return room.insertCities(cities)
    }

    override fun getReserveGeoLocation(latlng: String, key: String): Observable<GeoCodeResponse> {
        return apiGoogle.getReserveGeoLocation(latlng, key)
    }

    override fun getCityDB(): Observable<AuthJson<List<City>>> {
        return room.city.concatMap { cityDao ->
            cityDao.queryCity().doOnError { message -> EmptyResultSetException("") }
                .toObservable().map { citys -> AuthJson(null, "", citys) }
        }
    }

    override fun getBankDB(): Observable<AuthJson<List<Bank>>> {
        return room.bank.concatMap { bankDao ->
            bankDao.queryBank().doOnError { message -> EmptyResultSetException("") }
                .toObservable().map { banks -> AuthJson(null, "", banks) }
        }
    }

    override fun getBank(): Observable<BankDao> {
        return room.bank
    }

    override fun insertBank(bank: Bank?): Observable<Boolean> {
        return room.insertBank(bank)
    }

    override fun insertBanks(banks: MutableList<Bank>?): Observable<Boolean> {
        return room.insertBanks(banks)
    }

    override fun getCity(): Observable<CityDao> {
        return room.city
    }

    override fun insertCity(city: City?): Observable<Boolean> {
        return room.insertCity(city)
    }

    override fun getBanks(): Observable<AuthJson<List<Bank>>> {
        if (App.instance.checkConnectivity()) {
            var banksTemp: AuthJson<List<Bank>>? = null
            return api.getBanks().concatMap { bankResponse ->
                banksTemp = bankResponse
                room.insertBanks(bankResponse.data)
            }.concatMap {
                Observable.just(banksTemp)
            }
        }
        return getBankDB()
    }

    override fun getCities(): Observable<AuthJson<List<City>>> {
        if (App.instance.checkConnectivity()) {
            var citiesTemp: AuthJson<List<City>>? = null
            return api.getCities().concatMap { cityResponse ->
                citiesTemp = cityResponse
                room.insertCities(cityResponse.data)
            }.concatMap {
                Observable.just(citiesTemp)
            }
        }
        return getCityDB()
    }

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
        if (App.instance.checkConnectivity()) {
            var userProfileTemp: AuthJson<UserProfile>? = null
            return api.getProfile().concatMap { userProfile ->
                userProfileTemp = userProfile
                room.insertUserProfile(userProfile.data)
            }.concatMap {
                Observable.just(userProfileTemp)
            }
        }
        return getUserProfileDB()
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

    override fun getUserProfileDB(): Observable<AuthJson<UserProfile>> {
        return room.userProfile.concatMap { userProfileDao ->
            userProfileDao.queryUserProfile().doOnError { message -> EmptyResultSetException("") }
                .toObservable().map { userProfile -> AuthJson(null, "", userProfile) }
        }
    }

    override fun createOrder(createOrderRequest: CreateOrderRequest): Observable<AuthJson<EmptyResponse>> {
        return api.createOrder(createOrderRequest)
    }

}

