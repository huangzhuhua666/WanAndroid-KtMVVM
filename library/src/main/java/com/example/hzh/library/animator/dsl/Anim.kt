package com.example.hzh.library.animator.dsl

import android.animation.*
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnCancel
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnRepeat
import androidx.core.animation.doOnStart

/**
 * Create by hzh on 2019/08/15.
 */
private typealias AnimListener = (Animator) -> Unit

abstract class Anim {

    abstract var animator: Animator

    var isReverse: Boolean = false

    var duration: Long
        get() = 300L
        set(value) {
            animator.duration = value
        }

    var interpolator: Interpolator
        get() = LinearInterpolator()
        set(value) {
            animator.interpolator = value
        }

    var delay: Long
        get() = 0L
        set(value) {
            animator.startDelay = value
        }

    var evaluator: TypeEvaluator<*>?
        get() = null
        set(value) {
            value?.let {
                when (animator) {
                    is ValueAnimator -> (animator as ValueAnimator).setEvaluator(it)
                    is ObjectAnimator -> (animator as ObjectAnimator).setEvaluator(it)
                }
            }
        }

    var builder: AnimatorSet.Builder? = null

    open fun start() {
        if (animator.isRunning) animator.cancel()
        if (isReverse) reverse()
        animator.start()
    }

    open fun cancel() {
        if (animator.isRunning) animator.cancel()
    }

    abstract fun reverse()

    var onStart: AnimListener? = null
    var onRepeat: AnimListener? = null
    var onEnd: AnimListener? = null
    var onCancel: AnimListener? = null

    internal fun addListener() {
        animator.run {
            doOnStart { onStart?.invoke(it) }
            doOnRepeat { onRepeat?.invoke(it) }
            doOnEnd { onEnd?.invoke(it) }
            doOnCancel { onCancel?.invoke(it) }
        }
    }
}