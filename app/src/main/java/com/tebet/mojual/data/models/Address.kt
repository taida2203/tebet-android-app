package com.tebet.mojual.data.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.tebet.mojual.BR
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
    var localTagPos: Int? = null
) : Serializable, BaseObservable() {
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
}
