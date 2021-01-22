package com.sunny.lib.common.router

import com.alibaba.android.arouter.launcher.ARouter
import com.sunny.lib.common.utils.JsonUtils

object RouterJump {

    @JvmStatic
    fun navigation(url: String) {
        ARouter.getInstance().build(url).navigation()
    }

    @JvmStatic
    fun navigation(url: String, jumpValue: String = "") {
        ARouter.getInstance().build(url)
                .withString(RouterConstant.JumpValue, jumpValue)
                .navigation()
    }

    @JvmStatic
    fun navigation(url: String, any: Any?) {
        val jumpValue = JsonUtils.getJson(any)
        ARouter.getInstance().build(url)
                .withString(RouterConstant.JumpValue, jumpValue)
                .navigation()
    }
}