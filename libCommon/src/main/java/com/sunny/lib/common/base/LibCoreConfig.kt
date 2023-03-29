package com.sunny.lib.common.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber

object LibCoreConfig {

    lateinit var appContext: Context

    @JvmStatic
    fun initContext(context: Context) {
        appContext = context
    }

    fun getSystemService(name: String): Any? {
        return appContext.getSystemService(name)
    }

    @SuppressLint("MissingPermission")
    fun sendBroadcastAsUser(intent: Intent, userHandle: UserHandle?) {
        if (userHandle == null) {
            Timber.e("sendBroadcastAsUser userHandle is null")
            return
        }
        try {
            appContext.sendBroadcastAsUser(intent, userHandle)
        } catch (e: Exception) {
            Timber.e("sendBroadcastAsUser error :$e")
        }
    }

    val resources: Resources by lazy {
        appContext.resources
    }

    val pkgName: String by lazy {
        appContext.packageName
    }

    val mainHandler: Handler = Handler(Looper.getMainLooper())

    val workHandler: Handler by lazy {
        val ht = HandlerThread("pax_work_handler(${Process.myPid()})")
        ht.start()
        Handler(ht.looper)
    }

    fun getColor(resId: Int): Int {
        return resources.getColor(resId)
    }

    /**
     * Application周期内的[CoroutineScope]提供者，当需要在页面生命周期之外开启协程时使用
     */
    val mainApplicationScope by lazy {
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate + CoroutineExceptionHandler { _, throwable ->
            Timber.e("MainApplicationScope:\n${throwable.message.toString()}", throwable)
        })
    }

    /**
     * 默认[Dispatchers.Default]
     */
    val defaultApplicationScope by lazy {
        CoroutineScope(SupervisorJob() + Dispatchers.Default + CoroutineExceptionHandler { _, throwable ->
            Timber.e("DefaultApplicationScope:\n${throwable.message.toString()}", throwable)
        })
    }

    /**
     * 默认[Dispatchers.IO]
     */
    val ioApplicationScope by lazy {
        CoroutineScope(SupervisorJob() + Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            Timber.e("IOApplicationScope:\n${throwable.message.toString()}", throwable)
        })
    }
}