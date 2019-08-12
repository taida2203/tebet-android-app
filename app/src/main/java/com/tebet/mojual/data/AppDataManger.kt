package com.tebet.mojual.data

import androidx.room.EmptyResultSetException
import co.sdk.auth.core.LoginConfiguration
import co.sdk.auth.core.models.AuthJson
import com.tebet.mojual.App
import com.tebet.mojual.common.util.checkConnectivity
import com.tebet.mojual.data.local.db.DbHelper
import com.tebet.mojual.data.local.db.dao.*
import com.tebet.mojual.data.local.prefs.PreferencesHelper
import com.tebet.mojual.data.models.*
import com.tebet.mojual.data.models.google_map.GeoCodeResponse
import com.tebet.mojual.data.remote.ApiGoogleHelper
import com.tebet.mojual.data.remote.ApiHelper
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
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
    override fun getAssetDB(): Observable<AuthJson<List<Asset>>> {
        return room.asset.concatMap { bankDao ->
            bankDao.queryAsset().subscribeOn(Schedulers.newThread())
                .doOnError { message -> EmptyResultSetException("") }
                .toObservable().map { assets -> AuthJson(null, "", assets) }
        }
    }

    override val asset: Observable<AssetDao>
        get() = room.asset

    override fun insertAssets(asset: List<Asset>): Observable<Boolean> {
        return room.insertAssets(asset)
    }

    override fun getAsserts(profileId: String, offset: Int?, limit: Int?): Observable<AuthJson<List<Asset>>> {
        if (App.instance.checkConnectivity()) {
            var assetsTemp: AuthJson<List<Asset>>? = null
            return api.getAsserts(profileId, offset, limit).concatMap { assetResponse ->
                assetsTemp = assetResponse
                assetResponse.data?.let { room.insertAssets(it) }
            }.concatMap {
                Observable.just(assetsTemp)
            }
        }
        return getAssetDB()
    }

    override fun isShowTutorialShowed(isShowTutorialShowed: Boolean?) {
        return preferences.isShowTutorialShowed(isShowTutorialShowed)
    }

    override fun isShowTutorialShowed(): Boolean {
        return preferences.isShowTutorialShowed
    }

    override fun clearAllTables(): Observable<Boolean> {
        return room.clearAllTables()
    }

    override fun getRegionDB(): Observable<AuthJson<List<Region>>> {
        return room.region.concatMap { bankDao ->
            bankDao.queryRegion().subscribeOn(Schedulers.newThread())
                .doOnError { message -> EmptyResultSetException("") }
                .toObservable().map { regions -> AuthJson(null, "", regions) }
        }
    }

    override val region: Observable<RegionDao>
        get() = room.region

    override fun getRegions(): Observable<AuthJson<List<Region>>> {
        if (App.instance.checkConnectivity()) {
            var regionsTemp: AuthJson<List<Region>>? = null
            return api.getRegions().concatMap { regionResponse ->
                regionsTemp = regionResponse
                regionResponse.data?.let { room.insertRegions(it) }
            }.concatMap {
                Observable.just(regionsTemp)
            }
        }
        return getRegionDB()
    }

    override fun insertRegions(regions: List<Region>): Observable<Boolean> {
        return room.insertRegions(regions)
    }

    override fun insertCities(cities: List<City>): Observable<Boolean> {
        return room.insertCities(cities)
    }

    override fun getReserveGeoLocation(latlng: String, key: String): Observable<GeoCodeResponse> {
        return apiGoogle.getReserveGeoLocation(latlng, key)
    }

    override fun getCityDB(): Observable<AuthJson<List<City>>> {
        return room.city.concatMap { cityDao ->
            cityDao.queryCity().subscribeOn(Schedulers.newThread()).doOnError { message -> EmptyResultSetException("") }
                .toObservable().map { citys -> AuthJson(null, "", citys) }
        }
    }

    override fun getBankDB(): Observable<AuthJson<List<Bank>>> {
        return room.bank.concatMap { bankDao ->
            bankDao.queryBank().subscribeOn(Schedulers.newThread()).doOnError { message -> EmptyResultSetException("") }
                .toObservable().map { banks -> AuthJson(null, "", banks) }
        }
    }

    override val bank: Observable<BankDao>
        get() = room.bank

    override fun insertBank(bank: Bank): Observable<Boolean> {
        return room.insertBank(bank)
    }

    override fun insertBanks(banks: List<Bank>): Observable<Boolean> {
        return room.insertBanks(banks)
    }

    override val city: Observable<CityDao>
        get() = room.city

    override fun insertCity(city: City): Observable<Boolean> {
        return room.insertCity(city)
    }

    override fun getBanks(): Observable<AuthJson<List<Bank>>> {
        if (App.instance.checkConnectivity()) {
            var banksTemp: AuthJson<List<Bank>>? = null
            return api.getBanks().concatMap { bankResponse ->
                banksTemp = bankResponse
                bankResponse.data?.let { room.insertBanks(it) }
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
                cityResponse.data?.let { room.insertCities(it) }
            }.concatMap {
                Observable.just(citiesTemp)
            }
        }
        return getCityDB()
    }

    override fun insertUserProfile(userProfile: UserProfile): Observable<Boolean> {
        return room.insertUserProfile(userProfile)
    }

    override val userProfile: Observable<UserProfileDao>
        get() = room.userProfile

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
                userProfile.data?.let { room.insertUserProfile(it) }
            }.concatMap {
                Observable.just(userProfileTemp)
            }
        }
        return getUserProfileDB()
    }

    override fun updateProfile(updateProfileRequest: UserProfile): Observable<AuthJson<UserProfile>> {
        var userProfileTemp: AuthJson<UserProfile>? = null
        return api.updateProfile(updateProfileRequest)
            .concatMap { userProfile ->
                userProfileTemp = userProfile
                userProfile.data?.let { room.insertUserProfile(it) }
            }
            .concatMap { Observable.just(userProfileTemp) }
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
            userProfileDao.queryUserProfile().subscribeOn(Schedulers.newThread())
                .doOnError { message -> EmptyResultSetException("") }
                .toObservable().map { userProfile -> AuthJson(null, "", userProfile) }
        }
    }

    override fun createOrder(createOrderRequest: CreateOrderRequest): Observable<AuthJson<EmptyResponse>> {
        return api.createOrder(createOrderRequest)
    }

}

