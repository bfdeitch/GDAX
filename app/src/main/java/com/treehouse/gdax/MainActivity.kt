package com.treehouse.gdax

import android.arch.lifecycle.LifecycleActivity
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.view.Gravity
import android.widget.Toolbar
import org.jetbrains.anko.*
import org.jetbrains.anko.design.*
import kotlin.concurrent.thread

data class BottomNavEntry(val title: String, val fragment: Fragment)
val bottomNavItems = arrayOf(
        BottomNavEntry("Order Book", OpenOrdersFragment()),
        BottomNavEntry("Charts", OpenOrdersFragment()),
        BottomNavEntry("Trade History", TradeHistoryFragment()))

class MainActivity : LifecycleActivity() {
  lateinit var toolbar: Toolbar

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

    lifecycle.addObserver(MyWebSocket())

//    thread {
//      while (true) {
//        Thread.sleep(5000)
//        val numBids = db.openOrdersDao().getCt()
//        e("Num Bids: $numBids")
//
//        val bestBids = db.openOrdersDao().getBids()
//        e("Size: ${bestBids.size}, BestBid: ${bestBids[0]}")
//
//        val bestAsks = db.openOrdersDao().getAsks()
//        e("Size: ${bestAsks.size}, BestAsk: ${bestAsks[0]}")
//      }
//    }

      coordinatorLayout {
        lparams(width = matchParent, height = matchParent)
        backgroundColor = primaryColor

        appBarLayout {
          val actionBarHeight =  context.theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize)).getDimension(0, 0f).toInt()
          toolbar = toolbar {
            title = "Trade History"
            setTitleTextColor(Color.WHITE)
            backgroundColor = primaryColorLight
          }.lparams(width = matchParent, height = actionBarHeight) {
            scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
                    AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
          }
          this@MainActivity.setActionBar(toolbar)
        }.lparams(width = matchParent)

        frameLayout {
          id = 123
        }.lparams(width = matchParent, height = matchParent) {
          behavior = AppBarLayout.ScrollingViewBehavior()
        }

        val bottomNav = include<BottomNavigationView>(R.layout.bottom_navigation) {
            setOnNavigationItemSelectedListener {
              when (it.itemId) {
                R.id.openOrders -> switchFragment(bottomNavItems[0])
                R.id.priceChart -> switchFragment(bottomNavItems[1])
                R.id.tradeHistory -> switchFragment(bottomNavItems[2])
              }
              true
            }
        }.lparams {
          gravity = Gravity.BOTTOM
        }
      }

    switchFragment(bottomNavItems[2])
  }

  fun switchFragment(entry: BottomNavEntry) {
    e("SWITCH FRAGMENT: ${entry.fragment}")
    val fragmentTransaction = supportFragmentManager.beginTransaction()
    fragmentTransaction.replace(123, entry.fragment)
    fragmentTransaction.commit()

    toolbar.title = entry.title
    invalidateOptionsMenu()
  }
}
