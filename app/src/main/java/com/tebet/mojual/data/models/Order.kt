package com.tebet.mojual.data.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.tebet.mojual.BR
import java.io.Serializable

data class Order(
    var orderId: Int,
    var orderCode: String,
    var quantity: Int? = null,
    var planDate: Long? = null,
    var price: Double? = null,
    var totalPrice: Double? = null
) : Serializable, BaseObservable() {
    var isSelected: Boolean = false
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.selected)
        }
}
