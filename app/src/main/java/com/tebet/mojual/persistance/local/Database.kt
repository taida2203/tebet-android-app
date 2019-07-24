package com.tebet.mojual.persistance.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.persistance.dao.UserProfileDao

/**
 * Created by Ege Kuzubasioglu on 9.06.2018 at 23:59.
 * Copyright (c) 2018. All rights reserved.
 */
@Database(entities = [(UserProfile::class)], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
}