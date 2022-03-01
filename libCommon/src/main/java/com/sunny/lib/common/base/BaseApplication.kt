package com.sunny.lib.common.base

import android.app.Application
import com.sunny.lib.base.log.TimberUtils

open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        TimberUtils.doInit(this)
        AppInitUtils.doInitByApplication(this)
    }
}
