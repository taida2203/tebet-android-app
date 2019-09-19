package com.tebet.mojual.data.models.enumeration

/**
 * Created by TaiDA
 */
enum class SortBy(val value: String) {
    STATUS("od.status"),
    SALE_DATE("od.planDate"),
    AMOUNT("od.totalPrice");

    companion object {
        fun getByName(name: String): SortBy? {
            val convertedItem = values().firstOrNull {
                it.name.equals(
                    name,
                    ignoreCase = true
                )
            }
            return convertedItem
        }
    }
}
