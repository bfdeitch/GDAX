package com.treehouse.gdax.Data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.PrimaryKey

@android.arch.persistence.room.Entity(tableName = "RECEIVED_ORDERS")
data class ReceivedOrder (
    @PrimaryKey var sequence: Long = 0,
    @ColumnInfo var type: String = "",
    @ColumnInfo var time: String = "",
    @ColumnInfo var order_id: String = "",
    @ColumnInfo var size: String = "",
    @ColumnInfo var price: String = "",
    @ColumnInfo var funds: String = "",
    @ColumnInfo var side: String = "",
    @ColumnInfo var order_type: String = ""
)