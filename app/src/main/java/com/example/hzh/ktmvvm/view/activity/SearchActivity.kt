package com.example.hzh.ktmvvm.view.activity

import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.base.WanActivity
import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.ktmvvm.data.bean.Website
import com.example.hzh.ktmvvm.databinding.ActivitySearchBinding
import com.example.hzh.ktmvvm.diff.ArticleDiffCallback
import com.example.hzh.ktmvvm.diff.StringDiffCallback
import com.example.hzh.ktmvvm.diff.WebsiteDiffCallback
import com.example.hzh.ktmvvm.viewmodel.SearchVM
import com.example.hzh.library.adapter.ItemClickPresenter
import com.example.hzh.library.adapter.SimpleBindingAdapter
import com.example.hzh.library.extension.filterFastClickListener
import com.example.hzh.library.http.APIException
import com.google.android.flexbox.FlexboxLayoutManager

/**
 * Create by hzh on 2019/11/11.
 */
class SearchActivity : WanActivity<ActivitySearchBinding, SearchVM>() {

    override val mLayoutId: Int
        get() = R.layout.activity_search

    override val mTitleView: View?
        get() = mBinding.llTitle

    override val mViewModel: SearchVM? by viewModels()

    override val isClickHideKeyboard: Boolean
        get() = true

    private val mHistoryAdapter by lazy { SimpleBindingAdapter<String>(R.layout.item_search_history) }

    private val mHotAdapter by lazy { SimpleBindingAdapter<Website>(R.layout.item_website) }

    private val mCommonAdapter by lazy { SimpleBindingAdapter<Website>(R.layout.item_website) }

    private val mArticleAdapter by lazy { SimpleBindingAdapter<Article>(R.layout.item_article) }

    override fun initView() {
        mBinding.searchVM = mViewModel

        mBinding.let {
            it.rvHistory.run {
                isNestedScrollingEnabled = false
                layoutManager = FlexboxLayoutManager(mContext)
                adapter = mHistoryAdapter
            }

            it.rvHot.run {
                isNestedScrollingEnabled = false
                layoutManager = FlexboxLayoutManager(mContext)
                adapter = mHotAdapter
            }

            it.rvCommon.run {
                isNestedScrollingEnabled = false
                layoutManager = FlexboxLayoutManager(mContext)
                adapter = mCommonAdapter
            }

            it.rvResult.adapter = mArticleAdapter
        }
    }

    override fun initListener() {
        mViewModel?.run {
            historyList.observe(mContext, Observer {
                // 搜索历史
                mHistoryAdapter.setNewDiffData(
                    StringDiffCallback(
                        it
                    )
                )
            })

            hotKeyList.observe(mContext, Observer {
                // 热搜
                mHotAdapter.setNewDiffData(
                    WebsiteDiffCallback(
                        it
                    )
                )
            })

            commonWebList.observe(mContext, Observer {
                // 常用网站
                mCommonAdapter.setNewDiffData(
                    WebsiteDiffCallback(
                        it
                    )
                )
            })

            articleList.observe(mContext, Observer {
                // 搜索结果
                mArticleAdapter.setNewDiffData(
                    ArticleDiffCallback(
                        it
                    )
                )
            })

            mBinding.run {
                btnBack.filterFastClickListener {
                    mViewModel?.run {
                        if (isResult.value!!) {
                            isResult.value = false
                            return@filterFastClickListener
                        } else finish()
                    }
                    finish()
                }

                // 清空搜索栏
                btnClear.filterFastClickListener { keyword.value = "" }
            }

            mHistoryAdapter.mPresenter = object : ItemClickPresenter<String>() {
                override fun onItemClick(view: View, item: String, position: Int) {
                    super.onItemClick(view, item, position)
                    if (isFastClick) return

                    keyword.value = item // 关键词设置到EditText
                    saveHistory(item) // 保存搜索历史
                    getInitData(false) // 搜索
                }
            }

            mHotAdapter.mPresenter = object : ItemClickPresenter<Website>() {
                override fun onItemClick(view: View, item: Website, position: Int) {
                    super.onItemClick(view, item, position)
                    if (isFastClick) return

                    keyword.value = item.name // 关键词设置到EditText
                    saveHistory(item.name) // 保存搜索历史
                    getInitData(false) // 搜索
                }
            }
        }

        mCommonAdapter.mPresenter = object : ItemClickPresenter<Website>() {
            override fun onItemClick(view: View, item: Website, position: Int) {
                super.onItemClick(view, item, position)
                if (isFastClick) return

                WebActivity.open(mContext, item.link, item.name) // 浏览网站
            }
        }

        mArticleAdapter.mPresenter = object : ItemClickPresenter<Article>() {
            override fun onItemClick(view: View, item: Article, position: Int) {
                super.onItemClick(view, item, position)
                if (isFastClick) return

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