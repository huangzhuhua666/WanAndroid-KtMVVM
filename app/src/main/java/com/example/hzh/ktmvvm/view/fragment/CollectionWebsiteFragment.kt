package com.example.hzh.ktmvvm.view.fragment

import android.view.View
import androidx.lifecycle.Observer
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.base.WanFragment
import com.example.hzh.ktmvvm.data.bean.Website
import com.example.hzh.ktmvvm.databinding.BaseRefreshListBinding
import com.example.hzh.ktmvvm.util.WebsiteDiffCallback
import com.example.hzh.ktmvvm.view.activity.AuthActivity
import com.example.hzh.ktmvvm.view.activity.WebActivity
import com.example.hzh.ktmvvm.viewmodel.CollectionVM
import com.example.hzh.ktmvvm.widget.EditCollectPopup
import com.example.hzh.ktmvvm.widget.EditWebsiteDialog
import com.example.hzh.library.adapter.ItemClickPresenter
import com.example.hzh.library.adapter.SimpleBindingAdapter
import com.example.hzh.library.extension.obtainVM
import com.example.hzh.library.http.APIException
import com.google.android.flexbox.FlexboxLayoutManager
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lxj.xpopup.XPopup
import kotlinx.android.synthetic.main.base_refresh_list.*

/**
 * Create by hzh on 2019/9/26.
 */
class CollectionWebsiteFragment : WanFragment<BaseRefreshListBinding, CollectionVM>() {

    companion object {

        fun newInstance(): CollectionWebsiteFragment = CollectionWebsiteFragment()
    }

    override val mLayoutId: Int
        get() = R.layout.base_refresh_list

    override val mViewModel: CollectionVM?
        get() = obtainVM(CollectionVM::class.java).also { it.flag = 1 }

    private val mAdapter by lazy { SimpleBindingAdapter<Website>(R.layout.item_website) }

    private var mEditWebDialog: EditWebsiteDialog? = null

    override fun initView() {
        mBinding.baseVM = mViewModel

        refreshLayout.setEnableLoadMore(false)

        baseList.apply {
            layoutManager = FlexboxLayoutManager(mContext)
            adapter = mAdapter
        }
    }

    override fun initListener() {
        LiveEventBus.get("dismiss_dialog", Boolean::class.java)
            .observe(viewLifecycleOwner, Observer {
                // 编辑成功，关闭dialog
                mEditWebDialog?.run { if (it && isShowing()) dismiss() }
            })
        LiveEventBus.get("update_collect_website").observe(viewLifecycleOwner, Observer {
            mViewModel?.getInitData(false)
        })

        mViewModel?.run {
            websiteList.observe(viewLifecycleOwner, Observer {
                mAdapter.setNewDiffData(WebsiteDiffCallback(it))
                refreshLayout.finishRefresh()
            })
        }

        mAdapter.mPresenter = object : ItemClickPresenter<Website> {
            override fun onItemClick(view: View, item: Website, position: Int) {
                WebActivity.open(mContext, item.link, item.name)
            }

            override fun onLongClick(view: View, item: Website, position: Int): Boolean {
                XPopup.Builder(mContext)
                    .atView(view)
                    .offsetX(view.width / 2)
                    .offsetY(-view.height / 2)
                    .hasShadowBg(false)
                    .asCustom(EditCollectPopup(mContext) {
                        if (App.isLogin)
                            when (it.id) {
                                R.id.btnEdit -> mEditWebDialog = EditWebsiteDialog(item) { web ->
                                    mViewModel?.collectOrEditWebsite(web)
                                }.apply { show(mContext) } // 编辑网站
                                R.id.btnDelete -> mViewModel?.deleteWebsite(item) // 删除网站
                            }
                        else AuthActivity.open(mContext)
                    }).show()
                return true
            }
        }
    }

    override fun initData() {
        mViewModel?.getInitData(false)
    }

    override fun onLoginExpired(e: APIException) {
        super.onLoginExpired(e)
        AuthActivity.open(mContext)
    }
}