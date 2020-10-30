package com.demo.kotlin.view.widget.adapter

import android.view.View
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView

/**
 * Description:
 */
class HeaderAndFooterWrapper(innerAdapter: BaseAdapter<Any>) :
    RecyclerView.Adapter<Holder<ViewDataBinding>>() {
    private val mInnerAdapter: BaseAdapter<Any>

    // 保存 header 和 footer 容器
    private val mHeaderViews = SparseArrayCompat<View?>()
    private val mFooterViews = SparseArrayCompat<View?>()

    // 提供的公共方法
    fun addHeaderView(view: View?) {
        mHeaderViews.put(mHeaderViews.size() + HEADER_TYPE_BEGIN, view)
    }

    val innerAdapter: BaseAdapter<Any>
        get() = mInnerAdapter

    // 提供的公共方法
    fun addHeaderView(view: View?, index: Int) {
        var index = index
        if (index > mHeaderViews.size()) {
            index = mHeaderViews.size()
        }
        mHeaderViews.put(index + HEADER_TYPE_BEGIN, view)
    }

    val headerCount: Int
        get() = mHeaderViews.size()

    fun removeHeaderView(index: Int) {
        mHeaderViews.delete(index + HEADER_TYPE_BEGIN)
    }

    fun addFootView(view: View?) {
        mFooterViews.put(mFooterViews.size() + FOOTER_TYPE_BEGIN, view)
    }

    fun removeFooter(tag: Any) {
        var index = -1
        for (i in 0 until mFooterViews.size()) {
            val v = mFooterViews[FOOTER_TYPE_BEGIN + i]
                ?: continue
            if (tag === v.tag) {
                index = FOOTER_TYPE_BEGIN + i
                break
            }
        }
        if (index != -1) {
            mFooterViews.delete(index)
        }
    }

    fun getFooterBtTag(tag: Any): View? {
        var view: View? = null
        for (i in 0 until mFooterViews.size()) {
            val v = mFooterViews[FOOTER_TYPE_BEGIN + i]
                ?: continue
            if (tag === v.tag) {
                view = v
                break
            }
        }
        return view
    }

    // 创建Item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder<ViewDataBinding> {
        if (mHeaderViews[viewType] != null) {
            return Holder(mHeaderViews[viewType]!!)
        } else if (mFooterViews[viewType] != null) {
            return Holder(mFooterViews[viewType]!!)
        }
        // 如果不是 header 和 footer 交给其内部的 adapter 去处理
        return mInnerAdapter.onCreateViewHolder(parent, viewType)
    }

    // 给Item 赋值 渲染View
    override fun onBindViewHolder(holder: Holder<ViewDataBinding>, position: Int) {
        if (isHeaderPosition(position) || isFooterPosition(position)) {
            return
        }
        mInnerAdapter.onBindViewHolder(holder, position - mHeaderViews.size())
    }

    // 返回有多少count
    override fun getItemCount(): Int {
        return mHeaderViews.size() + mFooterViews.size() + mInnerAdapter.getItemCount()
    }

    // 数据渲染的时候： 会先根据这里返回的type 在走 onCreateViewHolder 方法
    override fun getItemViewType(position: Int): Int {
        if (isHeaderPosition(position)) {
            return mHeaderViews.keyAt(position)
        } else if (isFooterPosition(position)) {
            // 减去 header 和 内部adapter 的数据 就是 footer 的数量了
            return mFooterViews.keyAt(position - mHeaderViews.size() - innerCount)
        }
        return mInnerAdapter.getItemViewType(position - mHeaderViews.size())
    }

    // 这两个方法暂时还不清楚用法
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        WrapperUtils.onAttachedToRecyclerView(
            mInnerAdapter,
            recyclerView, object : WrapperUtils.SpanSizeCallback {
                override fun getSpanSize(
                    layoutManager: GridLayoutManager,
                    oldLookup: SpanSizeLookup,
                    position: Int
                ): Int {
                    val viewType = getItemViewType(position)
                    if (mHeaderViews[viewType] != null) {
                        return layoutManager.spanCount
                    } else if (mFooterViews[viewType] != null) {
                        return layoutManager.spanCount
                    }
                    return oldLookup?.getSpanSize(position) ?: 1
                }
            })
    }

    override fun onViewAttachedToWindow(holder: Holder<ViewDataBinding>) {
        super.onViewAttachedToWindow(holder)
        val position: Int = holder.getLayoutPosition()
        if (isHeaderPosition(position) || isFooterPosition(position)) {
            WrapperUtils.setFullSpan(holder)
        }
    }

    // 根据position 判断是不是属于 footer 或者 header
    private fun isHeaderPosition(position: Int): Boolean {
        return position < mHeaderViews.size()
    }

    private fun isFooterPosition(position: Int): Boolean {
        return position >= mHeaderViews.size() + innerCount
    }

    // 得到内部 adapter 的数据数量
    private val innerCount: Int
        private get() = mInnerAdapter.getItemCount()

    companion object {
        // header 和 footer 开始 type 值
        private const val HEADER_TYPE_BEGIN = 100000
        private const val FOOTER_TYPE_BEGIN = 200000
    }

    init {
        mInnerAdapter = innerAdapter
    }
}