package com.example.hzh.ktmvvm.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.ktmvvm.data.bean.Category
import com.example.hzh.ktmvvm.data.network.ProjectApi
import com.example.hzh.library.viewmodel.BaseVM
import kotlin.properties.Delegates

/**
 * Create by hzh on 2019/9/21.
 */
class ProjectVM : BaseVM() {

    private val service by lazy { App.httpClient.getService(ProjectApi::class.java) }

    var context by Delegates.notNull<Context>()

    var cid by Delegates.notNull<Int>()

    val treeList = MutableLiveData<List<Category>>()

    val articleList = MutableLiveData<List<Article>>()

    fun getProjectTree() {
        doOnIO(
            tryBlock = {
                treeList.postValue(
                    listOf(Category(name = context.getString(R.string.fresh_project))).plus(
                        service.getProjectTree()
                    )
                )
            },
            catchBlock = { e -> e.printStackTrace() }
        )
    }

    override fun getInitData() {
        isLoadMore = false
        doOnIO(
            tryBlock = {
                articleList.postValue(
                    when (cid) {
                        -1 -> {
                            pageNo = 1
                            service.getNewProject(pageNo).datas
                        }
                        else -> {
                            pageNo = 0
                            service.getProject(pageNo, cid).datas
                        }
                    }
                )
            },
            catchBlock = { e -> e.printStackTrace() }
        )
    }

    override fun loadData() {
        super.loadData()
        doOnIO(
            tryBlock = {
                articleList.postValue(
                    when (cid) {
                        -1 -> service.getNewProject(pageNo).datas
                        else -> service.getProject(pageNo, cid).datas
                    }
                )
            },
            catchBlock = { e -> e.printStackTrace() }
        )
    }
}