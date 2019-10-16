package com.example.hzh.ktmvvm.util

import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.hzh.ktmvvm.data.bean.Category
import com.example.hzh.library.extension.setListener
import com.example.hzh.library.viewmodel.BaseVM
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.youth.banner.Banner

/**
 * Create by hzh on 2019/09/11.
 */
@BindingAdapter("bind:setImages")
fun Banner.setBannerImages(bannerList: List<com.example.hzh.ktmvvm.data.bean.Banner>?) {
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

@BindingAdapter("bind:noMoreData")
fun SmartRefreshLayout.noMoreData(isNoMoreData: Boolean) {
    setNoMoreData(isNoMoreData)
}

@BindingAdapter("bind:loadWeb")
fun WebView.loadWeb(url: String?) {
    url?.let {
        if (it == "") return
        loadUrl(url)
    }
}

@BindingAdapter("bind:setKnowledgeCategory")
fun TextView.setKnowledgeCategory(childList: List<Category>) {
    text = childList.joinToString("     ", transform = { child -> child.name })
}

@BindingAdapter("bind:setOnSearchListener")
fun EditText.setOnSearchListener(vm: BaseVM) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) vm.getInitData()
        false
    }
}