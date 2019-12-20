package com.example.hzh.ktmvvm.view.activity

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.adapter.SimplePageAdapter
import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.base.WanActivity
import com.example.hzh.ktmvvm.databinding.ActivityCollectionBinding
import com.example.hzh.ktmvvm.util.Event
import com.example.hzh.ktmvvm.view.fragment.CollectionArticleFragment
import com.example.hzh.ktmvvm.view.fragment.CollectionWebsiteFragment
import com.example.hzh.ktmvvm.viewmodel.CollectionVM
import com.example.hzh.ktmvvm.widget.AddCollectPopup
import com.example.hzh.ktmvvm.widget.EditArticleDialog
import com.example.hzh.ktmvvm.widget.EditWebsiteDialog
import com.example.hzh.library.extension.filterFastClickListener
import com.google.android.material.tabs.TabLayoutMediator
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lxj.xpopup.XPopup

/**
 * Create by hzh on 2019/9/25.
 */
class CollectionActivity : WanActivity<ActivityCollectionBinding, CollectionVM>() {

    companion object {

        fun open(activity: Activity) =
            activity.let { it.startActivity(Intent(it, CollectionActivity::class.java)) }
    }

    override val mLayoutId: Int
        get() = R.layout.activity_collection

    override val mTitleView: View?
        get() = mBinding.llTitle

    override val mViewModel: CollectionVM? by viewModels()

    private val titleList by lazy {
        listOf(
            getString(R.string.article),
            getString(R.string.website)
        )
    }

    private val mMenuWindow by lazy {
        XPopup.Builder(mContext)
            .atView(mBinding.btnAdd)
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
        mBinding.run {
            listOf(
                CollectionArticleFragment.newInstance(),
                CollectionWebsiteFragment.newInstance()
            ).let { fragmentList ->
                vpContent.adapter = SimplePageAdapter(
                    supportFragmentManager, lifecycle,
                    fragmentList.size
                ) { fragmentList[it] }
            }

            TabLayoutMediator(tabLayout, vpContent) { tab, position ->
                tab.text = titleList[position]
            }.attach()
        }
    }

    override fun initListener() {
        LiveEventBus.get(Event.DIALOG_DISMISS, Boolean::class.java).observe(mContext, Observer {
            mEditArticleDialog?.run { if (it && isShowing()) dismiss() }
            mEditWebDialog?.run { if (it && isShowing()) dismiss() }
        })

        mBinding.run {
            btnBack.filterFastClickListener { finish() }

            btnAdd.filterFastClickListener { mMenuWindow.show() }
        }
    }

    override fun initData() {

    }
}