package com.tebet.mojual.data.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.Ignore
import com.tebet.mojual.BR
import java.io.Serializable

data class ContainerWrapper(
    var id: Long = -1,
    @Ignore
    var assignedContainers: List<Asset> = ArrayList(),
    var weight: Double = 20.0,
    var time: Long = 5,
    var customerData: Quality = Quality()
) : Serializable, BaseObservable() {
    enum class CheckStatus {
        CheckStatusDone,
        CheckStatusChecking,
        CheckStatusCheck
    }

    var selectedItem: Int = -1
        @Bindable get() = field
        set(value) {
            field = value
            val selectedContainer = assignedContainers[value]
            customerData.containerId = selectedContainer.assetId
            customerData.containerCode = selectedContainer.code
            customerData.tableId = customerData.orderId + selectedContainer.assetId
            notifyPropertyChanged(BR.selectedItem)
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
