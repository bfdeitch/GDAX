package com.treehouse.gdax

import com.github.kittinunf.fuel.*
import org.json.JSONObject
import org.junit.Test

class OrderBookAPITest {
    val endpoint = "https://api.gdax.com/products/ETH-USD/book?level=3"

    @Test
    fun runTest() {
        val (request, response, result) = endpoint.httpGet().responseString()
        println("TEST")
        result.fold({ data ->
            val json = JSONObject(data)
            val sequence = json["sequence"]
            val bids = json.getJSONArray("bids")
            val asks = json.getJSONArray("asks")
            println(sequence)
            println(bids)
            (0..asks.length()).map {
                println(asks.getJSONArray(it)[0])
                println(asks[it])
            }
        }, { error ->
            println(error)
        })
        assert(true)
    }
}