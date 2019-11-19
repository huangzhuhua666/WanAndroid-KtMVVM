package com.example.hzh.ktmvvm.data.model

import com.example.hzh.ktmvvm.data.bean.Guide
import com.example.hzh.ktmvvm.data.repository.NavigationRepository

/**
 * Create by hzh on 2019/11/12.
 */
class NavigationModel {

    private val repo by lazy { NavigationRepository.getInstance() }

    suspend fun getNavigation(): List<Guide> = repo.getNavigation()
}