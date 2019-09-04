package com.tebet.mojual

import android.app.Activity
import android.app.Service
import android.content.Context
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.multidex.MultiDexApplication
import co.common.util.LanguageUtil
import co.common.util.PreferenceUtils
import co.sdk.auth.AuthSdk
import com.facebook.stetho.Stetho
import com.google.firebase.messaging.RemoteMessage
import com.squareup.leakcanary.LeakCanary
import com.tebet.mojual.common.constant.ConfigEnv
import com.tebet.mojual.common.util.Utility
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

class App : MultiDexApplication(), HasActivityInjector, HasServiceInjector {
    val context: Context
        get() = this
    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>
    @Inject
    lateinit var serviceInjector: DispatchingAndroidInjector<Service>
    @Inject
    lateinit var mCalligraphyConfig: CalligraphyConfig

    var notificationHandlerData: MutableLiveData<RemoteMessage> = MutableLiveData()
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


        var uuid: String? = null
        try {
            uuid = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        } catch (ignored: Exception) {
            Timber.e(ignored)
        }

        AuthSdk.init(
            this, ConfigEnv.apiRoot,
            ConfigEnv.consumerKey, ConfigEnv.consumerSecret, uuid!!
        )

        //        SquTypefaceHandler.INSTANCE.initialize(this);

        val languageIndex = LanguageUtil.instance.getLanguageIndex()
        if (languageIndex < 0) {
            //            String language = Locale.getDefault().toString();
            //            if (!TextUtils.isEmpty(language) && !"in-ID".equalsIgnoreCase(language) && !"in_ID".equalsIgnoreCase(language)) {
            //                LanguageUtil.Companion.getInstance().changeEnglish(this);
            //            } else {
            //                LanguageUtil.Companion.getInstance().changeBahasa(this);
            //            }
            val deviceDefLang = Locale.getDefault().isO3Language.toLowerCase()
            Timber.d("Device Default Lang on init : %s", deviceDefLang)
            if (deviceDefLang == "ind") {
                LanguageUtil.instance.changeBahasa(this)
            } else {
                LanguageUtil.instance.changeEnglish(this)
            }
        }
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
            //            Crashlytics.logException(t);
        }
    }

    companion object {
        lateinit var instance: App
            private set
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

    override fun serviceInjector(): AndroidInjector<Service> = serviceInjector
}

