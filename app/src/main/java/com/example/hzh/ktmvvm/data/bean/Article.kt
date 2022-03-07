package com.example.hzh.ktmvvm.data.bean

import androidx.databinding.ObservableField
import com.alibaba.fastjson.annotation.JSONField
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
        }
    @Column(ignore = true)
    val oCollect: ObservableField<Boolean> = ObservableField(false)

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
    var shareUser = ""
    var superChapterId = -1
    var superChapterName = ""
    var title = ""
    var type = -1
    var userId = -1
    var visible = -1
    var zan = -1
    var expired = -1L

    fun copy(collect: Boolean) = Article().also {
        it.apkLink = apkLink
        it.audit = audit
        it.author = author
        it.chapterId = chapterId
        it.chapterName = chapterName
        it.collect = collect
        it.courseId = courseId
        it.desc = desc
        it.envelopePic = envelopePic
        it.fresh = fresh
        it.articleId = articleId
        it.link = link
        it.niceDate = niceDate
        it.niceShareDate = niceShareDate
        it.origin = origin
        it.originId = originId
        it.prefix = prefix
        it.projectLink = projectLink
        it.publishTime = publishTime
        it.selfVisible = selfVisible
        it.shareDate = shareDate
        it.shareUser = shareUser
        it.superChapterId = superChapterId
        it.superChapterName = superChapterName
        it.title = title
        it.type = type
        it.userId = userId
        it.visible = visible
        it.zan = zan
        it.expired = expired
    }
}