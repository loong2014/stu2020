/*
 * 应用级别的顶层函数/扩展函数
 */
package com.sunny.lib.common.utils

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Looper
import android.os.Process
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sunny.lib.common.base.LibCoreConfig
import timber.log.Timber


fun isMainThread() = Looper.myLooper() == Looper.getMainLooper()


fun isMainProcess(context: Context): Boolean {
    val curPid = Process.myPid()
    val pkgPid = getPidByProcessName(context, context.packageName)
    Timber.i("isMainProcess pkgPid($pkgPid) , curPid($curPid)")
    return curPid == pkgPid
}

/**
 * 是否是HPC
 */
val isHPC: Boolean by lazy {
    Build.PRODUCT.equals("df91_hu_hpc")
}

/**
 * 是否是CID
 */
val isCID: Boolean by lazy {
    Build.PRODUCT.equals("df91_hu_mpc")
}

/**
 * 获取字符数量 汉字占2个长度，英文占1个长度
 */
fun handleText(text: String?, maxLen: Int): String {
    if (text.isNullOrBlank()) return ""

    var count = 0
    var endIndex = 0
    text.forEachIndexed { index, char ->
        count += if (char.code < 128) 1 else 2
        if (maxLen == count || (char.code >= 128 && maxLen + 1 == count)) {
            endIndex = index
            return@forEachIndexed
        }
    }
    return if (count < maxLen) text else {
        text.substring(0, endIndex) + "..."
    }
}

/**
 * 判断应用是否安装
 */
fun isAppInstalled(pkgName: String): Boolean {
    val pkgInfo = try {
        LibCoreConfig.appContext.packageManager.getPackageInfo(pkgName, 0)
    } catch (e: Exception) {
        null
    }
    return pkgInfo != null
}

fun getPidByProcessName(context: Context, pkgName: String?): Int {
    if (pkgName.isNullOrBlank()) return -1
    val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    am.runningAppProcesses?.forEach {
        if (it.processName == pkgName) {
            return it.pid
        }
    }
    return -1
}

fun getProcessNameByPid(context: Context): String? {
    val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    am.runningAppProcesses?.forEach {
        if (it.pid == android.os.Process.myPid()) {
            return it.processName
        }
    }
    return null
}

inline fun <reified T> fromJson(json: String?): T? {
    json ?: return null
    return try {
        val type = object : TypeToken<T>() {}.type
        return Gson().fromJson(json, type)
    } catch (e: Exception) {
        Timber.i("fromJson error :$e")
        null
    }
}

inline fun <reified T> toJson(bean: T?): String? {
    bean ?: return null
    return try {
        return Gson().toJson(bean)
    } catch (e: Exception) {
        null
    }
}

inline fun View.setSafeClickListener(crossinline action: () -> Unit) {
    setSafeClickListener(1000, action)
}

inline fun View.setSafeClickListener(gap: Int, crossinline action: () -> Unit) {
    var lastClick = 0L
    setOnClickListener {
        val dif = System.currentTimeMillis() - lastClick
        lastClick = System.currentTimeMillis()
        if (dif < gap) return@setOnClickListener
        action.invoke()
    }
}

fun String.toOnlyCode(): Int {
    var code: Int = 0
    forEach {
        code += it.code
    }
    return code
}