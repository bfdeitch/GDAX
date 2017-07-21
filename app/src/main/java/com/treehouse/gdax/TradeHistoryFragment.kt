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


class TradeHistoryFragment : LifecycleFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val viewModel = ViewModelProviders.of(this).get(TradeHistoryViewModel::class.java)
        return context.recyclerView {
            val tradeHistoryAdapter = TradeHistoryAdapter()
            adapter = tradeHistoryAdapter
            viewModel.trades.observe(this@TradeHistoryFragment, Observer {
                // it == List<MatchOrder>
                if (it != null) {
                    val trades = it.map { Trade(it.side == "sell", it.size, it.price, it.time) }
                    tradeHistoryAdapter.trades.clear()
                    tradeHistoryAdapter.trades.addAll(trades)
                    tradeHistoryAdapter.notifyDataSetChanged()
                }
            })
            val myLayoutManger = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            myLayoutManger.isItemPrefetchEnabled = false
            layoutManager = myLayoutManger
        }
    }
}