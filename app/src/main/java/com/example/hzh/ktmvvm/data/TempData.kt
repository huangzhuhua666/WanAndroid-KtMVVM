package com.example.hzh.ktmvvm.data

import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.ktmvvm.data.bean.Website

/**
 * Create by hzh on 2021/12/9
 */
object TempData {

    val articles = listOf(
        Article().apply {
            articleId = 19509
            type = 1
            fresh = true
            superChapterId = 294
            author = "lelelongwang"
            niceDate = "2021-08-24 00:14"
            envelopePic =
                "https://www.wanandroid.com/blogimgs/61aef3ce-51b7-42b5-b1ac-1c84b2a0d2ea.png"
            title = "WanJetpack项目：用Jetpack实现玩Android，追求更官方的实现方式"
            desc =
                "玩Android 项目。用Jetpack MVVM开发架构、单Activity多Fragment项目设计，项目结构清晰，代码简洁优雅，追求更官方的实现方式。欢迎star，非常感谢。"
            superChapterName = "开源项目主Tab"
            chapterName = "未分类"
            collect = false
        },
        Article().apply {
            articleId = 19508
            type = 0
            fresh = false
            superChapterId = 294
            author = "taxeric"
            niceDate = "2021-08-24 00:13"
            envelopePic =
                "https://www.wanandroid.com/blogimgs/03928418-8d6a-4ba2-8aad-33ef4fc8d0c9.png"
            title = "初步上手Compose：宝可梦图鉴"
            desc = "基于Jetpack Compose框架开发的图鉴程序，API来源：PokeAPI"
            superChapterName = "开源项目主Tab"
            chapterName = "未分类"
            collect = true
        },
        Article().apply {
            articleId = 19476
            type = 0
            fresh = false
            superChapterId = 294
            author = "AndroidBBQ"
            niceDate = "2021-08-19 21:10"
            envelopePic =
                "https://www.wanandroid.com/blogimgs/a25aa26d-dbab-4e0a-a0fc-5a805b4d7d18.png"
            title = "WanJetpack项目：用Jetpack实现玩Android，追求更官方的实现方式"
            desc = "kotin、协程、MVVM、jetpack、组件化、buildSrc、koin、BRAVH、约束布局等"
            superChapterName = "开源项目主Tab"
            chapterName = "完整项目"
            collect = true
        }
    )

    val histories = listOf("代码混淆 安全", "逆向 加固", "自定义View", "动画")

    val hotKeyList = listOf(
        Website().apply { name = "面试" },
        Website().apply { name = "Studio3" },
        Website().apply { name = "动画" },
        Website().apply { name = "自定义View" },
        Website().apply { name = "性能优化 速度" }
    )

    val commonWebList = listOf(
        Website().apply { name = "androidos" },
        Website().apply { name = "androidxref" },
        Website().apply { name = "cs.android" },
        Website().apply { name = "API文档" },
        Website().apply { name = "系统版本" }
    )
}