package com.tebet.mojual.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tebet.mojual.data.models.City
import io.reactivex.Single

@Dao
interface CityDao {
    @Query("SELECT * FROM city")
    fun queryCity(): Single<City>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCity(bank: City)
}
