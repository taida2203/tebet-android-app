package com.tebet.mojual.data.local.db.dao

import androidx.room.*
import com.tebet.mojual.data.models.Bank
import com.tebet.mojual.data.models.UserProfile
import io.reactivex.Single

@Dao
interface BankDao {
    @Query("SELECT * FROM bank")
    fun queryBank(): Single<Bank>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBank(bank: Bank)
}
