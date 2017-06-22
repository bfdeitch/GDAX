package com.treehouse.gdax.Data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "MATCH_ORDERS")
data class MatchOrder (
    @PrimaryKey var sequence: Int = 0,
    @ColumnInfo var type: String = "",
    @ColumnInfo var trade_id: Int = 0,
    @ColumnInfo var maker_order_id: String = "",
    @ColumnInfo var taker_order_id: String = "",
    @ColumnInfo var time: String = "",
    @ColumnInfo var size: Float = 0f,
    @ColumnInfo var price: Float = 0f,
    @ColumnInfo var side: String = ""
)