package com.tebet.mojual.data.local.prefs

import com.tebet.mojual.data.models.UserProfile

interface PreferencesHelper {
    var accessToken: String?
    var isShowTutorialShowed: Boolean
    var userProfilePref: UserProfile
    var notificationCountPref: Long?
}
