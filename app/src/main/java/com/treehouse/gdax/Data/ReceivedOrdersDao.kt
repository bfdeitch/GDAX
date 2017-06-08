package com.treehouse.gdax.Data

@android.arch.persistence.room.Dao
interface ReceivedOrdersDao {
  @android.arch.persistence.room.Query("SELECT * FROM RECEIVED_ORDERS ORDER BY PRICE DESC")
  fun getAll(): List<ReceivedOrder>

  @android.arch.persistence.room.Insert
  fun insert(receivedOrder: ReceivedOrder)

  @android.arch.persistence.room.Delete
  fun delete(receivedOrders: List<ReceivedOrder>)
}