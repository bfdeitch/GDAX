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
import android.graphics.PorterDuff
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner


data class BottomNavEntry(val title: String, val fragment: Fragment)
val bottomNavItems = arrayOf(
        BottomNavEntry("Order Book", OpenOrdersFragment()),
        BottomNavEntry("Price Chart", ChartFragment()),
        BottomNavEntry("Trade History", TradeHistoryFragment()))

class MainActivity : LifecycleActivity() {
  var currentFragmentId = 2
  var selectedGranularityId = 0
  lateinit var coordinatorLayout: CoordinatorLayout
  lateinit var appBarLayout: AppBarLayout
  lateinit var toolbar: Toolbar
  lateinit var spinner: Spinner

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

            spinner = spinner {
              val choices = arrayOf("15m", "1h", "6h", "1d")
              val spinnerArrayAdapter = ArrayAdapter<String>(this@MainActivity, R.layout.spinner_item, choices)
              spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
              adapter = spinnerArrayAdapter
              background.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
              onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(adapterView: AdapterView<*>) {}
                override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                  selectedGranularityId = position
                  val granularity = when (position) {
                    0 -> 15 * 60
                    1 -> 60 * 60
                    2 -> 6 * 60 * 60
                    else -> 24 * 60 * 60
                  }
                  (bottomNavItems[1].fragment as ChartFragment).chartView.resetCandles(granularity)
                }

              }
            }.lparams(Gravity.RIGHT)
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
    val granularity = bundle.getInt("GRANULARITY", 0)
    switchFragment(fragmentId)
    spinner.setSelection(granularity)
  }

  override fun onSaveInstanceState(bundle: Bundle) {
    super.onSaveInstanceState(bundle)
    bundle.putInt("FRAGMENT", currentFragmentId)
    bundle.putInt("GRANULARITY", selectedGranularityId)
  }

  fun switchFragment(fragmentId: Int) {
    currentFragmentId = fragmentId
    val entry = bottomNavItems[fragmentId]

    //https://stackoverflow.com/questions/30554824/how-to-reset-the-toolbar-position-controlled-by-the-coordinatorlayout
    val consumed = IntArray(2)
    val behavior = (appBarLayout.layoutParams as CoordinatorLayout.LayoutParams).behavior
    behavior?.onNestedPreScroll(coordinatorLayout, appBarLayout, null, 0, -1000, consumed)

    val fragmentTransaction = supportFragmentManager.beginTransaction()
    fragmentTransaction.replace(123, entry.fragment)
    fragmentTransaction.commit()

    if (entry.fragment is OpenOrdersFragment) {
      entry.fragment.recyclerView?.smoothScrollToPosition(0)
    }

    spinner.visibility = if (entry.fragment is ChartFragment) View.VISIBLE else View.GONE

    toolbar.title = entry.title
    invalidateOptionsMenu()
  }
}
