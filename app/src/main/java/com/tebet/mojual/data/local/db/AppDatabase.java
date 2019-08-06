package com.tebet.mojual.data.local.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.tebet.mojual.data.local.db.dao.BankDao;
import com.tebet.mojual.data.local.db.dao.CityDao;
import com.tebet.mojual.data.local.db.dao.RegionDao;
import com.tebet.mojual.data.local.db.dao.UserProfileDao;
import com.tebet.mojual.data.models.Bank;
import com.tebet.mojual.data.models.City;
import com.tebet.mojual.data.models.Region;
import com.tebet.mojual.data.models.UserProfile;

@Database(entities = {UserProfile.class, City.class, Bank.class, Region.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserProfileDao userProfileDao();

    public abstract BankDao bankDao();

    public abstract CityDao cityDao();

    public abstract RegionDao regionDao();
}
