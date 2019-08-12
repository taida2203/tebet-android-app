package com.tebet.mojual.data

import co.sdk.auth.core.models.AuthJson
import com.tebet.mojual.data.local.db.DbHelper
import com.tebet.mojual.data.local.prefs.PreferencesHelper
import com.tebet.mojual.data.models.*
import com.tebet.mojual.data.remote.ApiGoogleHelper
import com.tebet.mojual.data.remote.ApiHelper
import io.reactivex.Observable

interface DataManager: ApiHelper, ApiGoogleHelper, DbHelper, PreferencesHelper {
    fun getUserProfileDB() : Observable<AuthJson<UserProfile>>
    fun getCityDB() : Observable<AuthJson<List<City>>>
    fun getBankDB() : Observable<AuthJson<List<Bank>>>
    fun getRegionDB() : Observable<AuthJson<List<Region>>>
    fun getAssetDB() : Observable<AuthJson<List<Asset>>>
}
