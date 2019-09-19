package com.tebet.mojual.data.models.enumeration

import java.util.Arrays

/**
 * Created by TaiDA
 */
enum class OrderStatus {
    OPEN, // order is in processing
    CLOSED, // order with no next steps
    REJECTED; // still open but rejected and has next steps

    companion object {
        fun getByName(name: String): OrderStatus? {
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
