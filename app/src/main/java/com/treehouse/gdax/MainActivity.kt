package com.treehouse.gdax

import android.arch.lifecycle.LifecycleActivity
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.Gravity
import android.widget.Toolbar
import org.jetbrains.anko.*
import org.jetbrains.anko.design.*
import kotlin.concurrent.thread

data class BottomNavEntry(val title: String, val fragment: Fragment)
val bottomNavItems = arrayOf(
        BottomNavEntry("Order Book", OpenOrdersFragment()),
        BottomNavEntry("Charts", ChartFragment()),
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

    relativeLayout {
      lparams(width = matchParent, height = matchParent)
      coordinatorLayout {
        backgroundColor = primaryColor

        appBarLayout {
          val actionBarHeight = context.theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize)).getDimension(0, 0f).toInt()
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


      }.lparams(width = matchParent, height = matchParent) {
        above(R.id.navigation)
      }

      include<BottomNavigationView>(R.layout.bottom_navigation) {
        setOnNavigationItemSelectedListener {
          when (it.itemId) {
            R.id.openOrders -> switchFragment(bottomNavItems[0])
            R.id.priceChart -> switchFragment(bottomNavItems[1])
            R.id.tradeHistory -> switchFragment(bottomNavItems[2])
          }
          true
        }
      }.lparams(width = matchParent) {
        alignParentBottom()
      }
    }

    switchFragment(bottomNavItems[2])
  }

  fun switchFragment(entry: BottomNavEntry) {
    e("SWITCH FRAGMENT: ${entry.fragment}")
    val fragmentTransaction = supportFragmentManager.beginTransaction()
    fragmentTransaction.replace(123, entry.fragment)
    fragmentTransaction.commit()

    if (entry.fragment is OpenOrdersFragment) {
      e("RecyclerView: ${entry.fragment.recyclerView}")
      entry.fragment.recyclerView?.smoothScrollToPosition(0)
    }

    toolbar.title = entry.title
    invalidateOptionsMenu()
  }
}
