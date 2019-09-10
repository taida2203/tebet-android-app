package com.tebet.mojual.data.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.Ignore
import androidx.databinding.library.baseAdapters.BR
import java.io.Serializable

class Address(
    var address: String? = null,
    var country: String? = null,
    var kecamatan: String? = null,
    var kelurahan: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var postalCode: String? = null,
    var rtrw: String? = null,
    var type: String? = null
) : Serializable, BaseObservable() {
    companion object {
        const val DOMICILE_ADDRESS = "DOMICILE_ADDRESS"
        const val PICK_UP_ADDRESS = "PICK_UP_ADDRESS"
    }
    var mapLocation: String? = null
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.mapLocation)
        }
    var city: String? = null
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.city)
        }

    @Ignore
    var expanded: Boolean = true
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.expanded)
        }
}
