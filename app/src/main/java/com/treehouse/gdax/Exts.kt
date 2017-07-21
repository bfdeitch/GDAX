package com.treehouse.gdax

import android.graphics.Color
import android.util.Log


val green = Color.parseColor("#70ce5c")
val red = Color.parseColor("#ff6939")
val primaryColor = Color.parseColor("#1e2b34")
val primaryColorLight = Color.parseColor("#2F3D45")

fun Any.e(any: Any? = "no message provided") {
  Log.e(this.javaClass.simpleName + "`~", any.toString())
}

fun formatNumString(number: Float, decimalSpots: Int): String {
  val beforeDec = number.toString().substringBefore(".")
  val afterDec = number.toString().substringAfter(".").padEnd(decimalSpots, '0')
  return "$beforeDec.$afterDec"
}
