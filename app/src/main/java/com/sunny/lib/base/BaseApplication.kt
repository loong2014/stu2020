package com.sunny.lib.base

import android.app.Application

import com.sunny.lib.utils.ContextProvider

open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ContextProvider.appContext = this

    }
}
