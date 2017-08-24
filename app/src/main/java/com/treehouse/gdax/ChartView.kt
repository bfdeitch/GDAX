package com.treehouse.gdax

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.annotation.UiThread
import android.view.View
import android.widget.TextView
import com.github.kittinunf.fuel.httpGet
import org.jetbrains.anko.*
import kotlin.concurrent.thread

data class Candle(val time:Long, val low:Float, val high:Float, val open:Float, val close:Float, val volume:Double)
class ChartView(context: Context) : View(context) {
    val granularity = 15 * 60
    val endpoint = "https://api.gdax.com/products/ETH-USD/candles?granularity=$granularity"
    var candles = listOf<Candle>()
    val greenPaint = Paint()
    val redPaint = Paint()

    init {
        greenPaint.color = green
        greenPaint.style = Paint.Style.FILL
        redPaint.color = red

        endpoint.httpGet().responseString { request, response, result ->
            result.fold({ data ->
                //do something with data
                val cleanedData = data.removePrefix("[[").removeSuffix("]]")
                candles = cleanedData.split("],[").map {
                    val a = it.split(",")
                    Candle(a[0].toLong(), a[1].toFloat(), a[2].toFloat(), a[3].toFloat(), a[4].toFloat(), a[5].toDouble())
                }
                e("CANDLES!!!!")
                context.runOnUiThread { invalidate() }
            }, { err ->

            })
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        e("ON DRAW: ${candles.size}")

        // time, low, high, open, close, volume
        if (candles.isNotEmpty()) {
            val candleWidth = canvas.width / (candles.size.toFloat())
            val highestPrice = candles.maxBy { it.high }!!.high
            val lowestPrice = candles.minBy { it.low }!!.low

            candles.forEachIndexed { index, candle ->
                val left = canvas.width - candleWidth * (index + 1f)
                val right = canvas.width - candleWidth * index * 1f
                val bottom = canvas.height - (candle.low - lowestPrice) / (highestPrice - lowestPrice) * canvas.height
                val top = canvas.height - (candle.high - lowestPrice) / (highestPrice - lowestPrice) * canvas.height
                canvas.drawRect(left, top, right, bottom, greenPaint)
            }

            e("highest: $highestPrice, lowest: $lowestPrice, canvas height: ${canvas.height}")
        }
    }
}