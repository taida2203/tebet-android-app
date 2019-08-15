package com.tebet.mojual.data.local.db


import com.tebet.mojual.data.local.db.dao.*
import com.tebet.mojual.data.models.*
import io.reactivex.Observable

interface DbHelper {
    val userProfile: Observable<UserProfileDao>

    val bank: Observable<BankDao>

    val region: Observable<RegionDao>

    val city: Observable<CityDao>

    val asset: Observable<AssetDao>

    fun insertUserProfile(userProfile: UserProfile): Observable<Boolean>

    fun insertBank(bank: Bank): Observable<Boolean>

    fun insertBanks(banks: List<Bank>): Observable<Boolean>

    fun insertRegions(regions: List<Region>): Observable<Boolean>

    fun insertCity(city: City): Observable<Boolean>

    fun insertCities(cities: List<City>): Observable<Boolean>

    fun insertAssets(asset: List<Asset>): Observable<Boolean>

    fun clearAllTables(): Observable<Boolean>

    fun clearAllProfiles(): Observable<Boolean>
}
