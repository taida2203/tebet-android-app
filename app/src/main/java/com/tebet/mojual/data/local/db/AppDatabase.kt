package com.tebet.mojual.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tebet.mojual.data.local.db.dao.*
import com.tebet.mojual.data.models.*

@Database(entities = [UserProfile::class, City::class, Bank::class, Region::class, Asset::class], version = 5)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao

    abstract fun bankDao(): BankDao

    abstract fun cityDao(): CityDao

    abstract fun regionDao(): RegionDao

    abstract fun assetDao(): AssetDao
}
