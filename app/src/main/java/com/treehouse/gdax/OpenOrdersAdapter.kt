package com.treehouse.gdax

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.treehouse.gdax.Data.PriceSideTuple
import org.jetbrains.anko.*

data class Order(val isBid: Boolean, val size: Float, val price: Float, val orderId: String)
class OpenOrdersAdapter : RecyclerView.Adapter<OpenOrdersAdapter.ViewHolder>() {
    val openOrders = mutableListOf<PriceSideTuple>()

    override fun getItemCount() = openOrders.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.include<ConstraintLayout>(R.layout.completed_trade)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.update(openOrders[position])
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val sizeBar = view.find<View>(R.id.sizeBar)
        val sizeTextView = view.find<TextView>(R.id.sizeTextView)
        val priceTextView = view.find<TextView>(R.id.priceTextView)

        fun update(pst: PriceSideTuple) {
            val color = if (pst.side == "buy") green else red
            sizeBar.backgroundColor = color
            val viewWidth = view.dip(if (pst.sum > 80) 80f else pst.sum + 1f)
            sizeBar.layoutParams = ConstraintLayout.LayoutParams(viewWidth, 0)
            sizeTextView.text = formatNumString(pst.sum, 8)
            priceTextView.text = formatNumString(pst.price, 2)
            priceTextView.textColor = color
        }
    }
}