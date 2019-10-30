package com.tebet.mojual.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tebet.mojual.data.models.Bank
import io.reactivex.Single

@Dao
interface BankDao {
    @Query("SELECT * FROM bank")
    fun queryBank(): Single<List<Bank>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBank(bank: Bank)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(banks: List<Bank>)

    @Query("DELETE FROM bank")
    fun nukeTable()
}
