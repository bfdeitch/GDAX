package com.treehouse.gdax

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.treehouse.gdax.Data.AppDatabase
import org.jetbrains.anko.*

data class Trade(val isBuy: Boolean, val size: Float, val price: Float, val time: String)
class MyAdapter(val db: AppDatabase) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
  val trades = mutableListOf<Trade>()

  override fun getItemCount() = trades.size

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = parent.include<ConstraintLayout>(R.layout.completed_trade)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.update(trades[position])
  }

  class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val sizeTextView = view.find<TextView>(R.id.sizeTextView)
    val priceTextView = view.find<TextView>(R.id.priceTextView)
    val timeTextView = view.find<TextView>(R.id.timeTextView)

    fun update(trade: Trade) {
      sizeTextView.text = trade.size.toString().padEnd(10, '0')
      priceTextView.text = trade.price.toString()
      priceTextView.textColor = if (trade.isBuy) green else red
      timeTextView.text = trade.time.substringAfter("T").substringBefore(".")
    }
  }
}