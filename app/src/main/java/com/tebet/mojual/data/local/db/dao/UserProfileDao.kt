package com.tebet.mojual.data.local.db.dao

import androidx.room.*
import com.tebet.mojual.data.models.UserProfile
import io.reactivex.Single

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM UserProfile")
    fun queryUserProfile(): Single<UserProfile>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserProfile(userProfile: UserProfile)
}
