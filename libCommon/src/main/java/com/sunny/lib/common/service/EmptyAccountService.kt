package com.sunny.lib.common.service

/**
 * Created by zhangxin17 on 2020/12/29
 */
class EmptyAccountService : IAccountService {

    override fun isLogin(): Boolean {
        return false
    }

    override fun doAutoLogin() {
    }

    override fun getUserId(): Long {
        return -1
    }

    override fun getUserName(): String? {
        return ""
    }
}