package com.sunny.lib.utils

import android.app.Application
import android.content.ContentResolver
import android.content.Context

object ContextProvider {

    lateinit var appContext: Context

    val contentResolver: ContentResolver by lazy {
        appContext.contentResolver
    }

}