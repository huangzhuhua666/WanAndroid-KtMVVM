package com.example.hzh.ktmvvm.util

import android.graphics.Color
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.hzh.ktmvvm.data.bean.Category
import com.example.hzh.library.extension.setListener
import com.example.hzh.library.viewmodel.BaseVM
import com.example.hzh.library.widget.RTextView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.youth.banner.Banner
import java.util.*

/**
 * Create by hzh on 2019/09/11.
 */

/**
 * 设置Banner
 */
@BindingAdapter("bind:setImages")
fun Banner.setBannerImages(bannerList: List<com.example.hzh.ktmvvm.data.bean.Banner>?) {
    setBannerTitles(bannerList?.map {
        HtmlCompat.fromHtml(it.title, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
    })
    bannerList?.map { it.imagePath }?.let {
        setImageLoader(GlideLoader())
        update(it)
    }
}

/**
 * 网络图片加载
 * @param image 图片url
 * @param src 占位图、加载失败图
 */
@BindingAdapter("bind:loadImage", "android:src", requireAll = false)
fun ImageView.loadImage(image: String, src: Int) {
    if (image == "") return
    Glide.with(context)
        .load(image)
        .placeholder(src)
        .error(src)
        .thumbnail(.5f)
        .apply {
            diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            skipMemoryCache(false)
            dontAnimate()
            fitCenter()
        }
        .into(this)
}

/**
 * SmartRefreshLayout事件驱动
 */
@BindingAdapter("bind:refreshOrLoadMore")
fun SmartRefreshLayout.refreshOrLoadMore(vm: BaseVM) {
    setListener {
        onRefresh { vm.getInitData(true) }
        onLoadMore { vm.loadData() }
    }
}

@BindingAdapter("bind:isFinishGetData")
fun SmartRefreshLayout.isFinishGetData(isFinish: Boolean) {
    if (isFinish) {
        finishRefresh()
        finishLoadMore()
    }
}

/**
 * 设置已无更多数据
 */
@BindingAdapter("bind:noMoreData")
fun SmartRefreshLayout.noMoreData(isNoMoreData: Boolean) {
    setNoMoreData(isNoMoreData)
}

/**
 * h5网页加载
 * @param url url
 */
@BindingAdapter("bind:loadWeb")
fun WebView.loadWeb(url: String?) {
    url?.let {
        if (it == "") return
        if (!it.startsWith("http")) loadUrl("https://$it")
        else loadUrl(url)
    }
}

/**
 * 知识体系分类文字格式化
 */
@BindingAdapter("bind:setKnowledgeCategory")
fun TextView.setKnowledgeCategory(childList: List<Category>) {
    text = childList.joinToString("     ", transform = { child -> child.name })
}

/**
 * EditText点击搜索按钮
 */
@BindingAdapter("bind:setOnSearchListener")
fun EditText.setOnSearchListener(vm: BaseVM) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) vm.getInitData(false)
        false
    }
}

/**
 * 随机字体颜色
 * @param bound rbg边界 0 ~ bound - 1
 */
@BindingAdapter("bind:randomTextColor")
fun TextView.randomTextColor(bound: Int) {
    Color.rgb(Random().nextInt(bound), Random().nextInt(bound), Random().nextInt(bound)).let {
        if (this is RTextView) normalTextColor = it
        else setTextColor(it)
    }
}