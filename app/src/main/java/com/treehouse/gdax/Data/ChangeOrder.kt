package com.treehouse.gdax.Data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@android.arch.persistence.room.Entity(tableName = "CHANGE_ORDERS")
data class ChangeOrder (
    @PrimaryKey var sequence: Int = 0,
    @ColumnInfo var type: String = "",
    @ColumnInfo var time: String = "",
    @ColumnInfo var order_id: String = "",
    @ColumnInfo var new_size: String = "",
    @ColumnInfo var old_size: String = "",
    @ColumnInfo var price: String = "",
    @ColumnInfo var side: String = ""
)