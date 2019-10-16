package com.example.hzh.library.animator.dsl

import android.animation.Animator
import android.animation.ValueAnimator

/**
 * Create by hzh on 2019/08/15.
 */
class ValueAnim : Anim() {

    override var animator: Animator = ValueAnimator()

    private val valueAnimator
        get() = animator as ValueAnimator

    var values: Any? = null
        set(value) {
            field = value
            value?.let {
                valueAnimator.run {
                    when (it) {
                        is IntArray -> setIntValues(*it)
                        is FloatArray -> setFloatValues(*it)
                        is Array<*> -> setObjectValues(*it)
                        else -> throw IllegalArgumentException("unsupported value type")
                    }
                }
            }
        }

    var repeatCount: Int = -1
        set(value) {
            field = value
            valueAnimator.repeatCount = value
        }

    var repeatMode: Int = ValueAnimator.RESTART
        set(value) {
            field = value
            valueAnimator.repeatMode = value
        }

    var action: ((Any) -> Unit)? = null
        set(value) {
            field = value
            valueAnimator.apply {
                removeAllUpdateListeners()
                addUpdateListener { it.animatedValue.run { value?.invoke(this) } }
            }
        }

    override fun reverse() {
        values?.run {
            when (this) {
                is IntArray -> {
                    reverse()
                    valueAnimator.setIntValues(*this)
                }
                is FloatArray -> {
                    reverse()
                    valueAnimator.setFloatValues(*this)
                }
                else -> throw IllegalArgumentException("unsupported value type")
            }
        }
    }
}

fun valueAnim(creation: ValueAnim.() -> Unit) = ValueAnim().apply {
    creation()
    addListener()
}