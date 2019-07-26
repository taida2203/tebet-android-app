package com.tebet.mojual.di.module

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tebet.mojual.data.AppDataManger
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.persistance.dao.UserProfileDao
import com.tebet.mojual.persistance.local.Database
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Ege Kuzubasioglu on 9.06.2018 at 21:05.
 * Copyright (c) 2018. All rights reserved.
 */

@Module
class AppModule(private val app: Application) {
    companion object {
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Change the table name to the correct one
                database.execSQL("ALTER TABLE UserProfile RENAME TO profile")
            }
        }
    }

    @Provides
    @Singleton
    fun provideApplication(): Application = app

    @Provides
    @Singleton
    fun provideUserProfileDatabase(app: Application): Database = Room.databaseBuilder(
        app,
        Database::class.java, "Tebet_DB"
    )
        /*.addMigrations(MIGRATION_1_2)*/
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideUserProfileDao(
        database: Database
    ): UserProfileDao = database.userProfileDao()

    @Provides
    @Singleton
    internal fun provideDataManager(appDataManager: AppDataManger): DataManager {
        return appDataManager
    }

//  @Provides
//  @Singleton
//  fun provideUtils(): Utils = Utils(app)
}
