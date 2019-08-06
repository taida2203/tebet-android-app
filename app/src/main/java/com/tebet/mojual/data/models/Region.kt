package com.tebet.mojual.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(
    tableName = "region"
)
data class Region(
    @Json(name = "code")
    @PrimaryKey
    @ColumnInfo(name = "code") var code: String,
    @ColumnInfo(name = "name") var name: String? = null
)