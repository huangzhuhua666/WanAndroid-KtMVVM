package com.example.hzh.ktmvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.ktmvvm.data.bean.Website
import com.example.hzh.ktmvvm.data.model.ArticleModel
import com.example.hzh.ktmvvm.data.model.WebsiteModel
import com.example.hzh.ktmvvm.util.OperateCallback
import com.example.hzh.library.viewmodel.BaseVM
import com.jeremyliao.liveeventbus.LiveEventBus
import kotlin.properties.Delegates

/**
 * Create by hzh on 2019/9/26.
 */
class CollectionVM : BaseVM() {

    private val articleModel by lazy { ArticleModel() }
    private val websiteModel by lazy { WebsiteModel() }

    var flag by Delegates.notNull<Int>()

    private val _articleList = MutableLiveData<List<Article>>()
    val articleList: LiveData<List<Article>> = _articleList

    private val _websiteList = MutableLiveData<List<Website>>()
    val websiteList: LiveData<List<Website>> = _websiteList

    override fun getInitData(isRefresh: Boolean) {
        pageNo = 0
        doOnIO(
            tryBlock = {
                _isShowLoading.postValue(true)
                if (flag == 0) articleModel.getCollections(pageNo).run {
                    // 获取收藏文章
                    _articleList.postValue(datas)
                    _isOver.postValue(over)
                }
                else _websiteList.postValue(websiteModel.getCollectWebsite()) // 获取收藏网站列表
            },
            finallyBlock = {
                _isShowLoading.value = false
                isFinish.value = true
            }
        )
    }

    override fun loadData() {
        super.loadData()
        doOnIO(
            tryBlock = {
                articleModel.getCollections(pageNo).run {
                    // 获取收藏文章
                    _articleList.postValue(_articleList.value?.plus(datas))
                    _isOver.postValue(over)
                }
            },
            catchBlock = { --pageNo },
            finallyBlock = { isFinish.value = true }
        )
    }

    /**
     * 收藏站外文章
     * @param article
     */
    fun collectOuter(article: Article) {
        doOnIO(
            tryBlock = {
                articleModel.collectOuter(article, object : OperateCallback<Article> {
                    override fun onInputIllegal(tip: Int) = _toastTip.postValue(tip)

                    override fun onPreOperate() = _isShowLoading.postValue(true)

                    override fun onCallback(data: Article) {
                        LiveEventBus.get("dismiss_dialog").post(true)
                        LiveEventBus.get("update_collect_article").post(true)
                        _toastTip.postValue(R.string.collect_success)
                    }
                })
            },
            finallyBlock = { _isShowLoading.value = false }
        )
    }

    /**
     * 取消收藏文章
     * @param article 文章
     */
    fun unCollect(article: Article) {
        _isShowLoading.value = true
        doOnIO(
            tryBlock = {
                articleModel.unCollectMyCollection(article.articleId, article.originId).also {
                    _toastTip.postValue(R.string.uncollect_success)
                    LiveEventBus.get("uncollect").post(false)
                    getInitData(false)
                }
            },
            finallyBlock = { _isShowLoading.value = false }
        )
    }

    /**
     * 收藏、编辑网站
     * @param website websiteId为-1时是添加网站
     */
    fun collectOrEditWebsite(website: Website) {
        doOnIO(
            tryBlock = {
                val callback = object : OperateCallback<Website> {
                    override fun onInputIllegal(tip: Int) = _toastTip.postValue(tip)

                    override fun onPreOperate() = _isShowLoading.postValue(true)

                    override fun onCallback(data: Website) {
                        LiveEventBus.get("dismiss_dialog").post(true)
                        LiveEventBus.get("update_collect_website").post(true)

                        _toastTip.postValue(if (website.websiteId == -1) R.string.collect_success else R.string.operate_success)
                    }
                }

                if (website.websiteId == -1) websiteModel.collectWebsite(website, callback) // 添加网站
                else websiteModel.editWebsite(website, callback) // 编辑网站
            },
            finallyBlock = { _isShowLoading.value = false }
        )
    }

    /**
     * 删除收藏网站
     * @param website
     */
    fun deleteWebsite(website: Website) {
        _isShowLoading.value = true
        doOnIO(
            tryBlock = {
                websiteModel.deleteWebsite(website.websiteId).also {
                    _toastTip.postValue(R.string.uncollect_success)
                    getInitData(false)
                }
            },
            finallyBlock = { _isShowLoading.value = false }
        )
    }
}