package com.sunny.lib.common.base

import android.app.Application
import com.sunny.lib.common.ModuleConfig

abstract class BaseModuleApplication : BaseApplication() {

    /**
     * Application 初始化
     */
    abstract fun initModuleApp(application: Application)

    /**
     * 所有 Application 初始化后的自定义操作
     */
    abstract fun initModuleData(application: Application)

    protected fun doInitModuleApp(application: Application) {
        ModuleConfig.moduleApplications.forEach {
            try {
                val clazz = Class.forName(it)
                val moduleApp: BaseModuleApplication = clazz.newInstance() as BaseModuleApplication
                moduleApp.initModuleApp(application)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    protected fun doInitModuleData(application: Application) {
        ModuleConfig.moduleApplications.forEach {
            try {
                val clazz = Class.forName(it)
                val moduleApp: BaseModuleApplication = clazz.newInstance() as BaseModuleApplication
                moduleApp.initModuleData(application)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
