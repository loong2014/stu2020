package com.sunny.family

import com.sunny.lib.base.BaseApplication
import com.sunny.lib.manager.ActivityLifeManager
import com.sunny.lib.utils.HandlerUtils

class FamilyApplication : BaseApplication() {

    companion object {
        fun exitApp() {

            ActivityLifeManager.finishAll()

            HandlerUtils.uiHandler.postDelayed({
                val curPid = android.os.Process.myPid()
                android.os.Process.killProcess(curPid)
            }, 200)
        }
    }

    override fun onCreate() {
        super.onCreate()
        AppConfig.init(this)

    }
}
