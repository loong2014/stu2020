package com.sunny.lib.utils

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper

object HandlerUtils {

    val uiHandler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }

    val workHandler: Handler by lazy {
        val handlerThread = HandlerThread("com.sunny.family.work")
        handlerThread.start()
        Handler(handlerThread.looper)
    }

}