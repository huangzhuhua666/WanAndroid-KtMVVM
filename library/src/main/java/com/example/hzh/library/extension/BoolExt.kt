package com.example.hzh.library.extension

/**
 * Create by hzh on 2020/4/2.
 */
sealed class BoolExt<out T>

object No : BoolExt<Nothing>()
class Data<T>(val data: T) : BoolExt<T>()

inline fun <T> Boolean.yes(block: () -> T): BoolExt<T> = when {
    this -> Data(block.invoke())
    else -> No
}

inline fun <T> BoolExt<T>.no(block: () -> T) = when (this) {
    is No -> block.invoke()
    is Data -> data
}