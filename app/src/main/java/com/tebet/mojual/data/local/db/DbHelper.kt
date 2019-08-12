/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

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
}
