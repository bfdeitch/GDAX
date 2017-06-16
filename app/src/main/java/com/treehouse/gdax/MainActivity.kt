package com.treehouse.gdax

import android.arch.lifecycle.LifecycleActivity
import android.arch.lifecycle.Observer
import android.arch.persistence.room.Room
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.treehouse.gdax.Data.AppDatabase
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import kotlin.concurrent.thread

class MainActivity : LifecycleActivity() {
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
      backgroundColor = Color.parseColor("#1e2b34")

      val myAdapter = MyAdapter(db)
      val myLayoutManger = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
      myLayoutManger.isItemPrefetchEnabled = false
      recyclerView {
        id = 1
        adapter = myAdapter
        db.matchOrdersDao().loadMatchedOrdersSync().observe(this@MainActivity, Observer {
          // it == List<MatchOrder>
          if (it != null) {
            val trades = it.map { Trade(it.side == "sell", it.size.toFloat(), it.price.toFloat(), it.time) }
            myAdapter.trades.clear()
            myAdapter.trades.addAll(trades)
            myAdapter.notifyDataSetChanged()
          }
        })
        layoutManager = myLayoutManger
      }.lparams(width = matchParent)
      button {
        text = "Click Me!"
        onClick {
          thread {
            db.receivedOrdersDao().getAll().forEach { e(it) }
            db.openOrdersDao().getAll().forEach { e(it) }
            db.changeOrdersDao().getAll().forEach { e(it) }
            db.doneOrdersDao().getAll().forEach { e(it) }
            db.matchOrdersDao().getAll().forEach { e(it) }
          }
        }
      }.lparams {
        below(1)
      }
    }
  }

  override fun onDestroy() {
    webSocket.shutDown()
    super.onDestroy()
  }
}
