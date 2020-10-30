package com.demo.kotlin.view.widget.adapter

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Description: ViewHolder的基类
 */
class Holder<T : ViewDataBinding> : RecyclerView.ViewHolder {
    lateinit var binding: T
        private set

    constructor(viewDataBinding: T) : super(viewDataBinding.root) {
        binding = viewDataBinding
    }

    constructor(itemView: View) : super(itemView) {}
}