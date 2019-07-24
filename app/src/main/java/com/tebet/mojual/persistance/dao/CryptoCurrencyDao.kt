package com.tebet.mojual.persistance.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tebet.mojual.data.models.UserProfile
import io.reactivex.Single

/**
 * Created by Ege Kuzubasioglu on 9.06.2018 at 23:58.
 * Copyright (c) 2018. All rights reserved.
 */
@Dao
interface UserProfileDao {

    @Query("SELECT * FROM UserProfile ORDER BY userId limit :limit offset :offset")
    fun queryUserProfile(limit: Int, offset: Int): Single<UserProfile>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserProfile(cryptoCurrency: UserProfile)
}