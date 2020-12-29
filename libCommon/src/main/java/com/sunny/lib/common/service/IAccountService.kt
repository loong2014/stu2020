package com.sunny.lib.common.service

/**
 * Created by zhangxin17 on 2020/12/29
 */
interface IAccountService {
    fun isLogin(): Boolean
    fun doAutoLogin()
    fun getUserId(): Long
    fun getUserName(): String?
}