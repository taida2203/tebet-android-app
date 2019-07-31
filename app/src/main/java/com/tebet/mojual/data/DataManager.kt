package com.tebet.mojual.data

import com.tebet.mojual.data.local.db.DbHelper
import com.tebet.mojual.data.local.prefs.PreferencesHelper
import com.tebet.mojual.data.remote.ApiHelper

interface DataManager: ApiHelper, DbHelper, PreferencesHelper
