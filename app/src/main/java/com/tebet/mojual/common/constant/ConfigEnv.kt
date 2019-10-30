package com.tebet.mojual.common.constant

import com.tebet.mojual.BuildConfig


/**
 * Created by Tai Dao
 */

object ConfigEnv {

    enum class Environment { DEV, LOCAL, PRODUCTION
    }

    lateinit var environment: Environment
        private set

    lateinit var apiRoot: String
        private set

    var isAnalyticEnabled = false
        private set
    lateinit var googleApiKey: String
        private set

    val isProductionEnv: Boolean
        get() = environment == Environment.PRODUCTION

    val needCrashLogging: Boolean
        get() = !BuildConfig.DEBUG // environment != ConfigEnv.Environment.DEV

    init {
        when (BuildConfig.ENV_NAME) {
            "dev" -> {
                environment = Environment.DEV

                apiRoot = "https://dev.api.mo-jual.com"
                isAnalyticEnabled = true
                googleApiKey = "AIzaSyDAZpY0LoxIVYsg3b1YQ5-cEJW8EgYfB98"
            }
            "local" -> {
                environment = Environment.LOCAL

                apiRoot = "http://192.168.1.50:4000"

                isAnalyticEnabled = true
                googleApiKey = "AIzaSyDAZpY0LoxIVYsg3b1YQ5-cEJW8EgYfB98"
            }
            "production" -> {
                environment = Environment.PRODUCTION

                apiRoot = "https://api.mo-jual.com"
                isAnalyticEnabled = true
                googleApiKey = "AIzaSyDAZpY0LoxIVYsg3b1YQ5-cEJW8EgYfB98"
            }
        }
    }
}
