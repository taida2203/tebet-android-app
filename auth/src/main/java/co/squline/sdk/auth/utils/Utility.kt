package co.squline.sdk.auth.utils

import android.content.Context
import com.tebet.mojual.sdk.auth.R

import timber.log.Timber

class Utility private constructor(ct: Context) {

    init {
        context = ct
    }

    fun getString(string_id: Int): String {
        var message = context.getString(R.string.general_message_error)
        try {
            message = context.getString(string_id)
        } catch (e: Exception) {
            Timber.e(e)
        }

        return message
    }

    companion object {
        private lateinit var context: Context
        private var sInstance: Utility? = null

        fun init(context: Context) {
            if (sInstance != null) {
            }
            sInstance = Utility(context)
        }

        val instance: Utility
            get() {
                if (sInstance == null) {
                    throw IllegalStateException("Uninitialized.")
                }
                return sInstance as Utility
            }
    }
}
