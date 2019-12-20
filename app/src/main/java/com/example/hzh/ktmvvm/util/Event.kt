package com.example.hzh.ktmvvm.util

/**
 * Create by hzh on 2019/12/20.
 */
object Event {

    /**
     * 登录、注册、退出登陆相关事件
     */
    const val AUTH = "auth"

    /**
     * 文章取消收藏事件
     */
    const val ARTICLE_CANCEL_COLLECT = "article_cancel_collect"

    /**
     * 收藏站外文章事件
     */
    const val COLLECTION_ARTICLE_UPDATE = "collection_article_update"

    /**
     * 收藏网站事件
     */
    const val COLLECTION_WEBSITE_UPDATE = "collection_website_update"

    /**
     * 关闭dialog事件
     */
    const val DIALOG_DISMISS = "dialog_dismiss"

    /**
     * 待办事项删除事件
     */
    const val TODO_DELETE = "todo_delete"

    /**
     * 待办事项添加、编辑事件
     */
    const val TODO_SAVE = "todo_save"

    /**
     * 待办事项状态更新事件
     */
    const val TODO_STATUS = "todo_status"
}