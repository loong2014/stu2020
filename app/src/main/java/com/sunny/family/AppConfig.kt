package com.sunny.family

import android.content.Context
import com.facebook.drawee.backends.pipeline.Fresco
import com.sunny.lib.http.SunHttp
import com.sunny.lib.utils.ContextProvider
import com.sunny.lib.utils.FileUtils
import com.sunny.lib.utils.ResUtils

/**
 * Created by zhangxin17 on 2020-01-10
 */
object AppConfig {

    fun init(context: Context) {
        ContextProvider.init(context)
        ResUtils.init(context)
        SunHttp.init(context)
        Fresco.initialize(context)
//        if (LeakCanary.isInAnalyzerProcess(this)) {//1
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
        FileUtils.init(context)
    }

}