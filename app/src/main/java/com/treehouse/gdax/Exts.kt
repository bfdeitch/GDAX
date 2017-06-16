package com.treehouse.gdax

import android.graphics.Color
import android.util.Log


val green = Color.parseColor("#70ce5c")
val red = Color.parseColor("#ff6939")

fun Any.e(any: Any? = "no message provided") {
  Log.e(this.javaClass.simpleName + "`~", any.toString())
}
