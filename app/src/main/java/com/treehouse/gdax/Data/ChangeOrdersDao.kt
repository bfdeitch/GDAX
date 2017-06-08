package com.treehouse.gdax.Data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface ChangeOrdersDao {
  @Query("SELECT * FROM CHANGE_ORDERS ORDER BY PRICE DESC")
  fun getAll(): List<ChangeOrder>

  @Insert
  fun insert(changeOrder: ChangeOrder)

  @Delete
  fun delete(changeOrders: List<ChangeOrder>)
}