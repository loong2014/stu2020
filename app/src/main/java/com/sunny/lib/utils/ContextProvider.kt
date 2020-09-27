package com.sunny.lib.utils

import android.content.ContentResolver
import android.content.Context

object ContextProvider {

    lateinit var appContext: Context
    fun init(context: Context){
        appContext = context
    }

    val contentResolver: ContentResolver by lazy {
        appContext.contentResolver
    }

    @JvmStatic
    fun getSystemService(name: String): Any {
        return appContext.getSystemService(name)
    }
}