package com.treehouse.gdax

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.treehouse.gdax.Data.AppDatabase
import okhttp3.*
import java.util.concurrent.TimeUnit

class MyWebSocket : LifecycleObserver {
  val parser = MessageParser(db)

  val listener = object : WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response) {
      webSocket.send("""{"type": "subscribe","product_ids": ["ETH-USD"]}""")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
      //e("MESSAGE: " + text)
      parser.readMessage(text)
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

  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  fun shutDown() {
    webSocket.close(1000, "Goodbye, World!")
    client.dispatcher().executorService().shutdown()
  }
}