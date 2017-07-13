package com.treehouse.gdax

import android.content.Context
import android.graphics.Color
import android.support.v4.app.Fragment
import org.jetbrains.anko.*


data class NavDrawerEntry(val title: String, val fragment: Fragment)
val navDrawerItems = arrayOf(
        NavDrawerEntry("Trade History", TradeHistoryFragment()),
        NavDrawerEntry("Order Book", OrdersFragment()),
        NavDrawerEntry("Charts", OrdersFragment()))

class NavDrawer(context: Context, action: (NavDrawerEntry) -> Unit) : _LinearLayout(context) {
    init {
        orientation = VERTICAL
        backgroundColor = primaryColorLight

        view {
            backgroundColor = green
        }.lparams(width = matchParent, height = dip(100))

        navDrawerItems.forEach { entry ->
            textView(entry.title) {
                textColor = Color.WHITE
                textSize = 22f
                padding  = dip(16)
                onClick {
                    action(entry)
                    context.toast(entry.title)
                }
            }.lparams(width = matchParent, height = dip(75))
        }
    }
}