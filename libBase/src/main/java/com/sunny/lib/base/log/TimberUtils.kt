package com.sunny.lib.base.log

import android.content.Context
import timber.log.Timber

object TimberUtils {

    fun doInit(context: Context) {
        Timber.plant(SunnyLogTree())
    }
}