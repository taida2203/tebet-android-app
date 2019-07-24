package com.tebet.mojual.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(
    tableName = "UserProfile"
)
class UserProfile(
    @Json(name = "userId")
    @PrimaryKey
    @ColumnInfo(name = "userId")
    val userId: Int?,

    @Json(name = "profileId")
    @ColumnInfo(name = "profileId")
    val profileId: Int?,

    @Json(name = "profileType")
    @ColumnInfo(name = "profileType")
    val profileType: String?,

    @Json(name = "status")
    @ColumnInfo(name = "status")
    val status: String?,

    @Json(name = "username")
    @ColumnInfo(name = "username")
    val username: String?,

    @Json(name = "phone")
    @ColumnInfo(name = "phone")
    val phone: String?

//    @Json(name = "user")
//    @ColumnInfo(name = "user")
//    val user: User?
)
