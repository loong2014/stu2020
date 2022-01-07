package com.sunny.lib.common.base

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.sunny.lib.base.log.SunLog
import com.sunny.lib.base.utils.SystemUtils
import com.sunny.lib.common.crash.CrashManager
import com.sunny.lib.common.fresco.FrescoUtils
import com.sunny.lib.common.utils.ResUtils
import com.sunny.lib.utils.AppConfigUtils
import com.sunny.lib.base.utils.ContextProvider
import com.sunny.lib.utils.FileUtils

/**
 * Created by zhangxin17 on 2021/1/22
 * 应用初始化工具类
 */
object AppInitUtils {

    private const val TAG = "AppInitUtils"

    private var hasInit = false

    /**
     * 是否已完成初始化
     */
    @JvmStatic
    fun isAppInit(): Boolean {
        return hasInit
    }

    /**
     * 必须在应用启动时完成的初始化
     */
    fun doInitByApplication(application: Application) {
        val isMainProcess = SystemUtils.isMainProcess(application)

        SunLog.i(TAG, "doInitByApplication :$application , isMainProcess :$isMainProcess")

        doFirstLevelInit(application, isMainProcess)

        doSecondLevelInit(application, isMainProcess)
    }


    /**
     * Level1——应用启动时需要进行的初始化——基础模块
     */
    private fun doFirstLevelInit(application: Application, isMainProcess: Boolean) {
        ContextProvider.init(application)

        ResUtils.init(application)

        FileUtils.init(application)
    }

    /**
     * Level2——应用启动时需要进行的初始化——功能模块
     */
    private fun doSecondLevelInit(application: Application, isMainProcess: Boolean) {

        if (!isMainProcess) {
            return
        }

        // 目录初始化
        FileUtils.init(application)

        // 应用崩溃上报
        CrashManager.getInstance().init(application)

        // 路由初始化
        if (AppConfigUtils.isDebug()) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(application)

        // 图片初始化
        FrescoUtils.init(application)
    }

    /**
     * 系统异常引起的退出
     */
    @JvmStatic
    fun doExitAppByCrash() {
        val curPid = android.os.Process.myPid()
        SunLog.i(TAG, "doExitAppByCrash  pid :$curPid")
        android.os.Process.killProcess(curPid)
    }

}