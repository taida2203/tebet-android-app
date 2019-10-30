package com.tebet.mojual

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.Context
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import co.common.util.LanguageUtil
import co.common.util.PreferenceUtils
import co.sdk.auth.AuthSdk
import com.crashlytics.android.Crashlytics
import com.facebook.stetho.Stetho
import com.google.firebase.messaging.RemoteMessage
import com.squareup.leakcanary.LeakCanary
import com.tebet.mojual.common.constant.ConfigEnv
import com.tebet.mojual.common.util.Utility
import com.tebet.mojual.data.models.Message
import com.tebet.mojual.di.component.DaggerAppComponent
import com.tebet.mojual.di.module.AppModule
import com.tebet.mojual.di.module.NetModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import timber.log.Timber
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import java.util.*
import javax.inject.Inject

class App : MultiDexApplication(), HasActivityInjector, HasServiceInjector, Application.ActivityLifecycleCallbacks {
    val context: Context
        get() = this
    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>
    @Inject
    lateinit var serviceInjector: DispatchingAndroidInjector<Service>
    @Inject
    lateinit var mCalligraphyConfig: CalligraphyConfig

    var notificationHandlerData: MutableLiveData<Message?> = MutableLiveData()
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        @Suppress("DEPRECATION")
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .netModule(NetModule(ConfigEnv.apiRoot))
            .build()
            .inject(this)

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)

        Timber.plant(if (ConfigEnv.needCrashLogging) CrashReportingTree() else Timber.DebugTree())
        CalligraphyConfig.initDefault(mCalligraphyConfig)

        Utility.init(this)
        PreferenceUtils.init(this)
        registerActivityLifecycleCallbacks(this)

        var uuid: String? = null
        try {
            uuid = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        } catch (ignored: Exception) {
            Timber.e(ignored)
        }

        AuthSdk.init(
            this, ConfigEnv.apiRoot,
            "", "", uuid!!
        )

        //        SquTypefaceHandler.INSTANCE.initialize(this);

        val languageIndex = LanguageUtil.instance.getLanguageIndex()
        if (languageIndex < 0) {
            val deviceDefLang = Locale.getDefault().isO3Language.toLowerCase()
            Timber.d("Device Default Lang on init : %s", deviceDefLang)
            if (deviceDefLang == "ind") {
                LanguageUtil.instance.changeBahasa(this)
            } else {
                LanguageUtil.instance.changeEnglish(this)
            }
        }
    }

    override fun onActivityCreated(activity: Activity?, bundle: Bundle?) {
        wasInBackground = false
        stateOfLifeCycle = "Create"
    }

    override fun onTrimMemory(level: Int) {
        if (stateOfLifeCycle == "Stop") {
            wasInBackground = true
        }
        super.onTrimMemory(level)
    }

    override fun onActivityStarted(activity: Activity?) {
        stateOfLifeCycle = "Start"
    }

    override fun onActivityResumed(activity: Activity?) {
        stateOfLifeCycle = "Resume"
    }

    override fun onActivityPaused(activity: Activity?) {
        stateOfLifeCycle = "Pause"
    }


    override fun onActivityStopped(activity: Activity?) {
        stateOfLifeCycle = "Stop"
    }

    override fun onActivityDestroyed(activity: Activity?) {
        wasInBackground = false
        stateOfLifeCycle = "Destroy"
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    /**
     *
     *
     *
     * A tree which logs important information for crash reporting.
     */
    private class CrashReportingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }
            Crashlytics.logException(t)
        }
    }

    companion object {
        lateinit var instance: App
            private set
        var wasInBackground = false
        var stateOfLifeCycle = ""
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

    override fun serviceInjector(): AndroidInjector<Service> = serviceInjector
}

