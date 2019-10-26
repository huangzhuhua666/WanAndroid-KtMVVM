package com.example.hzh.ktmvvm.data.bean

import java.io.Serializable

data class User(
    var admin: Boolean,
    var chapterTops: List<String>,
    var collectIds: List<String>,
    var email: String,
    var icon: String,
    var id: Int,
    var nickname: String,
    var password: String,
    var publicName: String,
    var token: String,
    var type: Int,
    var username: String
) : Serializable