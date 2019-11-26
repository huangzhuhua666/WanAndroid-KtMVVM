package com.example.hzh.ktmvvm.view.activity

import android.app.Activity
import android.content.Intent
import android.os.SystemClock
import android.view.View
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.adapter.SimplePageAdapter
import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.databinding.ActivityMainBinding
import com.example.hzh.ktmvvm.databinding.DrawerHeadBinding
import com.example.hzh.ktmvvm.view.fragment.*
import com.example.hzh.ktmvvm.viewmodel.AuthVM
import com.example.hzh.library.activity.BaseActivity
import com.example.hzh.library.extension.obtainVM
import com.example.hzh.library.extension.toast
import com.example.hzh.library.widget.dialog.ConfirmDialog
import com.jeremyliao.liveeventbus.LiveEventBus

class MainActivity : BaseActivity<ActivityMainBinding, AuthVM>() {

    companion object {
        const val VIEW_COLLECTION = 0x10
    }

    override val mLayoutId: Int
        get() = R.layout.activity_main

    override val mTitleView: View?
        get() = mBinding.llTitle

    override val mViewModel: AuthVM?
        get() = obtainVM(AuthVM::class.java)

    private val titles by lazy { resources.getStringArray(R.array.main_title) }

    private val mills = LongArray(2)

    private val mHeaderBinding by lazy {
        DataBindingUtil.bind<DrawerHeadBinding>(mBinding.nav.getHeaderView(0))
    }

    private val mLogoutDialog by lazy {
        ConfirmDialog.Builder().run {
            title = getString(R.string.confirm_logout)
            rightClickListener = { mViewModel?.logout() }
            build()
        }
    }

    override fun initView() {
        mHeaderBinding?.run {
            lifecycleOwner = this@MainActivity
            avatar = App.configSP.getString("icon", "")
            nickname = App.configSP.getString("nickname", getString(R.string.visitor))
        }

        mBinding.run {
            vpContent.run {
                listOf(
                    HomeFragment.newInstance(),
                    KnowledgeFragment.newInstance(),
                    NavigationFragment.newInstance(),
                    WeChatAuthorFragment.newInstance(),
                    ProjectFragment.newInstance()
                ).let { fragmentList ->
                    adapter = SimplePageAdapter(
                        supportFragmentManager,
                        lifecycle,
                        fragmentList.size
                    ) { fragmentList[it] }
                    offscreenPageLimit = fragmentList.size
                }

                indicator.viewpager = this
                isUserInputEnabled = false
            }
        }
    }

    override fun initListener() {
        LiveEventBus.get("auth").observe(this, Observer {
            // 登录、退出登录消息，更新用户信息
            mHeaderBinding?.run {
                avatar = App.configSP.getString("icon", "")
                nickname = App.configSP.getString("nickname", getString(R.string.visitor))
            }
        })

        mBinding.run {
            btnDrawer.setOnClickListener { drawer.openDrawer(GravityCompat.START) }

            btnSearch.setOnClickListener { SearchActivity.open(mContext) }

            nav.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.collect -> { // 收藏
                        if (App.isLogin) CollectionActivity.open(mContext)
                        else AuthActivity.open(mContext, VIEW_COLLECTION)
                    }
                    R.id.todo -> TodoActivity.open(mContext) // 待办清单
                    R.id.logout -> if (App.isLogin) mLogoutDialog.show(mContext) // 退出登录
                }
                drawer.closeDrawer(GravityCompat.START)
                true
            }

            indicator.setOnTabChangedListener { mBinding.title = titles[it] }
        }
    }

    override fun initData() {
        mBinding.title = titles[0]
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return

        when (requestCode) {
            VIEW_COLLECTION -> CollectionActivity.open(mContext)
        }
    }

    override fun onBackPressed() {
        System.arraycopy(mills, 1, mills, 0, 1)
        mills[1] = SystemClock.uptimeMillis()

        if (mills[1] - mills[0] < 1000) super.onBackPressed()
        else toast(R.string.click_again_to_exit)
    }
}
