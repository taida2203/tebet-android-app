package com.tebet.mojual.data.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.tebet.mojual.BR

@Entity(
    tableName = "UserProfile"
)
data class UserProfile(
    @Json(name = "status")
    @ColumnInfo(name = "status")
    var status: String? = null,
    var authenticationType: String? = null,
    var avatar: String? = null,
    var bankCode: String? = null,
    var bankName: String? = null,
    var bankRegionCode: String? = null,
    var bankRegionName: String? = null,
    var email: String? = null,
    var firstName: String? = null,
    var fullName: String? = null,
    var gender: String? = null,
    var ktp: String? = null,
    var ktpNumber: String? = null,
    var lastName: String? = null,
    var bankAccountNumber: String? = null,
    var bankAccountName: String? = null,
    @Json(name = "phone")
    @ColumnInfo(name = "phone")
    var phone: String? = null,

    @Json(name = "profileId")
    @ColumnInfo(name = "profileId")
    var profileId: Int? = null,
    @Json(name = "profileType")
    @ColumnInfo(name = "profileType")
    var profileType: String? = null,
    var token: String? = null,
    @Json(name = "userId")
    @PrimaryKey
    @ColumnInfo(name = "userId")
    var userId: Int? = null,
    @Json(name = "username")
    @ColumnInfo(name = "username")
    var username: String? = null
) : BaseObservable() {
    var avatarLocal: String? = null
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.avatarLocal)
        }
    var ktpLocal: String? = null
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.ktpLocal)
        }
    var birthday: String? = null
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.birthday)
        }
    @Ignore
    var domicileAddress: Address? = null
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.domicileAddress)
        }

    @Ignore
    var pickupAddress: Address? = null
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.pickupAddress)
        }
}
