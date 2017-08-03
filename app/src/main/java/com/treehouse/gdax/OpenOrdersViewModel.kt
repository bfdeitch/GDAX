package com.treehouse.gdax

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.treehouse.gdax.Data.OpenOrder
import com.treehouse.gdax.Data.PriceSideTuple


class OpenOrdersViewModel (app: Application) : AndroidViewModel(app) {
    val openOrders: LiveData<List<PriceSideTuple>> = MutableLiveData<List<PriceSideTuple>>()
        get() {
            if (field.value == null) {
                e("loaded open orders")
                field = db.openOrdersDao().loadOpenOrdersSync()
            }
            return field
        }
}