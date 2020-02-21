package com.sunny.lib.base

import android.app.Application
import com.sunny.lib.http.SunHttp
import com.sunny.lib.utils.ContextProvider
import com.sunny.lib.utils.ResUtils

open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ContextProvider.appContext = this
        ResUtils.init(this)
        SunHttp.init(this)

    }
}
