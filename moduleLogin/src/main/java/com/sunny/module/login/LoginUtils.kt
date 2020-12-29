package com.sunny.module.login

/**
 * Created by zhangxin17 on 2020/12/29
 */
class LoginUtils private constructor() {

    companion object {
        val instance = SingletonHolder.holder

        fun getUserInfo(): UserModel? {
            return instance.mUserModel
        }


    }

    var mUserModel: UserModel? = null

    private object SingletonHolder {
        val holder = LoginUtils()
    }

    fun doAutoLogin() {
        val model = UserModel()
        model.id = 666L
        model.name = "Sunny"
        model.status = LoginStatus.VIP

        mUserModel = model
    }
}