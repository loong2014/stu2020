package com.sunny.family

import android.content.Context
import com.sunny.lib.utils.FileUtils

/**
 * Created by zhangxin17 on 2020-01-10
 */
object AppConfig {

    fun init(context: Context) {

//        if (LeakCanary.isInAnalyzerProcess(this)) {//1
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
        FileUtils.init(context)
    }

}