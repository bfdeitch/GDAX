package com.treehouse.gdax

import android.arch.lifecycle.LifecycleActivity
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.Fragment
import android.widget.Toolbar
import org.jetbrains.anko.*
import org.jetbrains.anko.design.*
import kotlin.concurrent.thread

data class BottomNavEntry(val title: String, val fragment: Fragment)
val bottomNavItems = arrayOf(
        BottomNavEntry("Order Book", OpenOrdersFragment()),
        BottomNavEntry("Price Chart", ChartFragment()),
        BottomNavEntry("Trade History", TradeHistoryFragment()))

class MainActivity : LifecycleActivity() {
  var currentFragmentId = 2
  lateinit var coordinatorLayout: CoordinatorLayout
  lateinit var appBarLayout: AppBarLayout
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
      coordinatorLayout = coordinatorLayout {
        backgroundColor = primaryColor

        appBarLayout = appBarLayout {
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
            R.id.openOrders -> switchFragment(0)
            R.id.priceChart -> switchFragment(1)
            R.id.tradeHistory -> switchFragment(2)
          }
          true
        }
      }.lparams(width = matchParent) {
        alignParentBottom()
      }
    }

    switchFragment(2)
  }

  override fun onRestoreInstanceState(bundle: Bundle) {
    val fragmentId = bundle.getInt("FRAGMENT", 2)
    switchFragment(fragmentId)
  }

  override fun onSaveInstanceState(bundle: Bundle) {
    super.onSaveInstanceState(bundle)
    bundle.putInt("FRAGMENT", currentFragmentId)
  }

  fun switchFragment(fragmentId: Int) {
    currentFragmentId = fragmentId
    val entry = bottomNavItems[fragmentId]
    e("SWITCH FRAGMENT1: ${entry.fragment}")

    //https://stackoverflow.com/questions/30554824/how-to-reset-the-toolbar-position-controlled-by-the-coordinatorlayout
    val consumed = IntArray(2)
    val behavior = (appBarLayout.layoutParams as CoordinatorLayout.LayoutParams).behavior
    behavior?.onNestedPreScroll(coordinatorLayout, appBarLayout, null, 0, -1000, consumed)

    e("SWITCH FRAGMENT2: ${entry.fragment}")
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
