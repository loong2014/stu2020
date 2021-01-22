package com.sunny.lib.common.base

import android.app.Application

open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AppInitUtils.doInitByApplication(this)
    }
}
