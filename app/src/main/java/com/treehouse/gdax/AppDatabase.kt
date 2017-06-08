package com.treehouse.gdax

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = arrayOf(OpenOrder::class), version = 3)
abstract class AppDatabase : RoomDatabase() {
  abstract fun openOrdersDao(): OpenOrdersDao
}