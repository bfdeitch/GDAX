package com.treehouse.gdax

import android.arch.lifecycle.LifecycleActivity
import android.arch.lifecycle.Observer
import android.arch.persistence.room.Room
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.treehouse.gdax.Data.AppDatabase
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.relativeLayout
import org.jetbrains.anko.support.v4.drawerLayout
import org.jetbrains.anko.wrapContent
import kotlin.concurrent.thread


class MainActivity : LifecycleActivity() {
  val db = Room.databaseBuilder(this, AppDatabase::class.java, "GDAX").build()
  val webSocket = MyWebSocket(db)

  fun clearDatabase() {
    thread {
      with(db.receivedOrdersDao()) { this.delete(this.getAll()) }
      with(db.openOrdersDao()) { this.delete(this.getAll()) }
      with(db.changeOrdersDao()) { this.delete(this.getAll()) }
      with(db.doneOrdersDao()) { this.delete(this.getAll()) }
      with(db.matchOrdersDao()) { this.delete(this.getAll()) }
    }
  }
  fun logDatabase() {
    thread {
      db.receivedOrdersDao().getAll().forEach { e(it) }
      db.openOrdersDao().getAll().forEach { e(it) }
      db.changeOrdersDao().getAll().forEach { e(it) }
      db.doneOrdersDao().getAll().forEach { e(it) }
      db.matchOrdersDao().getAll().forEach { e(it) }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

//    val actionBarHeight = theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize)).getDimension(0, 0f).toInt()
//    e("actionBarHeight: $actionBarHeight")
//    drawerLayout {
//      appBarLayout {
//        toolbar {
//          title = "Hello world!"
//        }.lparams(width = matchParent, height = actionBarHeight)
//      }.lparams(width = matchParent, height = wrapContent)

      relativeLayout {
        backgroundColor = Color.parseColor("#1e2b34")

        val myAdapter = MyAdapter(db)
        val myLayoutManger = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        myLayoutManger.isItemPrefetchEnabled = false
        recyclerView {
          adapter = myAdapter
          db.matchOrdersDao().loadMatchedOrdersSync().observe(this@MainActivity, Observer {
            // it == List<MatchOrder>
            if (it != null) {
              val trades = it.map { Trade(it.side == "sell", it.size, it.price, it.time) }
              myAdapter.trades.clear()
              myAdapter.trades.addAll(trades)
              myAdapter.notifyDataSetChanged()
            }
          })
          layoutManager = myLayoutManger
        }.lparams(width = matchParent)
//      }.lparams(width = matchParent)
    }
  }

  override fun onDestroy() {
    webSocket.shutDown()
    super.onDestroy()
  }
}
