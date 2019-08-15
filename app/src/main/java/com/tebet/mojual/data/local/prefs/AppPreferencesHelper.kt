package com.tebet.mojual.data.local.prefs

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.di.PreferenceInfo

import javax.inject.Inject


class AppPreferencesHelper @Inject
constructor(context: Context, @PreferenceInfo prefFileName: String) : PreferencesHelper {
    private val mPrefs: SharedPreferences = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN"
        private const val PREF_KEY_IS_MAP_TUTORIAL_SHOWED = "PREF_KEY_IS_MAP_TUTORIAL_SHOWED"
        private const val PREF_KEY_USER_PROFILE = "PREF_KEY_USER_PROFILE"
    }

    override var accessToken: String?
        get() = mPrefs.getString(PREF_KEY_ACCESS_TOKEN, null)
        set(value) = mPrefs.edit().putString(PREF_KEY_ACCESS_TOKEN, value).apply()

    override var isShowTutorialShowed: Boolean
        get() = mPrefs.getBoolean(PREF_KEY_IS_MAP_TUTORIAL_SHOWED, false)
        set(value) = mPrefs.edit().putBoolean(PREF_KEY_IS_MAP_TUTORIAL_SHOWED, value).apply()

    override var userProfilePref: UserProfile
        get() = Gson().fromJson(mPrefs.getString("PREF_KEY_USER_PROFILE", ""), UserProfile::class.java)
        set(value) = mPrefs.edit().putString("PREF_KEY_USER_PROFILE", Gson().toJson(value)).apply()
}
