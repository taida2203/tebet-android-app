package com.tebet.mojual.data.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.*
import androidx.databinding.library.baseAdapters.BR
import co.common.util.LanguageUtil
import co.common.util.PreferenceUtils
import com.tebet.mojual.BuildConfig

@Entity(
    tableName = "UserProfile"
)
data class UserProfile(
    @PrimaryKey
    @ColumnInfo(name = "userId")
    var userId: Int? = null,
    @ColumnInfo(name = "status")
    var status: String? = null,
    var authenticationType: String? = null,
    var avatar: String? = null,
    var bankCode: String? = null,
    var bankRegionCode: String? = null,
    var email: String? = null,
    var firstName: String? = null,
    var fullName: String? = null,
    var gender: String? = null,
    var ktp: String? = null,
    var ktpNumber: String? = null,
    var rating: String? = "4.5",
    var bonus: Double? = 100000.0,
    var lastName: String? = null,
    var bankAccountNumber: String? = null,
    var bankAccountName: String? = null,
    @ColumnInfo(name = "phone")
    var phone: String? = null,

    @ColumnInfo(name = "profileId")
    var profileId: Int? = null,
    @ColumnInfo(name = "profileType")
    var profileType: String? = null,
    var token: String? = null,
    @ColumnInfo(name = "username")
    var username: String? = null
) : BaseObservable() {
    companion object;

    enum class Status(val status: String) {
        Init("INIT"), InitProfile("INIT_PROFILE"), New("NEW"), Verified("VERIFIED"), Rejected("REJECTED")
    }

    @Ignore
    var statusEnum: Status? = null
        get() = Status.values().firstOrNull { enum -> enum.status == status }
        set(value) {
            field = value
            status = Status.values().firstOrNull { enum -> enum == value }?.status
        }

    var avatarLocal: String? = null
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.avatarLocal)
        }
    var ktpLocal: String? = null
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.ktpLocal)
        }
    var birthday: String? = null
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.birthday)
        }

    var bankName: String? = null
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.bankName)
        }

    var bankRegionName: String? = null
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.bankRegionName)
        }

    @Embedded(prefix = "add1_")
    var domicileAddress: Address? = Address(type = Address.DOMICILE_ADDRESS)
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.domicileAddress)
        }

    @Embedded(prefix = "add2_")
    var pickupAddress: Address? = Address(type = Address.PICK_UP_ADDRESS)
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.pickupAddress)
        }

    @Ignore
    var basicInfo: Boolean = true
        @Bindable get
        set(value) {
            field = !value
            notifyPropertyChanged(BR.basicInfo)
        }

    @Ignore
    var bankAccount: Boolean = true
        @Bindable get
        set(value) {
            field = !value
            notifyPropertyChanged(BR.bankAccount)
        }

    @Ignore
    var language: Int = PreferenceUtils.getInt(LanguageUtil.PREF_LANGUAGE_INDEX, -1)
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.language)
        }

    @Ignore
    var version: String = BuildConfig.VERSION_NAME
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.version)
        }


    fun isUserVerified(): Boolean {
        return statusEnum == Status.Verified
    }
}
