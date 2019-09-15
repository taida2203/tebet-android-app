package co.common.util

import android.content.Context
import android.os.Build
import android.text.TextUtils
import java.util.*

class LanguageUtil {
    companion object {
        const val LANGUAGE_INDEX_ENGLISH = 0
        const val LANGUAGE_INDEX_BAHASA = 1
        const val PREF_LANGUAGE_INDEX = "LANGUAGE_INDEX"

        val instance = LanguageUtil()
    }

    fun getLanguageIndex(): Int {
        return PreferenceUtils.getInt(PREF_LANGUAGE_INDEX, -1)
    }

    fun updateLanguage(context: Context) {
        val language = Locale.getDefault().toString()
        val currentLanguageIndex: Int
        currentLanguageIndex = when {
            !TextUtils.isEmpty(language) && !"in".equals(language, ignoreCase = true) && !"in".equals(language, ignoreCase = true) -> LANGUAGE_INDEX_ENGLISH
            else -> LANGUAGE_INDEX_BAHASA
        }
        if (currentLanguageIndex == PreferenceUtils.getInt(
                PREF_LANGUAGE_INDEX,
                -1
            )
        ) return

        when (PreferenceUtils.getInt(
            PREF_LANGUAGE_INDEX,
            LANGUAGE_INDEX_ENGLISH
        )) {
            LANGUAGE_INDEX_ENGLISH -> changeEnglish(context)
            LANGUAGE_INDEX_BAHASA -> changeBahasa(context)
        }
    }

    fun changeBahasa(context: Context) {
        PreferenceUtils.saveInt(
            PREF_LANGUAGE_INDEX,
            LANGUAGE_INDEX_BAHASA
        )
        changeLanguage(context, "in")
    }

    fun changeEnglish(context: Context) {
        PreferenceUtils.saveInt(
            PREF_LANGUAGE_INDEX,
            LANGUAGE_INDEX_ENGLISH
        )
        changeLanguage(context, "en")
    }

    private fun changeLanguage(context: Context, languageCode: String) {
        val res = context.resources
        val dm = res.displayMetrics
        val conf = res.configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) { // API 17+ only.
            conf.setLocale(Locale(languageCode.toLowerCase()))
        } else {
            conf.locale = Locale(languageCode)
        }
        res.updateConfiguration(conf, dm)
    }
}