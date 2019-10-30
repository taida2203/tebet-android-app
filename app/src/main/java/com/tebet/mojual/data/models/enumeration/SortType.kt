package com.tebet.mojual.data.models.enumeration

/**
 * Created by TaiDA
 */
enum class SortType {
    ASC,
    DESC;

    companion object {
        fun getByName(name: String): SortType {
            val convertedItem = values().firstOrNull {
                it.name.equals(
                    name,
                    ignoreCase = true
                )
            }
            if (convertedItem != null) {
                return convertedItem
            }
            return DESC
        }
    }
}
