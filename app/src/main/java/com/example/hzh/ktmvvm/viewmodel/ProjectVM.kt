package com.example.hzh.ktmvvm.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.data.model.ArticleBean
import com.example.hzh.ktmvvm.data.model.CategoryBean
import com.example.hzh.ktmvvm.data.network.ProjectApi
import com.example.hzh.library.viewmodel.BaseVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

/**
 * Create by hzh on 2019/9/21.
 */
class ProjectVM : BaseVM() {

    private val service by lazy { App.httpClient.getService(ProjectApi::class.java) }

    var context by Delegates.notNull<Context>()

    var cid by Delegates.notNull<Int>()

    val treeList = MutableLiveData<List<CategoryBean>>()

    val articleList = MutableLiveData<List<ArticleBean>>()

    fun getProjectTree() {
        launch(Dispatchers.IO) {
            try {
                treeList.postValue(
                    listOf(CategoryBean(name = context.getString(R.string.fresh_project)))
                        .plus(service.getProjectTree())
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun getInitData() {
        isLoadMore = false
        launch(Dispatchers.IO) {
            try {
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
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun loadData() {
        super.loadData()
        launch(Dispatchers.IO) {
            try {
                articleList.postValue(
                    when (cid) {
                        -1 -> service.getNewProject(pageNo).datas
                        else -> service.getProject(pageNo, cid).datas
                    }
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}