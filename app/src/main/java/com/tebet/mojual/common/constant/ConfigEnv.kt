package com.tebet.mojual.common.constant

import com.tebet.mojual.BuildConfig


/**
 * Created by Tai Dao
 */

object ConfigEnv {

    enum class Environment { DEV, STAGING, PRODUCTION, STABLE, HOTFIX
    }

    lateinit var environment: Environment
        private set

    lateinit var apiRoot: String
        private set

    var isAnalyticEnabled = false
        private set
    lateinit var agoraAppId: String
        private set
    lateinit var consumerKey: String
        private set
    lateinit var consumerSecret: String
        private set

    val isProductionEnv: Boolean
        get() = environment == ConfigEnv.Environment.PRODUCTION

    val needCrashLogging: Boolean
        get() = environment != ConfigEnv.Environment.DEV && !BuildConfig.DEBUG

    init {
        when (BuildConfig.ENV_NAME) {
            "dev" -> {
                environment = ConfigEnv.Environment.DEV

//                apiRoot = "https://dev.api.mo-jual.com"
                apiRoot = "http://10.0.2.2:4000"
                isAnalyticEnabled = true
                agoraAppId = "xxx"
                consumerKey = "android_student_cakap"
                consumerSecret = "e10adc3949ba59abbe56e057f20f883e"
            }
            "staging" -> {
                environment = ConfigEnv.Environment.STAGING

                apiRoot = "https://dev.api.mo-jual.com"
                isAnalyticEnabled = true
                agoraAppId = "yyy"
                consumerKey = "android_student_cakap"
                consumerSecret = "e10adc3949ba59abbe56e057f20f883e"
            }
            "hotfix" -> {
                environment = ConfigEnv.Environment.HOTFIX

                apiRoot = "https://dev.api.mo-jual.com"
                isAnalyticEnabled = true
                agoraAppId = "yyy"
                consumerKey = "android_student_cakap"
                consumerSecret = "e10adc3949ba59abbe56e057f20f883e"
            }
            "production" -> {
                environment = ConfigEnv.Environment.PRODUCTION

                apiRoot = "https://dev.api.mo-jual.com"
                isAnalyticEnabled = true
                agoraAppId = "zzz"
                consumerKey = "android_student_cakap"
                consumerSecret = "e10adc3949ba59abbe56e057f20f883e"
            }
        }
    }
}
