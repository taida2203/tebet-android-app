package com.tebet.mojual.data.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.Ignore
import com.tebet.mojual.BR
import com.tebet.mojual.data.models.enumeration.AssetAction

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
    var initialPrice: Double? = null,
    var pricePerKg: Double? = null,
    var status: String? = null,
    var createdBy: String? = null,
    var createdDate: Long? = null,
    var modifiedBy: String? = null,
    var modifiedDate: Long? = null,
    var action: String? = null,
    var rejectMessage1: String? = null,
    var rejectMessage2: String? = null
) : BaseObservable() {
    var isSelected: Boolean = true
        @Bindable get() = field
        set(value) {
            field = value
            action = if(value) AssetAction.APPROVE.name else AssetAction.REJECT.name
            notifyPropertyChanged(BR.selected)
        }
    @Ignore
    var expanded: Boolean = true
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.expanded)
        }

    override fun equals(other: Any?): Boolean {
        if (other is OrderContainer) {
            return assetId == other.assetId && orderId == other.orderId
        }
        return super.equals(other)
    }
}
