package com.demo.kotlin.view.ui.home.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.demo.kotlin.R
import com.demo.kotlin.data.entity.TXDouYinVideoEntity
import com.demo.kotlin.utils.GlideUtils
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder

class HomeAdapter : BaseBannerAdapter<TXDouYinVideoEntity, HomeHolder>() {
    override fun createViewHolder(parent: ViewGroup, itemView: View?, viewType: Int): HomeHolder {
        return HomeHolder(itemView)
    }

    override fun onBind(
        holder: HomeHolder?,
        data: TXDouYinVideoEntity?,
        position: Int,
        pageSize: Int
    ) {
        holder?.let {
            it.bindData(data, position, pageSize)
        }
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_home_banner
    }
}

class HomeHolder(itemView: View?) : BaseViewHolder<TXDouYinVideoEntity>(itemView!!) {
    override fun bindData(data: TXDouYinVideoEntity?, position: Int, pageSize: Int) {
        val ivBanner = findView<ImageView>(R.id.iv_banner)
        val ivAvatar = findView<ImageView>(R.id.iv_avatar)
        val tvTitle = findView<TextView>(R.id.tv_title)
        val tvAuthor = findView<TextView>(R.id.tv_author)
        data?.let {
            GlideUtils.loadImage(data.coverurl, ivBanner)
            GlideUtils.loadImage(data.avatar, ivAvatar)
            tvTitle.text = data.title
            tvAuthor.text = data.author
        }
    }
}