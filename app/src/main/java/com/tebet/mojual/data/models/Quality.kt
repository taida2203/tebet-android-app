package com.tebet.mojual.data.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.tebet.mojual.BR
import java.io.Serializable

data class Quality(
    var containerId: Long? = null,
    var containerCode: String? = null,
    var data: String? = null
) : Serializable, BaseObservable() {
    var isSelected: Boolean = false
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.selected)
        }
}
