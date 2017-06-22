package com.treehouse.gdax.Data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = arrayOf(ChangeOrder::class, DoneOrder::class, MatchOrder::class,
    OpenOrder::class, ReceivedOrder::class), version = 8)
abstract class AppDatabase : RoomDatabase() {
  abstract fun openOrdersDao(): OpenOrdersDao
  abstract fun changeOrdersDao(): ChangeOrdersDao
  abstract fun doneOrdersDao(): DoneOrdersDao
  abstract fun matchOrdersDao(): MatchOrdersDao
  abstract fun receivedOrdersDao(): ReceivedOrdersDao
}