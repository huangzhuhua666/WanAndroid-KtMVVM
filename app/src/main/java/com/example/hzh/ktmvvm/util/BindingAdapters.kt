package com.example.hzh.ktmvvm.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.hzh.ktmvvm.data.model.BannerBean
import com.youth.banner.Banner

/**
 * Create by hzh on 2019/09/11.
 */
@BindingAdapter("bind:setImages")
fun Banner.setBannerImages(bannerList: List<BannerBean>?) {
    bannerList?.map { it.title }?.let { setBannerTitles(it) }
    bannerList?.map { it.imagePath }?.let {
        setImageLoader(GlideLoader())
        setImages(it)
        start()
    }
}

@BindingAdapter("bind:loadImage")
fun ImageView.loadImage(image: String) {
    if (image == "") return
    Glide.with(context)
        .load(image)
        .thumbnail(.5f)
        .apply {
            diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            skipMemoryCache(false)
            dontAnimate()
            fitCenter()
        }
        .into(this)
}