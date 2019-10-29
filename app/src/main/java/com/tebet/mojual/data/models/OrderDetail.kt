package com.tebet.mojual.data.models

import androidx.room.Ignore
import com.tebet.mojual.data.models.enumeration.ContainerOrderState
import com.tebet.mojual.data.models.enumeration.ContainerOrderStatus
import com.tebet.mojual.data.models.enumeration.OrderStatus
import java.io.Serializable

data class OrderDetail(
    override var orderId: Long,
    override var orderCode: String,
    var quantity: Int? = null,
    var planDate: Long? = null,
    var totalPrice: Double? = null,

    var code: String? = null,
    var note: String? = null,
    var status: String? = null,
    var soldDate: Long? = null,
    var profileId: Long? = null,
    var profileCode: String? = null,
    var deliveryBonus: Double? = null,
    var bonus: Double? = null,
    var releaseBonus: Double? = null,
    var amountToPay: Double? = null,
    var initialPriceDate: Long? = null,
    var saleRequestDate: Long? = null,
    var pickupDate: Long? = null,
    var arrivalFactoryDate: Long? = null,
    var qcDoneDate: Long? = null,
    var paidDate: Long? = null,
    var containersReturnedDate: Long? = null,
    var basePrice: Double? = null,
    var containers: List<OrderContainer>? = null,
    var rejectionQuestions: List<Question>? = null
) : Serializable, IOrder {
    constructor(order: IOrder) : this(order.orderId, order.orderCode) {
        if (order is Order) {
            price = order.price
            quantity = order.quantity
            planDate = order.planDate
            totalPrice = order.totalPrice
        }
    }
    val isRejected: Boolean
        get() {
            when (status) {
                OrderStatus.REJECTED.name -> return true
            }
            return false
        }

    val canAction: Boolean
        get() {
            return containers?.firstOrNull { it.canAction } != null
        }

    val containFirstFinalPrice: Boolean
        get() {
            return containers?.firstOrNull { it.status == ContainerOrderStatus.FIRST_FINALIZED_PRICE_OFFERED.name } != null
        }

    @Ignore
    var price: Double? = null
}
