package com.tebet.mojual.data.models

import android.os.Parcel
import android.os.Parcelable

class Address(
    var address: String? = null,
    var city: String? = null,
    var country: String? = null,
    var kecamatan: String? = null,
    var kelurahan: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var postalCode: String? = null,
    var rtrw: String? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(address)
        parcel.writeString(city)
        parcel.writeString(country)
        parcel.writeString(kecamatan)
        parcel.writeString(kelurahan)
        parcel.writeValue(latitude)
        parcel.writeValue(longitude)
        parcel.writeString(postalCode)
        parcel.writeString(rtrw)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Address> {
        override fun createFromParcel(parcel: Parcel): Address {
            return Address(parcel)
        }

        override fun newArray(size: Int): Array<Address?> {
            return arrayOfNulls(size)
        }
    }
}
