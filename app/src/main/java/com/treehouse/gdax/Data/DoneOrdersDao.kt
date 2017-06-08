package com.treehouse.gdax.Data


@android.arch.persistence.room.Dao
interface DoneOrdersDao {
  @android.arch.persistence.room.Query("SELECT * FROM DONE_ORDERS ORDER BY PRICE DESC")
  fun getAll(): List<DoneOrder>

  @android.arch.persistence.room.Insert
  fun insert(doneOrder: DoneOrder)

  @android.arch.persistence.room.Delete
  fun delete(doneOrders: List<DoneOrder>)
}