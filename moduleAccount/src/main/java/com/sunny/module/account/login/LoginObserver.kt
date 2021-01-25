package com.sunny.module.account.login

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.sunny.lib.base.log.SunLog

/**
 * Created by zhangxin17 on 2021/1/25
 */
class LoginObserver(val lifecycle: Lifecycle) : LifecycleObserver {

    companion object {
        const val TAG = "LoginObserver"
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onActivityStatusChanged() {
        SunLog.i(TAG, "onActivityStatusChanged :${lifecycle.currentState}")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onActivityStart() {
        SunLog.i(TAG, "onActivityStart")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onActivityStop() {
        SunLog.i(TAG, "onActivityStop")
    }
}