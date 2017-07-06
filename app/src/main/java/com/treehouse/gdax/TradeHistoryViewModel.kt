package com.treehouse.gdax

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.treehouse.gdax.Data.MatchOrder

class TradeHistoryViewModel(app: Application) : AndroidViewModel(app) {
    val trades: LiveData<List<MatchOrder>> = MutableLiveData<List<MatchOrder>>()
    get() {
        if (field.value == null) {
            e("loaded trade history")
            field = db.matchOrdersDao().loadMatchedOrdersSync()
        }
        return field
    }
}