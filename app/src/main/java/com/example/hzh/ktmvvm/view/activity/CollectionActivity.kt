package com.example.hzh.ktmvvm.view.activity

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.adapter.SimplePageAdapter
import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.base.WanActivity
import com.example.hzh.ktmvvm.databinding.ActivityCollectionBinding
import com.example.hzh.ktmvvm.view.fragment.*
import com.example.hzh.ktmvvm.viewmodel.CollectionVM
import com.example.hzh.ktmvvm.widget.AddCollectPopup
import com.example.hzh.ktmvvm.widget.EditArticleDialog
import com.example.hzh.ktmvvm.widget.EditWebsiteDialog
import com.example.hzh.library.extension.obtainVM
import com.google.android.material.tabs.TabLayoutMediator
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lxj.xpopup.XPopup
import kotlinx.android.synthetic.main.activity_collection.*

/**
 * Create by hzh on 2019/9/25.
 */
class CollectionActivity : WanActivity<ActivityCollectionBinding, CollectionVM>() {

    companion object {

        fun open(activity: Activity) {
            activity.startActivity(Intent(activity, CollectionActivity::class.java))
        }
    }

    override val mLayoutId: Int
        get() = R.layout.activity_collection

    override val mTitleView: View?
        get() = llTitle

    override val mViewModel: CollectionVM?
        get() = obtainVM(CollectionVM::class.java)

    private val titleList by lazy {
        listOf(
            getString(R.string.article),
            getString(R.string.website)
        )
    }

    private val mMenuWindow by lazy {
        XPopup.Builder(mContext)
            .atView(btnAdd)
            .offsetX(14)
            .offsetY(-36)
            .hasShadowBg(false)
            .asCustom(AddCollectPopup(mContext) {
                if (App.isLogin)
                    when (it.id) {
                        R.id.btnArticle -> mEditArticleDialog = EditArticleDialog { article ->
                            mViewModel?.collectOuter(article)
                        }.apply { show(mContext) } // 收藏站外文章
                        R.id.btnWebsite -> mEditWebDialog = EditWebsiteDialog { web ->
                            mViewModel?.collectOrEditWebsite(web)
                        }.apply { show(mContext) } // 收藏网站
                    }
                else AuthActivity.open(mContext)
            })
    }

    private var mEditArticleDialog: EditArticleDialog? = null

    private var mEditWebDialog: EditWebsiteDialog? = null

    override fun initView() {
        listOf(
            CollectionArticleFragment.newInstance(),
            CollectionWebsiteFragment.newInstance()
        ).let { fragmentList ->
            vpContent?.adapter = SimplePageAdapter(
                supportFragmentManager, lifecycle,
                fragmentList.size
            ) { fragmentList[it] }
        }

        TabLayoutMediator(tabLayout, vpContent) { tab, position ->
            tab.text = titleList[position]
        }.attach()
    }

    override fun initListener() {
        LiveEventBus.get("dismiss_dialog", Boolean::class.java).observe(mContext, Observer {
            mEditArticleDialog?.run { if (it && isShowing()) dismiss() }
            mEditWebDialog?.run { if (it && isShowing()) dismiss() }
        })

        btnBack.setOnClickListener { finish() }

        btnAdd.setOnClickListener { mMenuWindow.show() }
    }

    override fun initData() {

    }
}