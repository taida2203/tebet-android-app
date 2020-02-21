package com.tebet.mojual.data.models.enumeration


/**
 * Created by TaiDA
 */
enum class ContainerOrderState {
    INITIAL_PRICE_OFFERED,
    QUALITY_REJECTED,
    INITIAL_PRICE_REJECTED,
    SALE_REQUESTED,
    WAIT_PICKUP,
    CHECKING,
    SALE_REJECTED,
    RETURNED,
    FINAL_PRICE_OFFERED,
    SALE_CONFIRMED,
    PAID,
    CONFIRM_PICK_UP,
    UNKNOWN
}
