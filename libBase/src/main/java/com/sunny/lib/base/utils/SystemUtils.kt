package com.sunny.lib.base.utils

import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.util.TypedValue
import com.sunny.lib.base.log.SunLog

object SystemUtils {
    private const val TAG = "SystemUtils"

    /**
     * 是否是测试包
     */
    @JvmStatic
    fun isDebug(context: Context): Boolean {
        try {
            val packInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            if (packInfo != null) {
                val info = packInfo.applicationInfo
                return info.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
            }
        } catch (t: Throwable) {
            throw RuntimeException(t)
        }
        return false
    }

    /**
     * 判断当前进程是否为主进程
     */
    @JvmStatic
    fun isMainProcess(context: Context): Boolean {
        val curPid = android.os.Process.myPid()
        val mainPid: Int = getPidByProcessName(context, context.packageName)
        return curPid == mainPid
    }

    /**
     * 根据进程名称获取进程id
     *
     * @param name 进程名
     * @return
     */
    @JvmStatic
    fun getPidByProcessName(context: Context, name: String): Int {

        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        // 通过调用ActivityManager的getRunningAppProcesses()方法获得系统里所有正在运行的进程
        val appProcessList = activityManager.runningAppProcesses
        if (appProcessList != null) {
            for (appProcess in appProcessList) {
                if (appProcess.processName == name) {
                    return appProcess.pid
                }
            }
        } else {
            SunLog.e(TAG, "app process list is empty")
        }
        return -1
    }

    /**
     * 判断应用是否安装
     */
    fun isAppInstalled(context: Context, pkgName: String): Boolean {
        val pkgInfo = try {
            context.packageManager.getPackageInfo(pkgName, 0)
        } catch (e: Exception) {
            null
        }
        return pkgInfo != null
    }

    fun dp2px(context: Context, dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        )
    }

    fun sp2px(context: Context, sp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            context.resources.displayMetrics
        )
    }
}