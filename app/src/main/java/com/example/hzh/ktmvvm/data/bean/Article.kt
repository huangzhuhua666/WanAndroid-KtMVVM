package com.example.hzh.ktmvvm.data.bean

import androidx.databinding.ObservableField
import com.alibaba.fastjson.annotation.JSONField
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport

/**
 * Create by hzh on 2019/09/12.
 */
class Article : LitePalSupport() {

    var apkLink = ""
    var audit = -1
    var author = ""
    var chapterId = -1
    var chapterName = ""

    var collect = false
        set(value) {
            field = value
            oCollect.set(value)
            _flowCollect.value = value
        }
    @Column(ignore = true)
    val oCollect: ObservableField<Boolean> = ObservableField(false)

    private val _flowCollect = MutableStateFlow(collect)
    val flowCollect: StateFlow<Boolean>
        get() = _flowCollect

    var courseId = -1
    var desc = ""
    var envelopePic = ""
    var fresh = false

    @JSONField(name = "id")
    @Column(unique = true)
    var articleId = -1
    var link = ""
    var niceDate = ""
    var niceShareDate = ""
    var origin = ""
    var originId = -1
    var prefix = ""
    var projectLink = ""
    var publishTime = -1L
    var selfVisible = -1

    var shareDate = ""
        set(value) {
            field = value ?: ""
        }

    var shareUser = ""
    var superChapterId = -1
    var superChapterName = ""
    var title = ""
    var type = -1
    var userId = -1
    var visible = -1
    var zan = -1
    var expired = -1L
}