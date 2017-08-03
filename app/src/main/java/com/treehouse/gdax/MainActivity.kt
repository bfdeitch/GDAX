package com.treehouse.gdax

import android.arch.lifecycle.LifecycleActivity
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.widget.DrawerLayout
import android.view.Gravity
import android.widget.Toolbar
import org.jetbrains.anko.*
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.support.v4.drawerLayout
import kotlin.concurrent.thread

class MainActivity : LifecycleActivity() {
  lateinit var drawer: DrawerLayout
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

    thread {
      while (true) {
        Thread.sleep(5000)
        val numBids = db.openOrdersDao().getCt()
        e("Num Bids: $numBids")

        val bestBids = db.openOrdersDao().getBids()
        e("Size: ${bestBids.size}, BestBid: ${bestBids[0]}")

        val bestAsks = db.openOrdersDao().getAsks()
        e("Size: ${bestAsks.size}, BestAsk: ${bestAsks[0]}")
      }
    }

    drawer = drawerLayout {

      coordinatorLayout {
        backgroundColor = primaryColor

        appBarLayout {
          val actionBarHeight =  context.theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize)).getDimension(0, 0f).toInt()
          toolbar = toolbar {
            title = "Trade History"
            setTitleTextColor(Color.WHITE)
            backgroundColor = primaryColorLight
            setNavigationIcon(R.drawable.ic_menu_black_24dp)
          }.lparams(width = matchParent, height = actionBarHeight) {
            scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
                    AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
          }
          this@MainActivity.setActionBar(toolbar)
          actionBar!!.setDisplayHomeAsUpEnabled(true)
          toolbar.setNavigationOnClickListener { drawer.openDrawer(Gravity.LEFT) }
        }.lparams(width = matchParent)

        frameLayout {
          id = 123
        }.lparams(width = matchParent, height = matchParent) {
          behavior = AppBarLayout.ScrollingViewBehavior()
        }
      }.lparams(width = matchParent, height = matchParent)

      // Drawer
      val navDrawer = NavDrawer(this@MainActivity, {
        switchFragment(it)
        drawer.closeDrawers()
      })
      navDrawer.lparams(width = dip(250), height = matchParent) {
        gravity = Gravity.START
      }
      this.addView(navDrawer)
    }

    switchFragment(navDrawerItems[0])
  }

  fun switchFragment(entry: NavDrawerEntry) {
    e("SWITCH FRAGMENT: ${entry.fragment}")
    val fragmentTransaction = supportFragmentManager.beginTransaction()
    fragmentTransaction.replace(123, entry.fragment)
    fragmentTransaction.commit()

    toolbar.title = entry.title
    invalidateOptionsMenu()
  }
}
