package com.tebet.mojual.data.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.tebet.mojual.BR
import java.io.Serializable

data class ContainerWrapper(
    var id: Long = -1,
    var assignedContainers: ArrayList<Asset> = ArrayList(),
    var weight: Double = 20.0,
    var time: Long = 30,
    var customerData: Quality? = Quality()
) : Serializable, BaseObservable() {
    var selectedItem: Int = 0
        @Bindable get() = field
        set(value) {
            field = value
            var selectedContainer = assignedContainers[value]
            customerData?.containerId = selectedContainer.assetId
            customerData?.containerCode = selectedContainer.code
            notifyPropertyChanged(BR.selectedItem)
        }
    var checking: Boolean = false
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.checking)
        }
}
