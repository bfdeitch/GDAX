package com.treehouse.gdax

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.treehouse.gdax.Data.AppDatabase
import org.jetbrains.anko.button
import org.jetbrains.anko.onClick
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.relativeLayout
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
  val db = Room.databaseBuilder(this, AppDatabase::class.java, "GDAX").build()
  val webSocket = MyWebSocket(db)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    thread {
      with(db.receivedOrdersDao()) { this.delete(this.getAll()) }
      with(db.openOrdersDao()) { this.delete(this.getAll()) }
      with(db.changeOrdersDao()) { this.delete(this.getAll()) }
      with(db.doneOrdersDao()) { this.delete(this.getAll()) }
      with(db.matchOrdersDao()) { this.delete(this.getAll()) }
    }
    relativeLayout {
      recyclerView {

      }
      button {
        onClick {
          thread {
            db.receivedOrdersDao().getAll().forEach { e(it) }
            db.openOrdersDao().getAll().forEach { e(it) }
            db.changeOrdersDao().getAll().forEach { e(it) }
            db.doneOrdersDao().getAll().forEach { e(it) }
            db.matchOrdersDao().getAll().forEach { e(it) }
          }
        }
      }
    }
  }

  override fun onDestroy() {
    webSocket.shutDown()
    super.onDestroy()
  }
}
