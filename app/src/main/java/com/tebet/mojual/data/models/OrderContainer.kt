package com.tebet.mojual.data.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.Ignore
import com.tebet.mojual.BR

data class OrderContainer(
    var assetId: Long? = null,
    var assetCode: String? = null,
    var orderId: Long? = null,
    var orderCode: String? = null,
    var combinedCode: String? = null,
    var basePrice: Double? = null,
    var firstFinalizedPrice: Double? = null,
    var secondFinalizedPrice: Double? = null,
    var holderProfileId: Long? = null,
    var densityQc: Double? = null,
    var temperatureQc: Double? = null,
    var eugenolQc: Double? = null,
    var weightQc: Double? = null,
    var weightCs: Double? = null,
    var densityIot: Double? = null,
    var temperatureIot: Double? = null,
    var eugenolIot: Double? = null,
    var pricePerKg: Double? = null,
    var status: String? = null,
    var createdBy: String? = null,
    var createdDate: Long? = null,
    var modifiedBy: String? = null,
    var modifiedDate: Long? = null
) : BaseObservable() {
    var isSelected: Boolean = true
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.selected)
        }
    @Ignore
    var expanded: Boolean = true
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.expanded)
        }
}
