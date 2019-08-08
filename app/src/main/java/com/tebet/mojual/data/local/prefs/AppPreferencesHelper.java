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

package com.tebet.mojual.data.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import com.tebet.mojual.di.PreferenceInfo;

import javax.inject.Inject;


public class AppPreferencesHelper implements PreferencesHelper {
    private static final String PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN";
    private static final String PREF_KEY_IS_MAP_TUTORIAL_SHOWNED = "PREF_KEY_IS_MAP_TUTORIAL_SHOWNED";
    private final SharedPreferences mPrefs;

    @Inject
    public AppPreferencesHelper(Context context, @PreferenceInfo String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }

    @Override
    public String getAccessToken() {
        return mPrefs.getString(PREF_KEY_ACCESS_TOKEN, null);
    }

    @Override
    public void setAccessToken(String accessToken) {
        mPrefs.edit().putString(PREF_KEY_ACCESS_TOKEN, accessToken).apply();
    }

    @Override
    public Boolean isShowTutorialShowed() {
        return mPrefs.getBoolean(PREF_KEY_IS_MAP_TUTORIAL_SHOWNED, false);
    }

    @Override
    public void isShowTutorialShowed(Boolean isShowTutorialShowed) {
        mPrefs.edit().putBoolean(PREF_KEY_IS_MAP_TUTORIAL_SHOWNED, isShowTutorialShowed).apply();
    }
}
