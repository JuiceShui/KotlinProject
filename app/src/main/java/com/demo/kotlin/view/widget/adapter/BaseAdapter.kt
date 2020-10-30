package com.demo.kotlin.view.widget.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import java.util.*

abstract class BaseAdapter<T>(data: List<T>?, context: Context) :
    RecyclerView.Adapter<Holder<ViewDataBinding>>() {
    protected var mData: List<T>? = ArrayList()
    var context: Context
        protected set
    protected var mOnItemClickListener: OnItemClickListener? = null
    protected var mInflater: LayoutInflater
    protected var mItemOnLongClickListener: onItemLongClickListener? = null
    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder<ViewDataBinding> {
        val layId = setLayId(parent, viewType)
        if (layId == 0) {
            try {
                throw Exception("warning , please set layout first!")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        val viewDataBinding =
            DataBindingUtil.inflate<ViewDataBinding>(mInflater, layId, parent, false)
        return Holder(viewDataBinding)
    }

    override fun onBindViewHolder(holder: Holder<ViewDataBinding>, position: Int) {
        // 设置点击事件
        holder.itemView.setOnClickListener(View.OnClickListener {
            if (mOnItemClickListener != null) {
                mOnItemClickListener!!.onItemClickListener(holder.itemView, position)
            }
        })
        holder.itemView.setOnLongClickListener(OnLongClickListener {
            if (mItemOnLongClickListener != null) {
                mItemOnLongClickListener!!.onLongClick(holder.itemView, position)
                return@OnLongClickListener true
            }
            false
        })
        setViewWithHolder(holder, position)
        // 这下面继续写
        setViews(holder.binding, position)
        // 处理消息刷新时 item 闪烁的问题
        holder.binding.executePendingBindings()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return mData!!.size
    }

    fun setViewWithHolder(holder: RecyclerView.ViewHolder?, position: Int) {}

    /**
     * 点击事件
     */
    interface OnItemClickListener {
        fun onItemClickListener(view: View?, position: Int)
    }

    /**
     * 设置layout
     *
     * @param parent   父容器
     * @param viewType view的类别
     * @return
     */
    abstract fun setLayId(parent: ViewGroup?, viewType: Int): Int

    /**
     * 渲染view
     *
     * @param viewDataBinding binding对象
     * @param position        对应位置
     */
    abstract fun setViews(viewDataBinding: ViewDataBinding?, position: Int)

    /**
     * 设置点击事件
     *
     * @param onItemClickListener listener
     */
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        mOnItemClickListener = onItemClickListener
    }

    interface onItemLongClickListener {
        fun onLongClick(view: View?, position: Int)
    }

    fun setOnItemLongClickListener(longClickListener: onItemLongClickListener?) {
        mItemOnLongClickListener = longClickListener
    }

    init {
        if (data != null) {
            mData = data
        }
        mData = data
        this.context = context
        mInflater = LayoutInflater.from(this.context)
    }
}
