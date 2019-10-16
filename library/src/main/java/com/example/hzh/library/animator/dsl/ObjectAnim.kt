package com.example.hzh.library.animator.dsl

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator

/**
 * Create by hzh on 2019/08/15.
 */
class ObjectAnim : Anim() {

    companion object {
        private const val TRANSLATION_X = "translationX"
        private const val TRANSLATION_Y = "translationY"
        private const val SCALE_X = "scaleX"
        private const val SCALE_Y = "scaleY"
        private const val ALPHA = "alpha"
        private const val ROTATION = "rotation"
        private const val ROTATION_X = "rotationX"
        private const val ROTATION_Y = "rotationY"
    }

    override var animator: Animator = ObjectAnimator()

    private val objectAnimator
        get() = animator as ObjectAnimator

    var repeatCount: Int = -1
        set(value) {
            field = value
            objectAnimator.repeatCount = value
        }

    var repeatMode: Int = ValueAnimator.RESTART
        set(value) {
            field = value
            objectAnimator.repeatMode = value
        }

    var translationX: FloatArray? = null
        set(value) {
            field = value
            translationX?.run {
                PropertyValuesHolder.ofFloat(TRANSLATION_X, *this)?.run {
                    valuesHolder[TRANSLATION_X] = this
                    objectAnimator.setValues(*valuesHolder.values.toTypedArray())
                }
            }
        }

    var translationY: FloatArray? = null
        set(value) {
            field = value
            translationY?.run {
                PropertyValuesHolder.ofFloat(TRANSLATION_Y, *this)?.run {
                    valuesHolder[TRANSLATION_Y] = this
                    objectAnimator.setValues(*valuesHolder.values.toTypedArray())
                }
            }
        }

    var scaleX: FloatArray? = null
        set(value) {
            field = value
            scaleX?.run {
                PropertyValuesHolder.ofFloat(SCALE_X, *this)?.run {
                    valuesHolder[SCALE_X] = this
                    objectAnimator.setValues(*valuesHolder.values.toTypedArray())
                }
            }
        }

    var scaleY: FloatArray? = null
        set(value) {
            field = value
            scaleY?.run {
                PropertyValuesHolder.ofFloat(SCALE_Y, *this)?.run {
                    valuesHolder[SCALE_Y] = this
                    objectAnimator.setValues(*valuesHolder.values.toTypedArray())
                }
            }
        }

    var alpha: FloatArray? = null
        set(value) {
            field = value
            alpha?.run {
                PropertyValuesHolder.ofFloat(ALPHA, *this)?.run {
                    valuesHolder[ALPHA] = this
                    objectAnimator.setValues(*valuesHolder.values.toTypedArray())
                }
            }
        }

    var rotation: FloatArray? = null
        set(value) {
            field = value
            rotation?.run {
                PropertyValuesHolder.ofFloat(ROTATION, *this)?.run {
                    valuesHolder[ROTATION] = this
                    objectAnimator.setValues(*valuesHolder.values.toTypedArray())
                }
            }
        }

    var rotationX: FloatArray? = null
        set(value) {
            field = value
            rotationX?.run {
                PropertyValuesHolder.ofFloat(ROTATION_X, *this)?.run {
                    valuesHolder[ROTATION_X] = this
                    objectAnimator.setValues(*valuesHolder.values.toTypedArray())
                }
            }
        }

    var rotationY: FloatArray? = null
        set(value) {
            field = value
            rotationY?.run {
                PropertyValuesHolder.ofFloat(ROTATION_Y, *this)?.run {
                    valuesHolder[ROTATION_Y] = this
                    objectAnimator.setValues(*valuesHolder.values.toTypedArray())
                }
            }
        }

    var target: Any? = null
        set(value) {
            field = value
            objectAnimator.target = target
        }

    private var valuesHolder = mutableMapOf<String, PropertyValuesHolder>()

    override fun reverse() {
        valuesHolder.forEach {
            when (it.key) {
                TRANSLATION_X -> translationX?.run {
                    reverse()
                    it.value.setFloatValues(*this)
                }
                TRANSLATION_Y -> translationY?.run {
                    reverse()
                    it.value.setFloatValues(*this)
                }
                SCALE_X -> scaleX?.run {
                    reverse()
                    it.value.setFloatValues(*this)
                }
                SCALE_Y -> scaleY?.run {
                    reverse()
                    it.value.setFloatValues(*this)
                }
                ALPHA -> alpha?.run {
                    reverse()
                    it.value.setFloatValues(*this)
                }
                ROTATION -> rotation?.run {
                    reverse()
                    it.value.setFloatValues(*this)
                }
                ROTATION_X -> rotationX?.run {
                    reverse()
                    it.value.setFloatValues(*this)
                }
                ROTATION_Y -> rotationY?.run {
                    reverse()
                    it.value.setFloatValues(*this)
                }
            }
        }
    }

    fun setPropertyValueHolder() {
        translationX?.run { PropertyValuesHolder.ofFloat(TRANSLATION_X, *this) }?.run { valuesHolder[TRANSLATION_X] = this }
        translationY?.run { PropertyValuesHolder.ofFloat(TRANSLATION_Y, *this) }?.run { valuesHolder[TRANSLATION_Y] = this }
        scaleX?.run { PropertyValuesHolder.ofFloat(SCALE_X, *this) }?.run { valuesHolder[SCALE_X] = this }
        scaleY?.run { PropertyValuesHolder.ofFloat(SCALE_Y, *this) }?.run { valuesHolder[SCALE_Y] = this }
        alpha?.run { PropertyValuesHolder.ofFloat(ALPHA, *this) }?.run { valuesHolder[ALPHA] = this }
        rotation?.run { PropertyValuesHolder.ofFloat(ROTATION, *this) }?.run { valuesHolder[ROTATION] = this }
        rotationX?.run { PropertyValuesHolder.ofFloat(ROTATION_X, *this) }?.run { valuesHolder[ROTATION_X] = this }
        rotationY?.run { PropertyValuesHolder.ofFloat(ROTATION_Y, *this) }?.run { valuesHolder[ROTATION_Y] = this }
        objectAnimator.setValues(*valuesHolder.values.toTypedArray())
    }
}

fun objectAnim(creation: ObjectAnim.() -> Unit) = ObjectAnim().apply {
    creation()
    addListener()
}