package com.tebet.mojual.data.models

import androidx.databinding.*
import androidx.room.Ignore
import androidx.databinding.library.baseAdapters.BR
import com.tebet.mojual.data.models.enumeration.ContainerOrderType
import java.io.Serializable

data class ContainerWrapper(
    var time: Long = 30,
    var customerData: Quality = Quality(),
    var sensorConnected: Boolean = true
) : Serializable, BaseObservable() {
    enum class CheckStatus {
        CheckStatusDone,
        CheckStatusChecking,
        CheckStatusCheck
    }

    var containerType: String? = ContainerOrderType.JERRYCAN.toString()
        set(value) {
            field = value
            customerData.containerType = value
        }

    var weight: Double = when (containerType) {
        ContainerOrderType.JERRYCAN.toString() -> 20.0
        ContainerOrderType.DRUM.toString() -> 200.0
        else -> 20.0
    }

    val weightList: List<Int>
        @Bindable get() {
            return when (containerType) {
                ContainerOrderType.JERRYCAN.toString() -> (20..30).toList()
                ContainerOrderType.DRUM.toString() -> (195..205).toList()
                else -> (20..30).toList()
            }
        }

    var assignedContainers: ObservableList<Asset> = ObservableArrayList()

    var selectedItem: Int = -1
        @Bindable get
        set(value) {
            field = value
            val selectedContainer = assignedContainers[value]
            customerData.assetId = selectedContainer.assetId
            customerData.assetCode = selectedContainer.code
            customerData.combinedCode = selectedContainer.combinedCode
            customerData.tableId = customerData.orderId + selectedContainer.assetId
            containerType = selectedContainer.containerType ?: ContainerOrderType.JERRYCAN.toString()
            notifyPropertyChanged(BR.selectedItem)
            notifyPropertyChanged(BR.weightList)
        }

    var selectedWeight: Int = -1
        @Bindable get
        set(value) {
            field = value
            customerData.weight = weightList[value].toDouble()
            notifyPropertyChanged(BR.selectedWeight)
        }

    @Ignore
    var checking: CheckStatus = CheckStatus.CheckStatusCheck
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.checking)
        }
    @Ignore
    var expanded: Boolean = true
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.expanded)
        }
    @Ignore
    var timeCountDown: Long? = null
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.timeCountDown)
        }
}
