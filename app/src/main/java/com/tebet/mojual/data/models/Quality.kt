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
    var sensorData: String? = null
) : Serializable, BaseObservable() {
    var assetCode: String = ""
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.assetCode)
        }
    var combinedCode: String? = ""
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.combinedCode)
        }

    var weight: Double = 20.0
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.weight)
        }

    @Ignore
    var isSelected: Boolean = false
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.selected)
        }
}
