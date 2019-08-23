package com.tebet.mojual.data.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.Ignore
import com.tebet.mojual.BR
import java.io.Serializable

data class Order(
    override var orderId: Int,
    override var orderCode: String,
    var quantity: Int? = null,
    var planDate: Long? = null,
    var totalPrice: Double? = null
) : Serializable, IOrder, BaseObservable() {
    @Ignore
    var price: Double? = null
    @Ignore
    var isSelected: Boolean = false
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.selected)
        }
}
