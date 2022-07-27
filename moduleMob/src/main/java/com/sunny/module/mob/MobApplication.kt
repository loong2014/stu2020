package com.sunny.module.mob

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.mob.MobSDK
import com.sunny.lib.common.base.BaseApplication
import timber.log.Timber

class MobApplication : BaseApplication() {
    override fun getLogTag(): String {
        return "SunnyMob-"
    }

    override fun onCreate() {
        super.onCreate()
        Timber.i("onCreate")
        MobSDK.init(this)

        ProcessLifecycleOwner.get().lifecycle.addObserver(object : LifecycleObserver {

            /**
             * 应用被创建
             */
            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            fun onAppCreate() {
                Timber.i("onAppCreate")
                doInit()
            }

            /**
             * 应用进入后台
             */
            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun onAppBackgrounded() {
                Timber.i("onAppBackgrounded")
            }

            /**
             * 应用回到前台
             */
            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            fun onAppForegrounded() {
                Timber.i("onAppForegrounded")
            }

            /**
             * 应用被销毁
             */
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onAppDestroy() {
                Timber.i("onAppDestroy")
                doRelease()
            }
        })
    }

    fun doInit() {
        MobPushManager.doInit()
    }

    fun doRelease() {
        MobPushManager.doRelease()
    }
}