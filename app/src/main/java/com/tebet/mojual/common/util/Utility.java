package com.tebet.mojual.common.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

import co.common.util.LanguageUtil;
import com.tebet.mojual.R;
import timber.log.Timber;

import java.util.Locale;

public class Utility {
    private Context context;
    private static Utility sInstance;

    private Utility(Context ct) {
        context = ct;
    }

    public static void init(Context context) {
        if (sInstance != null) {
            //TODO: init something
        }
        sInstance = new Utility(context);
    }

    public static Utility getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException("Uninitialized.");
        }
        return sInstance;
    }


    public String getString(int string_id) {
        String message = context.getString(R.string.general_message_error);
        try {
            message = getStringByLocal(context, string_id, LanguageUtil.Companion.getInstance().getLanguageIndex() == LanguageUtil.LANGUAGE_INDEX_ENGLISH ? "en": "in");
        } catch (Exception e) {
            Timber.e(e);
        }
        return message;
    }

    @NonNull
    public static String getStringByLocal(Context context, int resId, String locale) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            return getStringByLocalPlus17(context, resId, locale);
        else
            return getStringByLocalBefore17(context, resId, locale);
    }

    @NonNull
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static String getStringByLocalPlus17(Context context, int resId, String locale) {
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.setLocale(new Locale(locale));
        return context.createConfigurationContext(configuration).getResources().getString(resId);
    }

    private static String getStringByLocalBefore17(Context context,int resId, String language) {
        Resources currentResources = context.getResources();
        AssetManager assets = currentResources.getAssets();
        DisplayMetrics metrics = currentResources.getDisplayMetrics();
        Configuration config = new Configuration(currentResources.getConfiguration());
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        config.locale = locale;
        /*
         * Note: This (temporarily) changes the devices locale! TODO find a
         * better way to get the string in the specific locale
         */
        Resources defaultLocaleResources = new Resources(assets, metrics, config);
        String string = defaultLocaleResources.getString(resId);
        // Restore device-specific locale
        new Resources(assets, metrics, currentResources.getConfiguration());
        return string;
    }

    public String getDeviceLanguageId() {
        String language = Locale.getDefault().toString();
        int languageIndex = LanguageUtil.Companion.getInstance().getLanguageIndex();

        if (languageIndex < 0) {
            if (!TextUtils.isEmpty(language) && !"in-ID".equalsIgnoreCase(language) && !"in_ID".equalsIgnoreCase(language)) {
                language = "en-US";
            } else {
                language = "in-ID";
            }
        } else {
            switch (languageIndex) {
                case 0:
                    language = "en-US";
                    break;
                case 1:
                    language = "in-ID";
                    break;
            }
        }
        return language;
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

}
