package com.example.hzh.ktmvvm.widget

import android.content.Context
import android.util.AttributeSet
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.youth.banner.Banner

/**
 * Create by hzh on 2019/09/11.
 */
class ObsBanner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : Banner(context, attrs, defStyle), LifecycleObserver {

    var isPlayOnStart = true

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        if (isPlayOnStart) startAutoPlay()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        if (!isPlayOnStart) startAutoPlay()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        if (!isPlayOnStart) stopAutoPlay()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        if (isPlayOnStart) stopAutoPlay()
    }
}