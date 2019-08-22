package com.tebet.mojual.data.local.db.dao

import androidx.room.*
import com.tebet.mojual.data.models.Quality
import io.reactivex.Single

@Dao
interface QualityDao {
    @Query("SELECT * FROM quality")
    fun queryContainerChecks(): Single<List<Quality>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContainerCheck(quality: Quality)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(qualities: List<Quality>)

    @Query("DELETE FROM quality")
    fun nukeTable()

    @Delete
    fun delete(quality: Quality)
}
