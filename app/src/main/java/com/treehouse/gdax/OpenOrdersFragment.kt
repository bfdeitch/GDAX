package com.treehouse.gdax

import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.treehouse.gdax.Data.PriceSideTuple
import org.jetbrains.anko.recyclerview.v7.recyclerView

class OpenOrdersFragment : LifecycleFragment() {
    var recyclerView: RecyclerView? = null
    var updateCt = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        updateCt = 0
        val viewModel = ViewModelProviders.of(this).get(OpenOrdersViewModel::class.java)
        val openOrdersAdapter = OpenOrdersAdapter()
        val myLayoutManger = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView = context.recyclerView {
            adapter = openOrdersAdapter
            viewModel.openOrders.observe(this@OpenOrdersFragment, Observer {
                // it == List<PriceSideTuple>
                if (it != null) {
                    val bids = it.filter { it.side == "buy" }.sortedByDescending { it.price }.take(100)
                    val asks = it.filter { it.side == "sell" }.sortedBy { it.price }.take(100)
                    val list = mutableListOf<PriceSideTuple>()
                    list.addAll(asks.reversed())
                    list.addAll(bids)

                    openOrdersAdapter.openOrders.clear()
                    openOrdersAdapter.openOrders.addAll(list)
                    openOrdersAdapter.notifyDataSetChanged()
                    e("itemcount: ${adapter.itemCount}, updateCt: $updateCt")
                    if (updateCt == 0)
                        myLayoutManger.scrollToPositionWithOffset(adapter.itemCount / 2, context.heightPixels / 2)
                    updateCt++
                }
            })
            myLayoutManger.isItemPrefetchEnabled = false
            layoutManager = myLayoutManger
        }
        return recyclerView!!
    }
}