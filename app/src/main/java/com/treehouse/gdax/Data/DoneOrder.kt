package com.treehouse.gdax.Data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@android.arch.persistence.room.Entity(tableName = "DONE_ORDERS")
data class DoneOrder (
    @PrimaryKey var sequence: Long = 0,
    @ColumnInfo var type: String = "",
    @ColumnInfo var time: String = "",
    @ColumnInfo var price: String = "",
    @ColumnInfo var order_id: String = "",
    @ColumnInfo var reason: String = "",
    @ColumnInfo var side: String = "",
    @ColumnInfo var remaining_size: String = ""
)