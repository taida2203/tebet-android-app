package com.tebet.mojual.common.handler;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import androidx.multidex.MultiDexApplication;
import co.squline.sdk.auth.AuthSdk;
import com.facebook.FacebookSdk;
import com.tebet.mojual.R;
import com.tebet.mojual.common.constant.ConfigEnv;
import com.tebet.mojual.common.constant.ConfigVolley;
import com.tebet.mojual.common.models.GMTResponse;
import com.tebet.mojual.common.rtc.util.LanguageUtil;
import com.tebet.mojual.common.rtc.util.PreferenceUtils;
import com.tebet.mojual.common.util.Utility;
import timber.log.Timber;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AppController extends MultiDexApplication {
    public static final String TAG = AppController.class.getSimpleName();
    public static List<GMTResponse> gmtList = new ArrayList<>();
    private static AppController mInstance;

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        FacebookSdk.setApplicationId(getResources().getString(R.string.facebook_app_id));
//        Crashlytics crashlyticsKit = new Crashlytics.Builder()
//                .core(new CrashlyticsCore.Builder().disabled(!ConfigEnv.INSTANCE.isProductionEnv()).build())
//                .build();
//        Fabric.with(this, crashlyticsKit);
        Timber.plant(ConfigEnv.INSTANCE.getNeedCrashLogging() ? new CrashReportingTree() : new Timber.DebugTree());
        Utility.init(this);
        PreferenceUtils.init(this);

//        AnalyticEngine.INSTANCE.init(this, ConfigEnv.INSTANCE.isAnalyticEnabled());
//        RTCApplication.Companion.getInstance().init(this, ConfigEnv.INSTANCE.isProductionEnv());
        FacebookSdk.setAutoLogAppEventsEnabled(ConfigEnv.INSTANCE.isProductionEnv());
        FacebookSdk.setAdvertiserIDCollectionEnabled(ConfigEnv.INSTANCE.isProductionEnv());
        String uuid = null;
        try {
            uuid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            PreferenceUtils.saveString(ConfigVolley.DEVICEID, uuid);
        } catch (Exception ignored) {
            Timber.e(ignored);
        }
        AuthSdk.Companion.init(this, ConfigEnv.INSTANCE.getApiRoot(),
                ConfigEnv.INSTANCE.getConsumerKey(), ConfigEnv.INSTANCE.getConsumerSecret(), uuid);

//        SquTypefaceHandler.INSTANCE.initialize(this);
//        PaymentApplication.INSTANCE.init(this, ConfigEnv.INSTANCE.isProductionEnv());

        int languageIndex = LanguageUtil.Companion.getInstance().getLanguageIndex();
        if (languageIndex < 0) {
//            String language = Locale.getDefault().toString();
//            if (!TextUtils.isEmpty(language) && !"in-ID".equalsIgnoreCase(language) && !"in_ID".equalsIgnoreCase(language)) {
//                LanguageUtil.Companion.getInstance().changeEnglish(this);
//            } else {
//                LanguageUtil.Companion.getInstance().changeBahasa(this);
//            }
            String deviceDefLang = Locale.getDefault().getISO3Language().toLowerCase();
            Timber.d("Device Default Lang on init : %s", deviceDefLang);
            if (deviceDefLang.equals("ind")) {
                LanguageUtil.Companion.getInstance().changeBahasa(this);
            } else {
                LanguageUtil.Companion.getInstance().changeEnglish(this);
            }
        }

//        QiscusCore.getChatConfig()
//                .setEnableFcmPushNotification(true)
//                .setEnableLog(true);
    }

    public Context getContext() {
        return this;
    }

    /**
     * A tree which logs important information for crash reporting.
     */
    private static class CrashReportingTree extends Timber.Tree {
        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }
//            Crashlytics.logException(t);
        }
    }
}

