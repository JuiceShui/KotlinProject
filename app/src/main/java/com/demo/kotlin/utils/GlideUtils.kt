package com.demo.kotlin.utils

import android.annotation.SuppressLint
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.demo.kotlin.R

object GlideUtils {

    @SuppressLint("CheckResult")
    fun loadImage(
        url: String,
        view: ImageView,
        defaultImage: Int = R.mipmap.ic_loading
    ) {
        val option = RequestOptions()
        option.placeholder(defaultImage)
        option.dontAnimate()
        option.error(defaultImage)
        option.diskCacheStrategy(DiskCacheStrategy.ALL)
        Glide.with(view.context)
            .applyDefaultRequestOptions(option)
            .load(url)
            .into(view)
    }

    @SuppressLint("CheckResult")
    fun loadRoundImage(url: String, view: ImageView, defaultImage: Int = R.mipmap.ic_loading) {
        val option = RequestOptions()
        option.placeholder(defaultImage)
        option.dontAnimate()
        option.centerCrop()
        option.error(defaultImage)
        option.apply(RequestOptions.bitmapTransform(CircleCrop()))
        option.diskCacheStrategy(DiskCacheStrategy.ALL)
        Glide.with(view.context)
            .applyDefaultRequestOptions(option)
            .load(url)
            .into(view)
    }

    fun loadLocalImage(res: Int, view: ImageView) {
        Glide.with(view.context)
            .load(res)
            .into(view)
    }
}