package com.tebet.mojual.data.local.db


import com.tebet.mojual.data.local.db.dao.*
import com.tebet.mojual.data.models.*
import io.reactivex.Observable

interface DbHelper {
    val userProfile: Observable<UserProfileDao>
    fun insertUserProfile(userProfile: UserProfile): Observable<Boolean>
    fun clearAllProfiles(): Observable<Boolean>

    val bank: Observable<BankDao>
    fun insertBank(bank: Bank): Observable<Boolean>
    fun insertBanks(banks: List<Bank>): Observable<Boolean>
    fun clearAllBanks(): Observable<Boolean>

    val region: Observable<RegionDao>
    fun insertRegions(regions: List<Region>): Observable<Boolean>
    fun clearAllRegions(): Observable<Boolean>

    val city: Observable<CityDao>
    fun insertCity(city: City): Observable<Boolean>
    fun insertCities(cities: List<City>): Observable<Boolean>
    fun clearAllCity(): Observable<Boolean>

    val asset: Observable<AssetDao>
    fun insertAssets(asset: List<Asset>): Observable<Boolean>
    fun clearAllAssets(): Observable<Boolean>

    val quality: Observable<QualityDao>
    fun insertContainerCheck(quality: Quality): Observable<Boolean>
    fun insertContainerChecks(qualities: List<Quality>): Observable<Boolean>
    fun deleteContainerCheck(quality: Quality): Observable<Boolean>
    fun clearAllContainerCheck(): Observable<Boolean>

    fun clearAllTables(): Observable<Boolean>


}
