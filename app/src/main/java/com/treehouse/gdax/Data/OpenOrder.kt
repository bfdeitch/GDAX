package com.treehouse.gdax.Data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "OPEN_ORDERS")
data class OpenOrder(
    @PrimaryKey var sequence: Int = 0,
    @ColumnInfo var type: String = "",
    @ColumnInfo var time: String = "",
    @ColumnInfo var order_id: String = "",
    @ColumnInfo var price: Float = 0f,
    @ColumnInfo var remaining_size: Float = 0f,
    @ColumnInfo var side: String = "")
