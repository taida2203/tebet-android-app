package com.tebet.mojual.di.module

import android.app.Application
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
        database.execSQL("ALTER TABLE cryptocurrency RENAME TO cryptoCurrencies")
      }
    }
  }

  @Provides
  @Singleton
  fun provideApplication(): Application = app

//  @Provides
//  @Singleton
//  fun provideCryptocurrenciesDatabase(app: Application): Database = Room.databaseBuilder(app,
//      Database::class.java, Constants.DATABASE_NAME)
//      /*.addMigrations(MIGRATION_1_2)*/
//      .fallbackToDestructiveMigration()
//      .build()
//
//  @Provides
//  @Singleton
//  fun provideCryptocurrenciesDao(
//      database: Database): CryptoCurrencyDao = database.userProfileDao()
//
//  @Provides
//  @Singleton
//  fun provideCryptocurrenciesViewModelFactory(
//      factory: CryptoCurrencyViewModelFactory): ViewModelProvider.Factory = factory
//
//  @Provides
//  @Singleton
//  fun provideUtils(): Utils = Utils(app)
}
