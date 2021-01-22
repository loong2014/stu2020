package com.sunny.module.account

import com.sunny.lib.common.service.IAccountService

/**
 * Created by zhangxin17 on 2020/12/29
 */
class SunAccountService : IAccountService {

    override fun isLogin(): Boolean {
        return LoginUtils.getUserInfo()?.status != LoginStatus.UnLogin
    }

    override fun doAutoLogin() {
        LoginUtils.instance.doAutoLogin()
    }

    override fun getUserId(): Long {
        return LoginUtils.getUserInfo()?.id ?: -1
    }

    override fun getUserName(): String? {
        return LoginUtils.getUserInfo()?.name
    }
}