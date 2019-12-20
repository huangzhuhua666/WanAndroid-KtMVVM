package com.example.hzh.ktmvvm.view.fragment

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.adapter.LeftGuideAdapter
import com.example.hzh.ktmvvm.adapter.RightGuideAdapter
import com.example.hzh.ktmvvm.data.bean.Guide
import com.example.hzh.ktmvvm.databinding.FragmentNavigationBinding
import com.example.hzh.ktmvvm.view.activity.WebActivity
import com.example.hzh.ktmvvm.viewmodel.NavigationVM
import com.example.hzh.library.adapter.ItemClickPresenter
import com.example.hzh.library.fragment.BaseFragment
import com.google.android.flexbox.FlexboxLayoutManager
import kotlin.math.abs

/**
 * Create by hzh on 2019/11/12.
 */
class NavigationFragment : BaseFragment<FragmentNavigationBinding, NavigationVM>() {

    companion object {

        fun newInstance(): NavigationFragment = NavigationFragment()
    }

    override val mLayoutId: Int
        get() = R.layout.fragment_navigation

    override val mViewModel: NavigationVM? by viewModels()

    private val mLeftAdapter by lazy { LeftGuideAdapter() }

    private val mRightAdapter by lazy {
        RightGuideAdapter { _, item, _ -> WebActivity.open(mContext, item.link, item.title) } // 浏览
    }

    private var isLeft = false
    private var currentIndex = 0

    override fun initView() {
        mBinding.let {
            it.baseVM = mViewModel

            it.rvLeft.adapter = mLeftAdapter

            it.rvRight.run {
                FlexboxLayoutManager(mContext)
                layoutManager = LinearLayoutManager(mContext)
                adapter = mRightAdapter
            }
        }
    }

    override fun initListener() {
        mViewModel?.guideList?.observe(viewLifecycleOwner, Observer {
            mLeftAdapter.setNewData(it)
            mRightAdapter.run { setNewData(it) }
        })

        mBinding.refreshLayout.setOnRefreshListener { mViewModel?.getInitData(true) }

        mLeftAdapter.mPresenter = object : ItemClickPresenter<Guide>() {
            override fun onItemClick(view: View, item: Guide, position: Int) {
                super.onItemClick(view, item, position)
                if (isFastClick) return

                isLeft = true
                currentIndex = position
                mLeftAdapter.changeCheckPosition(position) // 选中
                moveToCenter(mBinding.rvLeft, position) // 选中项移动到中间

                (mBinding.rvRight.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                    position,
                    0
                )
            }
        }

        mBinding.rvRight.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isLeft) { // 点击左边导航，不需要这里监听处理
                    isLeft = false
                    return
                }

                val first =
                    (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                rightMoveLeft(first)
            }
        })
    }

    override fun initData() {
        mViewModel?.getInitData(false)
    }

    /**
     * 移动RecyclerView的选中项的中间，使用的LayoutManager需要是LinearLayoutManager或其子类
     */
    private fun moveToCenter(rv: RecyclerView, position: Int) {
        rv.run {
            val firstItem = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            val lastItem = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

            val middle = (firstItem + lastItem) / 2 // 中间位置
            val difIndex = abs(position - middle)

            if (difIndex >= childCount) scrollToPosition(position)
            else {
                if (position < middle) scrollBy(0, -getChildAt(difIndex).top)
                else scrollBy(0, getChildAt(difIndex).top)
            }
        }
    }

    private fun rightMoveLeft(position: Int) {
        if (position == currentIndex) return // 位置相同的就不移动了

        currentIndex = position
        mLeftAdapter.changeCheckPosition(position)
        moveToCenter(mBinding.rvLeft, position)
    }
}