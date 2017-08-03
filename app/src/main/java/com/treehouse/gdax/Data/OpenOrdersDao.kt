package com.treehouse.gdax.Data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE

data class PriceTuple (
  @ColumnInfo(name = "PRICE") var price: Float,
  @ColumnInfo(name = "SUM") var sum: Float
)
data class PriceSideTuple (
  @ColumnInfo(name = "PRICE") var price: Float,
  @ColumnInfo(name = "SIDE") var side: String,
  @ColumnInfo(name = "SUM") var sum: Float
) {
  constructor() : this(0f, "", 0f)
}
@Dao
interface OpenOrdersDao {
  @Query("""SELECT PRICE "PRICE", SIDE "SIDE", SUM(REMAINING_SIZE) "SUM"
            FROM OPEN_ORDERS
            GROUP BY PRICE, SIDE""")
  fun loadOpenOrdersSync(): LiveData<List<PriceSideTuple>>

  @Query("SELECT * FROM OPEN_ORDERS WHERE ORDER_ID = :arg0")
  fun getOrder(orderId: String): OpenOrder?

  @Update
  fun updateOrder(openOrder: OpenOrder)

  @Query("""SELECT PRICE "PRICE", SUM(REMAINING_SIZE) "SUM"
            FROM OPEN_ORDERS
            WHERE SIDE = "buy"
            GROUP BY PRICE
            ORDER BY PRICE DESC""")
  fun getBids(): List<PriceTuple>

  @Query("""SELECT PRICE "PRICE", SUM(REMAINING_SIZE) "SUM"
            FROM OPEN_ORDERS
            WHERE SIDE = "sell"
            GROUP BY PRICE
            ORDER BY PRICE ASC""")
  fun getAsks(): List<PriceTuple>

  @Query("SELECT COUNT(1) FROM OPEN_ORDERS WHERE SIDE = \"buy\"")
  fun getCt(): Int

  @Query("SELECT * FROM OPEN_ORDERS ORDER BY PRICE DESC")
  fun getAll(): List<OpenOrder>

  @Insert
  fun insert(openOrder: OpenOrder)

  @Insert(onConflict = REPLACE)
  fun insertOrders(openOrders: List<OpenOrder>)

  @Delete
  fun delete(openOrders: List<OpenOrder>)

  @Delete
  fun delete(openOrder: OpenOrder)
}