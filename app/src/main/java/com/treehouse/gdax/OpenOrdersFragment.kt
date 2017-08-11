package com.treehouse.gdax

import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.treehouse.gdax.Data.PriceSideTuple
import org.jetbrains.anko.recyclerview.v7.recyclerView

class OpenOrdersFragment : LifecycleFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val viewModel = ViewModelProviders.of(this).get(OpenOrdersViewModel::class.java)
        return context.recyclerView {
            val openOrdersAdapter = OpenOrdersAdapter()
            adapter = openOrdersAdapter
            viewModel.openOrders.observe(this@OpenOrdersFragment, Observer {
                // it == List<PriceSideTuple>
                if (it != null) {
                    val bids = it.filter { it.side == "buy" }.sortedByDescending { it.price }.take(10)
                    val asks = it.filter { it.side == "sell" }.sortedBy { it.price }.take(10)
                    val list = mutableListOf<PriceSideTuple>()
                    list.addAll(asks.reversed())
                    list.addAll(bids)

                    openOrdersAdapter.openOrders.clear()
                    openOrdersAdapter.openOrders.addAll(list)
                    openOrdersAdapter.notifyDataSetChanged()
                }
            })
            val myLayoutManger = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            myLayoutManger.isItemPrefetchEnabled = false
            layoutManager = myLayoutManger
        }
    }
}