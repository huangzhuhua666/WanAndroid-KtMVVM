package com.example.hzh.ktmvvm.view.activity

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.base.WanActivity
import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.ktmvvm.data.bean.Website
import com.example.hzh.ktmvvm.databinding.ActivitySearchBinding
import com.example.hzh.ktmvvm.util.StringDiffCallback
import com.example.hzh.ktmvvm.util.WebsiteDiffCallback
import com.example.hzh.ktmvvm.viewmodel.SearchVM
import com.example.hzh.library.adapter.ItemClickPresenter
import com.example.hzh.library.adapter.SingleBindingAdapter
import com.example.hzh.library.extension.obtainVM
import com.example.hzh.library.http.APIException
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_search.refreshLayout

/**
 * Create by hzh on 2019/11/11.
 */
class SearchActivity : WanActivity<ActivitySearchBinding, SearchVM>() {

    companion object {

        fun open(activity: Activity) {
            activity.startActivity(Intent(activity, SearchActivity::class.java))
        }
    }

    override val mLayoutId: Int
        get() = R.layout.activity_search

    override val mTitleView: View?
        get() = llTitle

    override val mViewModel: SearchVM?
        get() = obtainVM(SearchVM::class.java)

    override val isClickHideKeyboard: Boolean
        get() = true

    private val mHistoryAdapter by lazy { SingleBindingAdapter<String>(R.layout.item_search_history) }

    private val mHotAdapter by lazy { SingleBindingAdapter<Website>(R.layout.item_website) }

    private val mCommonAdapter by lazy { SingleBindingAdapter<Website>(R.layout.item_website) }

    private val mArticleAdapter by lazy { SingleBindingAdapter<Article>(R.layout.item_article) }

    override fun initView() {
        mBinding.searchVM = mViewModel

        rvHistory.run {
            isNestedScrollingEnabled = false
            layoutManager = FlexboxLayoutManager(mContext)
            adapter = mHistoryAdapter
        }

        rvHot.run {
            isNestedScrollingEnabled = false
            layoutManager = FlexboxLayoutManager(mContext)
            adapter = mHotAdapter
        }

        rvCommon.run {
            isNestedScrollingEnabled = false
            layoutManager = FlexboxLayoutManager(mContext)
            adapter = mCommonAdapter
        }

        rvResult.adapter = mArticleAdapter
    }

    override fun initListener() {
        mViewModel?.run {
            historyList.observe(mContext, Observer {
                // 搜索历史
                mHistoryAdapter.setNewDiffData(StringDiffCallback(it))
            })

            hotKeyList.observe(mContext, Observer {
                // 热搜
                mHotAdapter.setNewDiffData(WebsiteDiffCallback(it))
            })

            commonWebList.observe(mContext, Observer {
                // 常用网站
                mCommonAdapter.setNewDiffData(WebsiteDiffCallback(it))
            })

            articleList.observe(mContext, Observer {
                // 搜索结果
                when (isLoadMore) {
                    false -> mArticleAdapter.setNewData(it)
                    true -> {
                        mArticleAdapter.addData(it)
                        refreshLayout.finishLoadMore()
                    }
                }
            })

            btnBack.setOnClickListener {
                mViewModel?.run {
                    if (isResult.value!!) {
                        isResult.value = false
                        return@setOnClickListener
                    } else finish()
                }
                finish()
            }

            // 清空搜索栏
            btnClear.setOnClickListener { keyword.value = "" }

            mHistoryAdapter.mPresenter = object : ItemClickPresenter<String> {
                override fun onItemClick(view: View, item: String) {
                    keyword.value = item // 关键词设置到EditText
                    saveHistory(item) // 保存搜索历史
                    getInitData(false) // 搜索
                }
            }

            mHotAdapter.mPresenter = object : ItemClickPresenter<Website> {
                override fun onItemClick(view: View, item: Website) {
                    keyword.value = item.name // 关键词设置到EditText
                    saveHistory(item.name) // 保存搜索历史
                    getInitData(false) // 搜索
                }
            }
        }

        mCommonAdapter.mPresenter = object : ItemClickPresenter<Website> {
            override fun onItemClick(view: View, item: Website) {
                WebActivity.open(mContext, item.link, item.name) // 浏览网站
            }
        }

        mArticleAdapter.mPresenter = object : ItemClickPresenter<Article> {
            override fun onItemClick(view: View, item: Article) {
                when (view.id) {
                    R.id.cvRoot -> WebActivity.open(mContext, item.link, item.title) // 浏览文章
                    R.id.btnCollect -> {
                        if (App.isLogin) mViewModel?.collectOrNot(item) // 收藏
                        else AuthActivity.open(mContext)
                    }
                }
            }
        }
    }

    override fun initData() {
        mViewModel?.run {
            getHistory()
            getHotKey()
            getCommonWebsite()
        }
    }

    override fun onLoginExpired(e: APIException) {
        super.onLoginExpired(e)
        AuthActivity.open(mContext)
    }

    override fun onBackPressed() {
        mViewModel?.run {
            if (isResult.value!!) {
                isResult.value = false
                return
            } else super.onBackPressed()
        }
        super.onBackPressed()
    }
}