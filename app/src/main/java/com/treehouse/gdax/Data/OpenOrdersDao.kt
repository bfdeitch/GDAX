package com.treehouse.gdax.Data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface OpenOrdersDao {
  @Query("SELECT * FROM OPEN_ORDERS ORDER BY PRICE DESC")
  fun getAll(): List<OpenOrder>

  @Insert
  fun insert(openOrder: OpenOrder)

  @Delete
  fun delete(openOrders: List<OpenOrder>)
}