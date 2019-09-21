package com.example.hzh.ktmvvm.util

import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.hzh.ktmvvm.data.model.BannerBean
import com.example.hzh.ktmvvm.data.model.CategoryBean
import com.example.hzh.library.extension.setListener
import com.example.hzh.library.viewmodel.BaseVM
import com.scwang.smartrefresh.layout.SmartRefreshLayout
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

@BindingAdapter("bind:refreshOrLoadMore")
fun SmartRefreshLayout.refreshOrLoadMore(vm: BaseVM) {
    setListener {
        onRefresh { vm.getInitData() }
        onLoadMore { vm.loadData() }
    }
}

@BindingAdapter("bind:loadWeb")
fun WebView.loadWeb(url: String?) {
    url?.let {
        if (it == "") return
        loadUrl(url)
    }
}

@BindingAdapter("bind:setKnowledgeCategory")
fun TextView.setKnowledgeCategory(childList: List<CategoryBean>) {
    text = childList.joinToString("     ", transform = { child -> child.name })
}