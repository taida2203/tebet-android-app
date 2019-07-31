package com.tebet.mojual.data.local.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.tebet.mojual.data.local.db.dao.UserProfileDao;
import com.tebet.mojual.data.models.UserProfile;

@Database(entities = {UserProfile.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserProfileDao userProfileDao();
}
