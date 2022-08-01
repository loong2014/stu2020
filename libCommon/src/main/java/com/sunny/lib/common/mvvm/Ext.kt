package com.sunny.lib.common.mvvm

import android.os.Looper

/**
 */

fun isMainThread() = Looper.myLooper() == Looper.getMainLooper()

fun needLogin(loginAfter: () -> Unit) {
    loginFunction(loginAfter, {
//        RouterHelper.openLogin()
    })
}

fun loginFunction(loginAfter: () -> Unit, loginBefore: () -> Unit) {
//    if (AccountManager.INSTANCE.hasAccount()) {
//        loginAfter.invoke()
//    } else {
//        loginBefore.invoke()
//    }
}

//是否测试环境
//fun isDebug(): Boolean {
//    return Constants.isDev() || Constants.isTest() || BuildConfig.DEBUG
//}