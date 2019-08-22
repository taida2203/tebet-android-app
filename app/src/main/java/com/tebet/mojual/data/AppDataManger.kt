package com.tebet.mojual.data

import androidx.room.EmptyResultSetException
import co.sdk.auth.core.models.AuthJson
import co.sdk.auth.core.models.LoginConfiguration
import com.tebet.mojual.App
import com.tebet.mojual.common.util.checkConnectivity
import com.tebet.mojual.data.local.db.DbHelper
import com.tebet.mojual.data.local.db.dao.*
import com.tebet.mojual.data.local.prefs.PreferencesHelper
import com.tebet.mojual.data.models.*
import com.tebet.mojual.data.models.google_map.GeoCodeResponse
import com.tebet.mojual.data.models.request.CreateOrderRequest
import com.tebet.mojual.data.models.request.SearchOrderRequest
import com.tebet.mojual.data.models.request.UpdatePasswordRequest
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
    override var userProfilePref: UserProfile
        get() = preferences.userProfilePref
        set(value) {
            preferences.userProfilePref = value
        }
    override var isShowTutorialShowed: Boolean
        get() = preferences.isShowTutorialShowed
        set(value) {
            preferences.isShowTutorialShowed = value
        }
    override var accessToken: String?
        get() = preferences.accessToken
        set(value) {
            preferences.accessToken = value
        }

    override fun searchOrders(searchOrderRequest: SearchOrderRequest): Observable<AuthJson<Paging<Order>>> {
        return api.searchOrders(searchOrderRequest)
    }

    override fun getOrderDetail(orderId: Int, loadCustomer: Boolean, loadContainers: Boolean): Observable<AuthJson<Order>> = api.getOrderDetail(orderId)

    override fun getNext7DaysPrice(): Observable<AuthJson<List<Price>>> = api.getNext7DaysPrice()

    override fun getAssetDB(): Observable<AuthJson<List<Asset>>> = room.asset.concatMap { bankDao ->
        bankDao.queryAsset().subscribeOn(Schedulers.newThread())
            .doOnError { message -> EmptyResultSetException("") }
            .toObservable().map { assets -> AuthJson(null, "", assets) }
    }

    override val asset: Observable<AssetDao>
        get() = room.asset

    override fun insertAssets(asset: List<Asset>): Observable<Boolean> = room.insertAssets(asset)

    override fun getAsserts(profileId: String, offset: Int?, limit: Int?): Observable<AuthJson<List<Asset>>> {
        if (App.instance.checkConnectivity()) {
            var assetsTemp: AuthJson<List<Asset>>? = null
            return api.getAsserts(profileId, offset, limit).concatMap { assetResponse ->
                assetsTemp = assetResponse
                room.clearAllAssets()
            }.concatMap {
                assetsTemp?.data?.let { room.insertAssets(it) } ?: Observable.just(assetsTemp)
            }.concatMap {
                Observable.just(assetsTemp)
            }
        }
        return getAssetDB()
    }


    override fun clearAllTables(): Observable<Boolean> = room.clearAllTables()

    override fun getRegionDB(): Observable<AuthJson<List<Region>>> = room.region.concatMap { bankDao ->
        bankDao.queryRegion().subscribeOn(Schedulers.newThread())
            .doOnError { message -> EmptyResultSetException("") }
            .toObservable().map { regions -> AuthJson(null, "", regions) }
    }

    override val region: Observable<RegionDao>
        get() = room.region

    override fun getRegions(): Observable<AuthJson<List<Region>>> {
        if (App.instance.checkConnectivity()) return api.getRegions().concatMap { regionResponse ->
            clearAllRegions().concatMap { regionResponse.data?.let { it1 -> room.insertRegions(it1) } }.concatMap { Observable.just(regionResponse) }
        }
        return getRegionDB()
    }

    override fun insertRegions(regions: List<Region>): Observable<Boolean> = room.insertRegions(regions)

    override fun insertCities(cities: List<City>): Observable<Boolean> = room.insertCities(cities)

    override fun getReserveGeoLocation(latlng: String, key: String): Observable<GeoCodeResponse> = apiGoogle.getReserveGeoLocation(latlng, key)

    override fun getCityDB(): Observable<AuthJson<List<City>>> = room.city.concatMap { cityDao ->
        cityDao.queryCity().subscribeOn(Schedulers.newThread()).doOnError { message -> EmptyResultSetException("") }
            .toObservable().map { citys -> AuthJson(null, "", citys) }
    }

    override fun getBankDB(): Observable<AuthJson<List<Bank>>> = room.bank.concatMap { bankDao ->
        bankDao.queryBank().subscribeOn(Schedulers.newThread()).doOnError { message -> EmptyResultSetException("") }
            .toObservable().map { banks -> AuthJson(null, "", banks) }
    }

    override val bank: Observable<BankDao>
        get() = room.bank

    override fun insertBank(bank: Bank): Observable<Boolean> = room.insertBank(bank)

    override fun insertBanks(banks: List<Bank>): Observable<Boolean> = room.insertBanks(banks)

    override val city: Observable<CityDao>
        get() = room.city

    override fun insertCity(city: City): Observable<Boolean> = room.insertCity(city)

    override fun getBanks(): Observable<AuthJson<List<Bank>>> {
        if (App.instance.checkConnectivity()) return api.getBanks().concatMap { response ->
            clearAllBanks().concatMap { response.data?.let { it1 -> room.insertBanks(it1) } }.concatMap { Observable.just(response) }
        }
        return getBankDB()
    }

    override fun getCities(): Observable<AuthJson<List<City>>> {
        if (App.instance.checkConnectivity()) return api.getCities().concatMap { response ->
            clearAllCity().concatMap { response.data?.let { it1 -> room.insertCities(it1) } }.concatMap { Observable.just(response) }
        }
        return getCityDB()
    }

    override fun insertUserProfile(userProfile: UserProfile): Observable<Boolean> = room.insertUserProfile(userProfile)

    override val userProfile: Observable<UserProfileDao>
        get() = room.userProfile

    override fun getDataMock(): Call<ResponseBody> = api.getDataMock()

    override fun getData(): Call<ResponseBody> = api.getData()

    override fun register(loginConfiguration: LoginConfiguration): Observable<AuthJson<EmptyResponse>> = api.register(loginConfiguration)

    override fun getProfile(): Observable<AuthJson<UserProfile>> {
        if (App.instance.checkConnectivity()) {
            return api.getProfile().doOnError { getUserProfileDB() }.concatMap { updateProfileDB(it) }
        }
        return getUserProfileDB()
    }

    override fun updateProfile(updateProfileRequest: UserProfile): Observable<AuthJson<UserProfile>> {
        return api.updateProfile(updateProfileRequest).concatMap { updateProfileDB(it) }
    }

    private fun updateProfileDB(userProfile: AuthJson<UserProfile>?): Observable<AuthJson<UserProfile>?> {
        return clearAllProfiles()
            .concatMap { userProfile?.data?.let { it1 -> insertUserProfile(it1) } }
            .concatMap { Observable.just(userProfile) }
            .subscribeOn(Schedulers.newThread())
    }

    override fun getUserProfileDB(): Observable<AuthJson<UserProfile>> = room.userProfile.concatMap { userProfileDao ->
        userProfileDao.queryUserProfile().subscribeOn(Schedulers.newThread())
            .doOnError { message -> EmptyResultSetException("") }
            .toObservable().map { userProfile -> AuthJson(null, "", userProfile) }
    }

    override fun getContainerCheckDB(): Observable<List<Quality>> = room.quality.concatMap { containerDao ->
        containerDao.queryContainerChecks().subscribeOn(Schedulers.newThread())
            .doOnError { message -> EmptyResultSetException("") }
            .toObservable()
    }

    override val quality: Observable<QualityDao> get() = room.quality

    override fun insertContainerChecks(qualities: List<Quality>): Observable<Boolean> = room.insertContainerChecks(qualities)

    override fun insertContainerCheck(quality: Quality): Observable<Boolean> = room.insertContainerCheck(quality)

    override fun clearAllContainerCheck(): Observable<Boolean> = room.clearAllContainerCheck()

    override fun clearAllProfiles(): Observable<Boolean> = room.clearAllProfiles()

    override fun clearAllBanks(): Observable<Boolean> = room.clearAllBanks()

    override fun clearAllRegions(): Observable<Boolean> = room.clearAllRegions()

    override fun clearAllCity(): Observable<Boolean> = room.clearAllCity()

    override fun clearAllAssets(): Observable<Boolean> = room.clearAllAssets()

    override fun updatePassword(updateProfileRequest: UpdatePasswordRequest): Observable<AuthJson<EmptyResponse>> = api.updatePassword(updateProfileRequest)

    override fun uploadImage(
        folder: MultipartBody.Part,
        file: MultipartBody.Part
    ): Observable<AuthJson<String>> {
        return api.uploadImage(folder, file)
    }

    override fun createOrder(createOrderRequest: CreateOrderRequest): Observable<AuthJson<Order>> = api.createOrder(createOrderRequest)

    override fun deleteContainerCheck(quality: Quality): Observable<Boolean> = room.deleteContainerCheck(quality)
}

