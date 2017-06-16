package com.treehouse.gdax.Data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface MatchOrdersDao {
  @Query("SELECT * FROM MATCH_ORDERS ORDER BY SEQUENCE DESC")
  fun loadMatchedOrdersSync(): LiveData<List<MatchOrder>>

  @Query("SELECT * FROM MATCH_ORDERS ORDER BY PRICE DESC")
  fun getAll(): List<MatchOrder>

  @Insert
  fun insert(matchOrder: MatchOrder)

  @Delete
  fun delete(matchOrders: List<MatchOrder>)
}