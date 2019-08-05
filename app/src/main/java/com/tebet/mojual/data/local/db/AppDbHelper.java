/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.tebet.mojual.data.local.db;

import com.tebet.mojual.data.local.db.dao.BankDao;
import com.tebet.mojual.data.local.db.dao.CityDao;
import com.tebet.mojual.data.local.db.dao.UserProfileDao;
import com.tebet.mojual.data.models.Bank;
import com.tebet.mojual.data.models.City;
import com.tebet.mojual.data.models.UserProfile;
import io.reactivex.Observable;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.concurrent.Callable;


@Singleton
public class AppDbHelper implements DbHelper {

    private final AppDatabase mAppDatabase;

    @Inject
    public AppDbHelper(AppDatabase appDatabase) {
        this.mAppDatabase = appDatabase;
    }

    @Override
    public Observable<UserProfileDao> getUserProfile() {
        return Observable.fromCallable(new Callable<UserProfileDao>() {
            @Override
            public UserProfileDao call() throws Exception {
                return mAppDatabase.userProfileDao();
            }
        });
    }

    @Override
    public Observable<Boolean> insertUserProfile(UserProfile userProfile) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mAppDatabase.userProfileDao().insertUserProfile(userProfile);
                return true;
            }
        });
    }

    @Override
    public Observable<BankDao> getBank() {
        return Observable.fromCallable(new Callable<BankDao>() {
            @Override
            public BankDao call() throws Exception {
                return mAppDatabase.bankDao();
            }
        });
    }

    @Override
    public Observable<Boolean> insertBank(Bank bank) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mAppDatabase.bankDao().insertBank(bank);
                return true;
            }
        });
    }

    @Override
    public Observable<Boolean> insertBanks(@Nullable List<Bank> banks) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mAppDatabase.bankDao().insertAll(banks);
                return true;
            }
        });
    }

    @Override
    public Observable<CityDao> getCity() {
        return Observable.fromCallable(new Callable<CityDao>() {
            @Override
            public CityDao call() throws Exception {
                return mAppDatabase.cityDao();
            }
        });
    }

    @Override
    public Observable<Boolean> insertCity(City city) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mAppDatabase.cityDao().insertCity(city);
                return true;
            }
        });
    }
}
