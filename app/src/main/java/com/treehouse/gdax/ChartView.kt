package com.treehouse.gdax

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.format.DateFormat
import android.view.View
import com.github.kittinunf.fuel.httpGet
import org.jetbrains.anko.*
import java.util.*

data class Candle(val time:Long, val low:Float, val high:Float, val open:Float, val close:Float, val volume:Double)
class ChartView(context: Context) : View(context) {
    var candles = listOf<Candle>()
    val greenPaint = Paint()
    val redPaint = Paint()
    val whitePaint = Paint()

    init {
        greenPaint.color = green
        greenPaint.style = Paint.Style.STROKE
        greenPaint.strokeWidth = 3f
        redPaint.color = red
        redPaint.strokeWidth = 3f
        whitePaint.color = Color.WHITE
        whitePaint.textSize = 36f

        resetCandles(60 * 60)
    }

    fun resetCandles(granularity: Int) {
        val endpoint = "https://api.gdax.com/products/ETH-USD/candles?granularity=$granularity"
        endpoint.httpGet().responseString { request, response, result ->
            result.fold({ data ->
                //do something with data
                val cleanedData = data.removePrefix("[[").removeSuffix("]]")
                candles = cleanedData.split("],[").map {
                    val a = it.split(",")
                    Candle(a[0].toLong(), a[1].toFloat(), a[2].toFloat(), a[3].toFloat(), a[4].toFloat(), a[5].toDouble())
                }
                context.runOnUiThread { invalidate() }
            }, { err ->

            })
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // time, low, high, open, close, volume
        if (candles.isNotEmpty()) {

            val subCandles = candles.take(60)

            val realHeight = canvas.height - dip(56) - dip(16) // 56dp is BottomNavigationView height
            val chartWidth = canvas.width - 100
            val candleWidth = chartWidth / subCandles.size.toFloat()
            val highestPrice = subCandles.maxBy { it.high }!!.high
            val lowestPrice = subCandles.minBy { it.low }!!.low

            fun scaledHeight(price: Float) = realHeight - (price - lowestPrice) /  (highestPrice - lowestPrice) * realHeight
            fun drawCandles() {
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

                    if (index % 12 == 11 || index == 0) {
                        val dateStr = getDate(candle.time)
                        canvas.drawText(dateStr, left, canvas.height.toFloat() - dip(56) - dip(4), whitePaint)
                    }
                }
            }
            fun drawPriceLabels() {
                val xPos = chartWidth + dip(8).toFloat()
                val startingSkip = ((highestPrice - lowestPrice) / 5f).toInt()
                val skip = (startingSkip downTo 5).firstOrNull { it % 5 == 0 } ?: 5
                e("starting: $startingSkip, skip: $skip")

                (lowestPrice.toInt()..highestPrice.toInt()).forEach {
                    if (it % skip == 0) {
                        canvas.drawText("$it", xPos, scaledHeight(it.toFloat()), whitePaint)
                    }
                }
            }

            drawCandles()
            drawPriceLabels()
        }
    }

    private fun getDate(time: Long): String {
        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = time*1000
        val date = DateFormat.format("MMM d", cal).toString()
        return date
    }

}