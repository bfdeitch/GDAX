package com.treehouse.gdax

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Delete

@Dao
interface OpenOrdersDao {
  @Query("SELECT * FROM OPEN_ORDERS ORDER BY PRICE DESC")
  fun getAll(): List<OpenOrder>

  @Insert
  fun insert(openOrder: OpenOrder)

  @Delete
  fun delete(openOrders: List<OpenOrder>)
}