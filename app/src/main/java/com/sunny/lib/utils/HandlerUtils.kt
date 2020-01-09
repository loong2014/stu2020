package com.sunny.lib.utils

import android.os.Handler
import android.os.Looper

object HandlerUtils {

    private var uiHandler: Handler = Handler(Looper.getMainLooper())

    fun getUiHandler(): Handler {
        return uiHandler
    }
    fun jjjj(){

    }
}