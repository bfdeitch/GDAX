package com.treehouse.gdax

import com.treehouse.gdax.Data.*
import org.json.JSONObject

class MessageParser(val db: AppDatabase) {

  fun readMessage(msg: String) {
    val JSON = JSONObject(msg)
    val type = JSON["type"] as String
    when (type) {
      "change" -> readChangeMessage(JSON)
      "done" -> readDoneMessage(JSON)
      "match" -> readMatchMessage(JSON)
      "open" -> readOpenMessage(JSON)
      "received" -> readReceivedMessage(JSON)
    }
  }

  private fun readChangeMessage(json: JSONObject) {
    val sequence = json["sequence"] as Int
    val time = json["time"] as String
    val order_id = json["order_id"] as String
    val new_size = json["new_size"] as String
    val old_size = json["old_size"] as String
    val price = json["price"] as String
    val side = json["side"] as String
    val event = ChangeOrder(sequence, "change", time, order_id, new_size, old_size, price, side)
    db.changeOrdersDao().insert(event)
  }

  private fun readDoneMessage(json: JSONObject) {
    val sequence = json["sequence"] as Int
    val time = json["time"] as String
    val price = if (json.has("price")) json["price"] as String else ""
    val order_id = json["order_id"] as String
    val reason = json["reason"] as String
    val side = json["side"] as String
    val remaining_size = if (json.has("remaining_size")) json["remaining_size"] as String else ""
    val event = DoneOrder(sequence, "done", time, price, order_id, reason, side, remaining_size)
    db.doneOrdersDao().insert(event)
  }

  private fun readMatchMessage(json: JSONObject) {
    val sequence = json["sequence"] as Int
    val trade_id = json["trade_id"] as Int
    val maker_order_id = json["maker_order_id"] as String
    val taker_order_id = json["taker_order_id"] as String
    val time = json["time"] as String
    val size = json["size"] as String
    val price = json["price"] as String
    val side = json["side"] as String
    val event = MatchOrder(sequence, "match", trade_id, maker_order_id, taker_order_id, time, size, price, side)
    db.matchOrdersDao().insert(event)
  }

  private fun readOpenMessage(json: JSONObject) {
    val sequence = json["sequence"] as Int
    val time = json["time"] as String
    val order_id = json["order_id"] as String
    val price = json["price"] as String
    val remaining_size = json["remaining_size"] as String
    val side = json["side"] as String
    val event = OpenOrder(sequence, "open", time, order_id, price, remaining_size, side)
    db.openOrdersDao().insert(event)

  }

  private fun readReceivedMessage(json: JSONObject) {
    val order_type = json["order_type"] as String
    val size = if (order_type == "limit") json["size"] as String else ""
    val price = if (order_type == "limit") json["price"] as String else ""
    val funds = if (order_type == "limit") "" else json["funds"] as String
    val sequence = json["sequence"] as Int
    val time = json["time"] as String
    val order_id = json["order_id"] as String
    val side = json["side"] as String
    val event = ReceivedOrder(sequence, "received", time, order_id, size, price, funds, side, order_type)
    db.receivedOrdersDao().insert(event)
  }

}