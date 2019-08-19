package com.tebet.mojual.data.local.db

import com.tebet.mojual.data.local.db.dao.*
import com.tebet.mojual.data.models.*
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AppDbHelper @Inject
constructor(private val mAppDatabase: AppDatabase) : DbHelper {

    override val userProfile: Observable<UserProfileDao>
        get() = Observable.fromCallable { mAppDatabase.userProfileDao() }

    override val bank: Observable<BankDao>
        get() = Observable.fromCallable { mAppDatabase.bankDao() }

    override val region: Observable<RegionDao>
        get() = Observable.fromCallable { mAppDatabase.regionDao() }

    override val city: Observable<CityDao>
        get() = Observable.fromCallable { mAppDatabase.cityDao() }

    override val asset: Observable<AssetDao>
        get() = Observable.fromCallable { mAppDatabase.assetDao() }

    override fun insertUserProfile(userProfile: UserProfile): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.userProfileDao().insertUserProfile(userProfile)
            true
        }
    }

    override fun insertBank(bank: Bank): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.bankDao().insertBank(bank)
            true
        }
    }

    override fun insertBanks(banks: List<Bank>): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.bankDao().insertAll(banks)
            true
        }
    }

    override fun insertRegions(regions: List<Region>): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.regionDao().insertAll(regions)
            true
        }
    }

    override fun insertCity(city: City): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.cityDao().insertCity(city)
            true
        }
    }

    override fun insertCities(cities: List<City>): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.cityDao().insertAll(cities)
            true
        }
    }

    override fun clearAllTables(): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.clearAllTables()
            true
        }
    }

    override fun clearAllProfiles(): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.userProfileDao().nukeTable()
            true
        }
    }

    override fun insertAssets(asset: List<Asset>): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.assetDao().insertAll(asset)
            true
        }
    }

    override fun clearAllAssets(): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.assetDao().nukeTable()
            true
        }
    }

}
