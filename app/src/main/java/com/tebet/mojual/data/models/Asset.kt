package com.tebet.mojual.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "asset")
class Asset(
    @PrimaryKey
    @ColumnInfo(name = "assetId")
    var assetId: Long = -1,
    var code: String = "",
    var combinedCode: String? = null,
    var note: String? = null,
    var volumn: Double? = null,
    var unit: String? = null,
    var type: String? = null,
    var status: String? = null,
    var trackingId: String? = null,
    var holderId: Long? = null,
    var holderCode: String? = null,
    var assigneeId: Long? = null,
    var assigneeCode: String? = null,
    var placeHolder: String? = null,
    var createdBy: String? = null,
    var createdDate: Long? = null,
    var modifiedBy: String? = null,
    var modifiedDate: Long? = null,

    @Ignore
    var holder: UserProfile? = null,
    @Ignore
    var assignee: UserProfile? = null
) {
    companion object {
        var quantity: Int = 20
    }
}
