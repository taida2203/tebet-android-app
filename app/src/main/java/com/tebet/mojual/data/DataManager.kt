package com.tebet.mojual.data

import co.sdk.auth.core.models.AuthJson
import com.tebet.mojual.data.local.db.DbHelper
import com.tebet.mojual.data.local.prefs.PreferencesHelper
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.remote.ApiHelper
import io.reactivex.Observable

interface DataManager: ApiHelper, DbHelper, PreferencesHelper {
    fun getUserProfileDB() : Observable<AuthJson<UserProfile>>
}
