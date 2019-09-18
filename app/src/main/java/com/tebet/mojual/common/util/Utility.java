package com.tebet.mojual.common.util;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;

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
            message = context.getString(string_id);
        } catch (Exception e) {
            Timber.e(e);
        }
        return message;
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
