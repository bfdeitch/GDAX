package com.treehouse.gdax

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.getAs
import org.json.JSONObject
import org.junit.Test


class ChartDataTest {
    val granularity = 15 * 60
    val endpoint = "https://api.gdax.com/products/ETH-USD/candles?granularity=$granularity"

    data class Candle(val time:Long, val low:Float, val high:Float, val open:Float, val close:Float, val volume:Double)

    @Test
    fun runTest() {
        val (request, response, result) = endpoint.httpGet().responseString()
        result.fold({ data ->
            //do something with data
            println("Data: $data")
            val cleanedData = data.removePrefix("[[").removeSuffix("]]")
            cleanedData.split("],[").map {
                val a = it.split(",")
                Candle(a[0].toLong(), a[1].toFloat(), a[2].toFloat(), a[3].toFloat(), a[4].toFloat(), a[5].toDouble())
            }
        }, { err ->
            //do something with error
            println("Error: $err")
        })
    }
}