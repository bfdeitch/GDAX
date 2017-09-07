package com.treehouse.gdax

import android.arch.lifecycle.LifecycleFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ChartFragment : LifecycleFragment() {
    lateinit var chartView: ChartView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        chartView = ChartView(context)
        return chartView
    }
}


