package com.tebet.mojual.data.models.enumeration

/**
 * Created by TaiDA
 */
enum class DocumentType {
    LOGISTIC_RECEIPT,
    OTHER;

    companion object {
        @JvmStatic
        fun getByName(name: String): DocumentType? {
            return values().firstOrNull {
                it.name.equals(name, ignoreCase = true)
            }
        }
    }
}
