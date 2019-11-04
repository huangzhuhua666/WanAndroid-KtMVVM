package com.example.hzh.ktmvvm.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.youth.banner.loader.ImageLoader

/**
 * Create by hzh on 2019/09/11.
 */
class GlideLoader : ImageLoader() {

    override fun displayImage(context: Context, path: Any, imageView: ImageView) {
        Glide.with(context)
            .load(path)
            .apply {
                diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                skipMemoryCache(false)
                dontAnimate()
                fitCenter()
            }
            .into(imageView)
    }
}