package com.tebet.mojual.data.models.enumeration

import java.util.Arrays

/**
 * Created by TaiDA
 */
enum class OrderStatus {
    OPEN, // order is in processing
    CLOSED, // order with no next steps
    REJECTED // still open but rejected and has next steps
}
