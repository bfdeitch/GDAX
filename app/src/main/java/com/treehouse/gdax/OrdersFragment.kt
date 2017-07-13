package com.treehouse.gdax

import android.arch.lifecycle.LifecycleFragment
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.textView

class OrdersFragment : LifecycleFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return context.frameLayout {
            textView {
                text = "Hello Fragments!"
            }
        }
    }
}