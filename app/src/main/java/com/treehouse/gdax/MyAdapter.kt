package com.treehouse.gdax

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.treehouse.gdax.Data.AppDatabase
import org.jetbrains.anko.*
import java.util.*
import java.util.concurrent.TimeUnit

data class Trade(val isBuy: Boolean, val size: Float, val price: Float, val time: String)
class MyAdapter : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
  val trades = mutableListOf<Trade>()

  override fun getItemCount() = trades.size

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = parent.include<ConstraintLayout>(R.layout.completed_trade)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.update(trades[position])
  }

  class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val sizeBar = view.find<View>(R.id.sizeBar)
    val sizeTextView = view.find<TextView>(R.id.sizeTextView)
    val priceTextView = view.find<TextView>(R.id.priceTextView)
    val timeTextView = view.find<TextView>(R.id.timeTextView)

    fun update(trade: Trade) {
      val color = if (trade.isBuy) green else red
      sizeBar.backgroundColor = color
      val viewWidth = view.dip(if (trade.size > 80) 80f else trade.size + 1f)
      sizeBar.layoutParams = ConstraintLayout.LayoutParams(viewWidth, 0)
      sizeTextView.text = formatNumString(trade.size, 8)
      priceTextView.text = formatNumString(trade.price, 2)
      priceTextView.textColor = color

      val gmtOffset = TimeUnit.HOURS.convert(TimeZone.getDefault().rawOffset.toLong(), TimeUnit.MILLISECONDS)
      val dstOffset = TimeUnit.HOURS.convert(Calendar.getInstance().get(Calendar.DST_OFFSET).toLong(), TimeUnit.MILLISECONDS)
      val timeString = trade.time.substringAfter("T").substringBefore(".")
      var hours = timeString.substringBefore(":").toInt() + gmtOffset + dstOffset
      val minutesSeconds = timeString.substringAfter(":")
      hours = if (hours < 0) 24 + hours else hours
      timeTextView.text = "$hours:$minutesSeconds"
    }

    fun formatNumString(number: Float, decimalSpots: Int): String {
      val beforeDec = number.toString().substringBefore(".")
      val afterDec = number.toString().substringAfter(".").padEnd(decimalSpots, '0')
      return "$beforeDec.$afterDec"
    }

  }
}