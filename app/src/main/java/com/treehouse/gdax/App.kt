package com.treehouse.gdax

import android.app.Application
import android.arch.persistence.room.Room
import com.treehouse.gdax.Data.AppDatabase

val db by lazy {
    App.db!!
}

class App : Application() {
    companion object {
        var db: AppDatabase? = null
    }

    override fun onCreate() {
        db = Room.databaseBuilder(this, AppDatabase::class.java, "GDAX").build()
        super.onCreate()
    }
}