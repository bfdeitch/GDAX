package com.treehouse.gdax

import com.github.kittinunf.fuel.*
import com.github.kittinunf.result.*
import org.junit.Test

class APITest {
    val endpoint = "https://api.gdax.com/products/ETH-USD/book?level=2"

    @Test
    fun runTest() {
        endpoint.httpGet().responseString { request, response, result ->
            result.fold({ data ->
                data
            }, { error ->
                println(error)
            })
            println(request)
            println(response)
            println(result)
        }
        assert(true)
    }
}