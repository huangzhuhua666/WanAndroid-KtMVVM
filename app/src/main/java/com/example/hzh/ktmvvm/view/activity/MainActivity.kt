package com.example.hzh.ktmvvm.view.activity

import android.app.Activity
import android.content.Intent
import android.os.SystemClock
import android.view.View
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.content.edit
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.compose.main.MainTitle
import com.example.hzh.ktmvvm.databinding.ActivityMainBinding
import com.example.hzh.ktmvvm.databinding.DrawerHeadBinding
import com.example.hzh.ktmvvm.util.Event
import com.example.hzh.ktmvvm.view.fragment.*
import com.example.hzh.ktmvvm.viewmodel.MainVM
import com.example.hzh.ktmvvm.work.UploadCrashDemoCrash
import com.example.hzh.library.activity.BaseActivity
import com.example.hzh.library.adapter.SimplePageAdapter
import com.example.hzh.library.extension.newFragment
import com.example.hzh.library.extension.startActivity
import com.example.hzh.library.extension.toast
import com.example.hzh.library.widget.dialog.ConfirmDialog
import com.jeremyliao.liveeventbus.LiveEventBus
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity<ActivityMainBinding, MainVM>() {

    companion object {
        const val VIEW_COLLECTION = 0x10
        const val VIEW_TODO = 0x11
    }

    override val mLayoutId: Int
        get() = R.layout.activity_main

    override val mTitleView: View
        get() = mBinding.composeTitle

    override val mViewModel: MainVM by viewModels()

    private val titles by lazy { resources.getStringArray(R.array.main_title) }

    private val mills = LongArray(2)

    private val mHeaderBinding by lazy {
        DataBindingUtil.bind<DrawerHeadBinding>(mBinding.nav.getHeaderView(0))
    }

    private val mLogoutDialog by lazy {
        ConfirmDialog.Builder().run {
            title = getString(R.string.confirm_logout)
//            rightClickListener = { mViewModel.logout() }
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
            composeTitle.setContent {
                val title by mViewModel.title.collectAsState()
                MainTitle(
                    title = title,
                    onDrawerClick = {
                        drawer.openDrawer(GravityCompat.START)
                    },
                    onSearchClick = {
                        startActivity<SearchActivity>()
                    }
                )
            }

            vpContent.run {
                listOf(
                    newFragment<HomeFragment>(),
                    newFragment<KnowledgeFragment>(),
                    newFragment<NavigationFragment>(),
                    newFragment<WeChatAuthorFragment>(),
                    newFragment<ProjectFragment>()
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
        LiveEventBus.get(Event.AUTH).observe(this) {
            // 登录、退出登录消息，更新用户信息
            mHeaderBinding?.run {
                avatar = App.configSP.getString("icon", "")
                nickname = App.configSP.getString("nickname", getString(R.string.visitor))
            }
        }

        mBinding.run {
            nav.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.collect -> { // 收藏
                        if (App.isLogin) startActivity<CollectionActivity>()
                        else AuthActivity.open(mContext, VIEW_COLLECTION)
                    }
                    R.id.todo -> { // 待办清单
                        if (App.isLogin) startActivity<TodoActivity>()
                        else AuthActivity.open(mContext, VIEW_TODO)
                    }
                    R.id.logout -> if (App.isLogin) mLogoutDialog.show(mContext) // 退出登录
                }
                drawer.closeDrawer(GravityCompat.START)
                true
            }

            indicator.setOnTabChangedListener { mViewModel.updateTitle(titles[it]) }
        }
    }

    override fun initData() {
        mViewModel.updateTitle(titles[0])

        if (App.configSP.getBoolean("is_first_in", true)) {
            WorkManager.getInstance(mContext).run {
                cancelAllWork()

                enqueue(PeriodicWorkRequest.Builder(
                    UploadCrashDemoCrash::class.java,
                    24L, // 任务执行周期
                    TimeUnit.HOURS
                ).setConstraints(
                    Constraints.Builder().run {
                        // 执行任务的约束条件
                        setRequiresBatteryNotLow(true) // 电量不能过低
                        setRequiredNetworkType(NetworkType.CONNECTED) // 有网络时才执行
                        build()
                    }
                ).build())
            }

            App.configSP.edit { putBoolean("is_first_in", false) }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return

        when (requestCode) {
            VIEW_COLLECTION -> startActivity<CollectionActivity>()
            VIEW_TODO -> startActivity<TodoActivity>()
        }
    }

    override fun onBackPressed() {
        System.arraycopy(mills, 1, mills, 0, 1)
        mills[1] = SystemClock.uptimeMillis()

        if (mills[1] - mills[0] < 1000) super.onBackPressed()
        else toast(R.string.click_again_to_exit)
    }
}
