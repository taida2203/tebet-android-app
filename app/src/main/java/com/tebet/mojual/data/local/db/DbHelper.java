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

import java.util.List;

public interface DbHelper {
    Observable<UserProfileDao> getUserProfile();

    Observable<Boolean> insertUserProfile(final UserProfile userProfile);

    Observable<BankDao> getBank();

    Observable<Boolean> insertBank(final Bank bank);

    Observable<Boolean> insertBanks(@Nullable List<Bank> banks);

    Observable<CityDao> getCity();

    Observable<Boolean> insertCity(final City city);
}
