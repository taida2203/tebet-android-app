package com.tebet.mojual.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tebet.mojual.data.models.Asset
import io.reactivex.Single

@Dao
interface AssetDao {
    @Query("SELECT * FROM asset")
    fun queryAsset(): Single<List<Asset>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAsset(asset: Asset)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(assets: List<Asset>)
}
