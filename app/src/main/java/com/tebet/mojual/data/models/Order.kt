package com.tebet.mojual.data.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.room.Ignore
import com.tebet.mojual.data.models.enumeration.OrderStatus
import java.io.Serializable

data class Order(
    override var orderId: Long = -1,
    override var orderCode: String = "",
    var quantity: Int? = null,
    var planDate: Long? = null,
    var totalPrice: Double? = null,
    var status: String? = null
) : Serializable, IOrder, BaseObservable() {
    constructor(order: IOrder) : this(order.orderId, order.orderCode) {
        if (order is OrderDetail) {
            price = order.price
            quantity = order.quantity
            planDate = order.planDate
            totalPrice = order.totalPrice
        }
    }

    @Ignore
    var price: Double? = null
    @Ignore
    var isSelected: Boolean = false
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.selected)
        }

    val isRejected: Boolean
        get() {
            when (status) {
                OrderStatus.REJECTED.name -> return true
            }
            return false
        }

    override fun equals(other: Any?): Boolean {
        if (other is Order) {
            return orderId == other.orderId
        }
        return super.equals(other)
    }
}
