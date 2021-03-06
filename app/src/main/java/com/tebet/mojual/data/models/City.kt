package com.tebet.mojual.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "city"
)
data class City(
    @PrimaryKey
    @ColumnInfo(name = "code") var code: String,
    @ColumnInfo(name = "fullName") var fullName: String? = null,
    @ColumnInfo(name = "name") var name: String? = null,
    @ColumnInfo(name = "type") var type: String? = null
)