package com.example.hzh.ktmvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.hzh.ktmvvm.data.bean.Guide
import com.example.hzh.ktmvvm.data.model.NavigationModel
import com.example.hzh.library.viewmodel.BaseVM

/**
 * Create by hzh on 2019/11/12.
 */
class NavigationVM : BaseVM() {

    private val navigationModel by lazy { NavigationModel() }

    private val _guideList = MutableLiveData<List<Guide>>()
    val guideList: LiveData<List<Guide>> = _guideList

    override fun getInitData(isRefresh: Boolean) {
        super.getInitData(isRefresh)
        doOnIO(
            tryBlock = { _guideList.postValue(navigationModel.getNavigation()) },
            finallyBlock = {
                _isShowLoading.value = false
                isFinish.value = true
            }
        )
    }
}