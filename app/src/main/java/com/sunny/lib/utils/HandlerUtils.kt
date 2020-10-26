package com.sunny.lib.utils

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper

object HandlerUtils {

    @JvmStatic
    val uiHandler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }

    @JvmStatic
    val workHandler: Handler by lazy {
        val handlerThread = HandlerThread("com.sunny.family.work")
        handlerThread.start()
        Handler(handlerThread.looper)
    }

}