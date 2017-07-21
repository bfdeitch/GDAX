package com.treehouse.gdax

import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.recyclerview.v7.recyclerView

class OpenOrdersFragment : LifecycleFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val viewModel = ViewModelProviders.of(this).get(OpenOrdersViewModel::class.java)
        return context.recyclerView {
            val openOrdersAdapter = OpenOrdersAdapter()
            adapter = openOrdersAdapter
            viewModel.openOrders.observe(this@OpenOrdersFragment, Observer {
                // it == List<OpenOrder>
                if (it != null) {
                    val openOrders = it.map { Order(it.side == "buy", it.remaining_size, it.price) }
                    val bids = openOrders.filter { it.isBid }.sortedByDescending { it.price }.take(10)
                    val asks = openOrders.filter { !it.isBid }.sortedBy { it.price }.take(10)
                    val list = mutableListOf<Order>()
                    list.addAll(asks)
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