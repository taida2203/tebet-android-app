package com.tebet.mojual.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(
    tableName = "UserProfile"
)
class UserProfile(
    @Json(name = "status")
    @ColumnInfo(name = "status")
    var status: String? = null,

    var authenticationType: String? = null,
    var avatar: String? = null,
    var avatarLocal: String? = null,
    var bankCode: String? = null,
    var bankName: String? = null,
    var bankRegionCode: String? = null,
    var bankRegionName: String? = null,
    var birthday: String? = null,
    @Ignore
    var domicileAddress: Address? = null,
    var email: String? = null,
    var firstName: String? = null,
    var fullName: String? = null,
    var gender: String? = null,
    var ktp: String? = null,
    var ktpLocal: String? = null,
    var lastName: String? = null,
    var password: String? = null,
    @Json(name = "phone")
    @ColumnInfo(name = "phone")
    var phone: String? = null,
    @Ignore
    var pickupAddress: Address? = null,
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
)
