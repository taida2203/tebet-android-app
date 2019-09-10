package com.tebet.mojual.data.models

import androidx.databinding.*
import androidx.room.Ignore
import androidx.databinding.library.baseAdapters.BR
import java.io.Serializable

data class ContainerWrapper(
    var weight: Double = 20.0,
    var time: Long = 30,
    var customerData: Quality = Quality(),
    var sensorConnected: Boolean = true
) : Serializable, BaseObservable() {
    enum class CheckStatus {
        CheckStatusDone,
        CheckStatusChecking,
        CheckStatusCheck
    }

    var weightList: List<Int> = (20..30).toList()

    var assignedContainers: ObservableList<Asset> = ObservableArrayList()

    var selectedItem: Int = -1
        @Bindable get() = field
        set(value) {
            field = value
            val selectedContainer = assignedContainers[value]
            customerData.assetId = selectedContainer.assetId
            customerData.assetCode = selectedContainer.code
            customerData.combinedCode = selectedContainer.combinedCode
            customerData.tableId = customerData.orderId + selectedContainer.assetId
            notifyPropertyChanged(BR.selectedItem)
        }

    var selectedWeight: Int = -1
        @Bindable get() = field
        set(value) {
            field = value
            customerData.weight = weightList[value].toDouble()
            notifyPropertyChanged(BR.selectedWeight)
        }

    @Ignore
    var checking: CheckStatus = CheckStatus.CheckStatusCheck
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.checking)
        }
    @Ignore
    var expanded: Boolean = true
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.expanded)
        }
    @Ignore
    var timeCountDown: Long? = null
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.timeCountDown)
        }
}
