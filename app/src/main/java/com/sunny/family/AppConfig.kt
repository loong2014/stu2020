package com.sunny.family

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.facebook.drawee.backends.pipeline.Fresco
import com.sunny.lib.http.SunHttp
import com.sunny.lib.utils.ContextProvider
import com.sunny.lib.utils.FileUtils
import com.sunny.lib.utils.ResUtils

/**
 * Created by zhangxin17 on 2020-01-10
 */
object AppConfig {

    var hasInit: Boolean? = false

    fun applicationInit(application: Application) {
        if (true == hasInit) {
            return
        }
        hasInit = true
        initLevel1(application)
        initLevel2(application)
    }

    private fun initLevel1(application: Application) {
        ContextProvider.init(application)
        ResUtils.init(application)
        SunHttp.init(application)
        Fresco.initialize(application)
//        if (LeakCanary.isInAnalyzerProcess(this)) {//1
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
        FileUtils.init(application)
    }

    private fun initLevel2(application: Application) {
        if (BuildConfig.DEBUG) {
            // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog()     // 打印日志
            ARouter.openDebug()   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(application) // 尽可能早，推荐在Application中初始化
    }

}