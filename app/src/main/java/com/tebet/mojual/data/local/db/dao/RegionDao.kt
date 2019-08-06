package com.tebet.mojual.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tebet.mojual.data.models.Region
import io.reactivex.Single

@Dao
interface RegionDao {
    @Query("SELECT * FROM region")
    fun queryRegion(): Single<List<Region>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRegion(region: Region)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(regions: List<Region>)
}
