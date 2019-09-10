package com.tebet.mojual.data.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.databinding.library.baseAdapters.BR
import java.io.Serializable

@Entity(
    tableName = "quality"
)
data class Quality(
    @PrimaryKey
    var tableId: Long = -1,
    var orderId: Long = -1,
    var orderCode: String = "",
    var assetId: Long? = null,
    var combinedCode: String? = "",
    var sensorData: String? = null
) : Serializable, BaseObservable() {
    var assetCode: String = ""
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.assetCode)
        }

    var weight: Double = 20.0
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.weight)
        }

    @Ignore
    var isSelected: Boolean = false
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.selected)
        }
}
