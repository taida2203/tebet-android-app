package com.tebet.mojual.common.rtc.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tebet.mojual.common.rtc.constant.SqulineConstant;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 */
public final class PreferenceUtils {
    private static SharedPreferences _shareRefs = null;
    private static Context context;
    private static PreferenceUtils sInstance;

    private PreferenceUtils(Context ct) {
        context = ct;
        _shareRefs = context.getSharedPreferences(SqulineConstant.Companion.getPREF_NAME(),
                Activity.MODE_PRIVATE);
    }

    public static void init(Context context) {
        if (sInstance != null) {
        }
        sInstance = new PreferenceUtils(context);
    }

    public static PreferenceUtils getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException("Uninitialized.");
        }
        return sInstance;
    }

    public static void saveObject(String key, Object value) {
        Editor editor = _shareRefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(value);
        editor.putString(key, json);
        editor.apply();
    }

    public static <T> T getObject(String key, Class<T> a) {
        Gson gson = new Gson();
        String dValue = _shareRefs.getString(key, "");
        return gson.fromJson(dValue, a);
    }

    public static <T> void saveListString(String key, List<T> value) {
        Editor editor = _shareRefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(value);
        editor.putString(key, json);
        editor.apply();
    }


    public static <T> List<T> getListString(String key, Class<T> a) {
        Gson gson = new Gson();
        String dValue = _shareRefs.getString(key, "CAPTURED_PHOTO");
        Type listType = new TypeToken<List<T>>() {}.getType();
        List<T> yourList = new ArrayList<>();
        try {
            yourList = new Gson().fromJson(dValue, listType);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return  yourList;
	}


    public static void saveDouble(String key, double value) {
        String dValue = String.valueOf(value);
        Editor editor = _shareRefs.edit();
        editor.putString(key, dValue);
        editor.apply();
    }

    public static double getDouble(String key, double defVa) {
        String strDefVa = String.valueOf(defVa);
        String dValue = _shareRefs.getString(key, strDefVa);
        return (dValue.equals(strDefVa)) ? defVa : Double
                .valueOf(dValue);
    }

    public static void saveFloat(String key, float value) {
        Editor editor = _shareRefs.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public static float getFloat(String key, float defVa) {
        return _shareRefs.getFloat(key, defVa);
    }

    public static void saveBoolean(String key, boolean value) {
        Editor editor = _shareRefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean(String key, boolean defVa) {
        return _shareRefs.getBoolean(key, defVa);
    }

    public static void saveString(String key, String value) {
        Editor editor = _shareRefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void saveStringSetting(String key1, String key2, String value1,
                                         String value2) {
        Editor editor = _shareRefs.edit();
        editor.putString(key1, value1);
        editor.apply();
    }

    public static String getString(String key, String defVa) {
        return _shareRefs.getString(key, defVa);
    }

    public static void saveInt(String key, int value) {

        Editor editor = _shareRefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getInt(String key, int defVa) {
        return _shareRefs.getInt(key, defVa);
    }

    public static void saveLong(String key, long value) {

        Editor editor = _shareRefs.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static long getLong(String key, long defVa) {
        return _shareRefs.getLong(key, defVa);
    }

    /**
     * clear all sharedPreferences
     */
    public static void clearAll() {
        Editor editor = _shareRefs.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * remove value sharePreference with key
     *
     * @param key
     */
    public static void remove(String key) {
        Editor editor = _shareRefs.edit();
        editor.remove(key);
        editor.apply();
    }
}