package com.example.hzh.library.animator.dsl

import android.animation.Animator
import android.animation.AnimatorSet

/**
 * Create by hzh on 2019/08/15.
 */
class AnimSet : Anim() {

    override var animator: Animator = AnimatorSet()

    private val animatorSet
        get() = animator as AnimatorSet

    private val animList by lazy { mutableListOf<Anim>() }

    fun valueAnim(creation: ValueAnim.() -> Unit): Anim = ValueAnim().apply {
        creation()
        addListener()
        animList.add(this)
    }

    fun objectAnim(creation: ObjectAnim.() -> Unit): Anim = ObjectAnim().apply {
        creation()
        setPropertyValueHolder()
        addListener()
        animList.add(this)
    }

    override fun start() {
        if (animatorSet.isRunning) cancel()
        animList.takeIf { isReverse }?.forEach { it.reverse() }
        if (animList.size == 1) animatorSet.play(animList.first().animator)
        animatorSet.start()
        isReverse = false
    }

    override fun cancel() {
        if (animatorSet.isRunning) animatorSet.cancel()
    }

    operator fun get(index: Int): Anim? =
        animList.takeIf { index in 0..animList.size }?.run { this[index] }

    override fun reverse() {
        if (animatorSet.isRunning) cancel()
        animList.takeIf { !isReverse }?.forEach { it.reverse() }
        animatorSet.start()
        isReverse = true
    }

    infix fun Anim.with(anim: Anim): Anim {
        if (builder == null) builder = animatorSet.play(animator).with(anim.animator)
        else builder!!.with(anim.animator)
        return anim
    }

    infix fun Anim.before(anim: Anim): Anim {
        animatorSet.play(animator).before(anim.animator).run { builder = this }
        return anim
    }

    infix fun Anim.after(anim: Anim): Anim {
        animatorSet.play(animator).after(anim.animator).run { builder = this }
        return anim
    }
}

fun animSet(creation: AnimSet.() -> Unit) = AnimSet().apply {
    creation()
    addListener()
}