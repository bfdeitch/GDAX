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
    val granularity = 60 * 60
    val endpoint = "https://api.gdax.com/products/ETH-USD/candles?granularity=$granularity"
    var candles = listOf<Candle>()
    val greenPaint = Paint()
    val redPaint = Paint()

    init {
        greenPaint.color = green
        greenPaint.style = Paint.Style.STROKE
        greenPaint.strokeWidth = 3f
        redPaint.color = red
        redPaint.strokeWidth = 3f

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

            val subCandles = candles.take(60)

            val realHeight = canvas.height - dip(56) - 40
            val chartWidth = canvas.width - 100
            val candleWidth = chartWidth / subCandles.size.toFloat()
            val highestPrice = subCandles.maxBy { it.high }!!.high
            val lowestPrice = subCandles.minBy { it.low }!!.low

            fun scaledHeight(y: Float) = realHeight - (y - lowestPrice) /  (highestPrice - lowestPrice) * realHeight

            subCandles.forEachIndexed { index, candle ->
                val left = chartWidth - candleWidth * (index + 1f) + 8f
                val right = chartWidth - candleWidth * index * 1f
                val midpoint = left + (right - left) / 2f

                var paint = greenPaint
                var bottom = scaledHeight(candle.open)
                var top = scaledHeight(candle.close)
                if (candle.close < candle.open) {
                    paint = redPaint
                    bottom = scaledHeight(candle.close)
                    top = scaledHeight(candle.open)
                }

                canvas.drawRect(left, top, right, bottom, paint)
                canvas.drawLine(midpoint, top, midpoint, scaledHeight(candle.high), paint)
                canvas.drawLine(midpoint, bottom, midpoint, scaledHeight(candle.low), paint)
            }

            e("highest: $highestPrice, lowest: $lowestPrice, canvas height: ${canvas.height}")
        }
    }
}