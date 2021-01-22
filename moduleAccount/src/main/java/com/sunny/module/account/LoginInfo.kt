package com.sunny.module.account

/**
 * Created by zhangxin17 on 2020/12/29
 */

class LoginStatus {
    companion object {
        const val UnLogin = -1
        const val Login = 1
        const val VIP = 2
    }
}

data class UserModel(var id: Long = -1, var name: String? = null, var status: Int = LoginStatus.UnLogin)