package com.treehouse.gdax

import android.content.Context
import android.graphics.Color
import org.jetbrains.anko.*


class NavDrawer(context: Context, action: () -> Unit) : _LinearLayout(context) {
    val items = arrayOf("Trade History", "Order Book", "Charts")
    init {
        orientation = VERTICAL
        backgroundColor = primaryColorLight

        view {
            backgroundColor = green
        }.lparams(width = matchParent, height = dip(100))

        items.forEach { name ->
            textView(name) {
                textColor = Color.WHITE
                textSize = 22f
                padding  = dip(16)
                onClick {
                    action()
                    context.toast(name)
                }
            }.lparams(width = matchParent, height = dip(75))
        }
    }
}