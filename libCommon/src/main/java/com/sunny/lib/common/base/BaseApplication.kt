package com.sunny.lib.common.base

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.sunny.lib.common.BuildConfig
import com.sunny.lib.common.utils.ResUtils
import com.sunny.lib.utils.ContextProvider
import com.sunny.lib.utils.FileUtils

open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()



        initLevel1(this)

        initLevel2(this)
    }

    private fun initLevel1(application: Application) {
        ContextProvider.init(application)

        ResUtils.init(application)

        FileUtils.init(application)
    }

    private fun initLevel2(application: Application) {
        // 这两行必须写在init之前，否则这些配置在init过程中将无效
        if (BuildConfig.DEBUG) {
            // 打印日志
            ARouter.openLog()
            // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openDebug()
        }
        // 尽可能早，推荐在Application中初始化
        ARouter.init(this)
    }
}
