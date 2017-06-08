package com.treehouse.gdax

import android.arch.persistence.room.Room
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import okhttp3.*
import org.jetbrains.anko.button
import org.jetbrains.anko.onClick
import org.jetbrains.anko.relativeLayout
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
  val db = Room.databaseBuilder(this, AppDatabase::class.java, "GDAX").build()
  val listener = object : WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response) {
      webSocket.send("""{"type": "subscribe","product_ids": ["ETH-USD"]}""")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
      e("MESSAGE: " + text)
      val JSON = JSONObject(text)
      val type = JSON["type"] as String
      if (type == "open") {
        val sequence = JSON["sequence"] as Int
        val time = JSON["time"] as String
        val order_id = JSON["order_id"] as String
        val price = JSON["price"] as String
        val remaining_size = JSON["remaining_size"] as String
        val side = JSON["side"] as String
        val event = OpenOrder(sequence, type, time, order_id, price, remaining_size, side)
        db.openOrdersDao().insert(event)
        e("$sequence, $type, $time, $order_id, $price, $remaining_size, $side")
      }
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
      e("CLOSE: $code $reason")
    }
    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
      e("FAILURE------------------------------------------------------------")
      e(t)
      e(t.stackTrace)
      t.printStackTrace()
      e(response)
    }
  }
  val client = OkHttpClient.Builder()
      .readTimeout(0, TimeUnit.MILLISECONDS)
      .build()

  val request = Request.Builder()
      .url("wss://ws-feed.gdax.com")
      .build()

  val webSocket = client.newWebSocket(request, listener)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    thread {
      with(db.openOrdersDao()) {
        this.delete(this.getAll())
      }
    }
    relativeLayout {
      button {
        onClick {
          thread {
            db.openOrdersDao().getAll().forEach {
              e(it)
            }
          }
        }
      }
    }
  }

  override fun onDestroy() {
    webSocket.close(1000, "Goodbye, World!");
    client.dispatcher().executorService().shutdown();
    super.onDestroy()
  }
}
