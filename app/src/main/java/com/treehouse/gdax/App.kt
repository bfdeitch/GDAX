package com.treehouse.gdax

import android.app.Application
import android.arch.persistence.room.Room
import com.github.kittinunf.fuel.httpGet
import com.treehouse.gdax.Data.AppDatabase
import com.treehouse.gdax.Data.OpenOrder
import org.json.JSONArray
import org.json.JSONObject
import kotlin.concurrent.thread

val db by lazy {
    App.tempDB!!
}

class App : Application() {
    companion object {
        var candles = mutableListOf<Candle>()
        var selectedGranularityId = 0
        var tempDB: AppDatabase? = null
    }

    override fun onCreate() {
            tempDB = Room.databaseBuilder(this, AppDatabase::class.java, "GDAX").build()
            clearDatabase()
            downloadOrderBook()
            super.onCreate()
    }

    fun clearDatabase() {
        thread {
            with(db.receivedOrdersDao()) { delete(getAll()) }
            with(db.openOrdersDao()) { delete(getAll()) }
            with(db.changeOrdersDao()) { delete(getAll()) }
            with(db.doneOrdersDao()) { delete(getAll()) }
            with(db.matchOrdersDao()) { delete(getAll()) }
        }
    }

    private fun downloadOrderBook() {
        e("DOWNLOAD ORDER BOOK")
        val endpoint = "https://api.gdax.com/products/ETH-USD/book?level=3"
        endpoint.httpGet().responseString { request, response, result ->
            e("ORDER BOOK RETRIEVED")
            result.fold({ data ->
                val json = JSONObject(data)
                val sequence = json.getString("sequence").toLong()
                val bids = json.getJSONArray("bids")
                val asks = json.getJSONArray("asks")
                addOpenOrders("buy", sequence, bids)
                addOpenOrders("sell", sequence, asks)
            }, { error -> e("ERROR: $error") })
        }
    }

    private fun addOpenOrders(side: String, sequence: Long, orders: JSONArray) {
        e("ADDING OPEN ORDERS - NUM ORDERS: ${orders.length()}")
        val openOrders: MutableList<OpenOrder> = mutableListOf()
        (0..orders.length()-1).map {
            val price = orders.getJSONArray(it)[0].toString().toFloat()
            val size = orders.getJSONArray(it)[1].toString().toDouble()
            val order_id = orders.getJSONArray(it)[2] as String
            val event = OpenOrder(sequence, "open", "", order_id, price, size, side)
            openOrders.add(event)
        }
        db.openOrdersDao().insertOrders(openOrders)
        e("DONE INSERTING")
    }
}