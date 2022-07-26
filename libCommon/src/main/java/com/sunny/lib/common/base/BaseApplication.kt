package com.sunny.lib.common.base

import android.app.Application
import com.sunny.lib.base.log.SunnyLogTree
import timber.log.Timber

open class BaseApplication : Application() {

    open fun getLogTag(): String {
        return "Sunny-"
    }

    override fun onCreate() {
        super.onCreate()
//        PaxFontHelper.changeDefaultFont(this)
        Timber.plant(SunnyLogTree(getLogTag()))
        AppInitUtils.doInitByApplication(this)
    }
}
