package com.example.hzh.ktmvvm.util

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
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

@Suppress("UNCHECKED_CAST")
@BindingAdapter("bind:firstData")
fun RecyclerView.setFirstData(data: List<Any>?) {
    data?.let { (adapter as BaseQuickAdapter<Any, BaseViewHolder>).setNewData(it) }
}

@Suppress("UNCHECKED_CAST")
@BindingAdapter("bind:loadData")
fun RecyclerView.setLoadData(data: List<Any>?) {
//    data?.let { (adapter as BaseQuickAdapter<Any, BaseViewHolder>).addData(it.) }
}