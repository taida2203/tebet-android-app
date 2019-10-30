package com.tebet.mojual.di.module

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.isupatches.wisefy.WiseFy
import com.tebet.mojual.R
import com.tebet.mojual.common.util.Sensor
import com.tebet.mojual.common.util.rx.AppSchedulerProvider
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.AppDataManger
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.local.db.AppDatabase
import com.tebet.mojual.data.local.db.AppDbHelper
import com.tebet.mojual.data.local.db.DbHelper
import com.tebet.mojual.data.local.prefs.AppPreferencesHelper
import com.tebet.mojual.data.local.prefs.PreferencesHelper
import com.tebet.mojual.di.DatabaseInfo
import com.tebet.mojual.di.PreferenceInfo
import dagger.Module
import dagger.Provides
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import javax.inject.Singleton

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
    internal fun provideAppDatabase(@DatabaseInfo dbName: String, context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, dbName).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @DatabaseInfo
    internal fun provideDatabaseName(): String {
        return "TEBET_DB"
    }

    @Provides
    @PreferenceInfo
    internal fun providePreferenceName(): String {
        return "TEBET_PREF"
    }

    @Provides
    @Singleton
    internal fun provideContext(application: Application): Context {
        return application
    }


    @Provides
    @Singleton
    internal fun provideDataManager(appDataManager: AppDataManger): DataManager {
        return appDataManager
    }


    @Provides
    internal fun provideSchedulerProvider(): SchedulerProvider {
        return AppSchedulerProvider()
    }

    @Provides
    @Singleton
    internal fun provideCalligraphyDefaultConfig(): CalligraphyConfig {
        return CalligraphyConfig.Builder()
            .setDefaultFontPath("fonts/montserrat/Montserrat-Regular.otf")
            .setFontAttrId(R.attr.fontPath)
            .build()
    }

    @Provides
    @Singleton
    internal fun provideDbHelper(appDbHelper: AppDbHelper): DbHelper {
        return appDbHelper
    }

    @Provides
    @Singleton
    internal fun providePreferencesHelper(appPreferencesHelper: AppPreferencesHelper): PreferencesHelper {
        return appPreferencesHelper
    }

    @Provides
    @Singleton
    internal fun provideSensorHelper(wifiManager: WiseFy, appContext: Context): Sensor {
        return Sensor(wifiManager, appContext)
    }

    @Provides
    @Singleton
    internal fun provideWifiHelper(appContext: Context): WiseFy {
        // useLegacyConnection = false, useLegacySearch = true
        return WiseFy.Brains(appContext, useLegacyConnection = false, useLegacySearch = true).logging(true).getSmarts()
    }

//  @Provides
//  @Singleton
//  fun provideUtils(): Utils = Utils(app)
}
