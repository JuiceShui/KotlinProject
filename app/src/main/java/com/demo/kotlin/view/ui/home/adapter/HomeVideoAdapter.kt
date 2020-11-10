package com.demo.kotlin.view.ui.home.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.demo.kotlin.R
import com.demo.kotlin.data.entity.TXDouYinVideoEntity
import com.demo.kotlin.databinding.ItemHomeVideoBinding
import com.demo.kotlin.utils.GlideUtils
import com.demo.kotlin.utils.dp2px
import com.demo.kotlin.view.widget.adapter.BaseAdapter

class HomeVideoAdapter(data: List<TXDouYinVideoEntity>, context: Context) :
    BaseAdapter<TXDouYinVideoEntity>(data, context) {
    private lateinit var mBinding: ItemHomeVideoBinding
    override fun setLayId(parent: ViewGroup?, viewType: Int): Int {
        return R.layout.item_home_video
    }

    override fun setViews(viewDataBinding: ViewDataBinding?, position: Int) {
        mBinding = viewDataBinding as ItemHomeVideoBinding
        mData?.let {
            GlideUtils.loadImage(it[position].coverurl, mBinding.ivCover)
            GlideUtils.loadImage(it[position].avatar, mBinding.ivAvatar)
            mBinding.tvAuthor.text = it[position].author
            mBinding.tvSign.text = it[position].signature
            mBinding.tvTitle.text = it[position].title
            if (position == it.size - 1) {
                val params = mBinding.root.layoutParams as ViewGroup.MarginLayoutParams
                params.bottomMargin = context.resources.getDimension(R.dimen.navigation_bar_height).toInt()
                    //context.dp2px(context.resources.getDimension(R.dimen.navigation_bar_height))
                mBinding.root.layoutParams = params
            }
        }
    }
}