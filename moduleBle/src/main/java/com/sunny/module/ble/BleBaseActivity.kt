package com.sunny.module.ble

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.sunny.lib.common.base.BaseActivity
import com.sunny.module.ble.master.PaxBleMasterService

open class BleBaseActivity : BaseActivity() {

    private var isBind = false
    protected var curBleControl: IBleClientInterface? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            curBleControl = IBleClientInterface.Stub.asInterface(service)
            curBleControl?.registerCallBack(bleCallback)
            showTip("onServiceConnected")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            showTip("onServiceDisconnected")
        }
    }

    private val bleCallback = object : IBleCallbackInterface.Stub() {
        override fun onRcvClientMsg(msg: String?) {
            showRcvMsg(msg ?: "Null")
        }

        override fun onRcvServiceMsg(msg: String?) {
            showRcvMsg(msg ?: "Null")
        }

        override fun onConnectStateChanged(type: Int, tip: String?) {
        }
    }

    open fun showRcvMsg(msg: String) {

    }

    open fun showTip(msg: String) {

    }

    protected fun doBindService(cls: Class<*>) {
        isBind = bindService(
            Intent(this, PaxBleMasterService::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
        if (isBind) {
            showTip("服务开启成功")
        } else {
            showTip("服务开启失败")
        }
    }

    protected fun doUnbindService() {
        if (isBind) {
            isBind = false
            unbindService(serviceConnection)
            showTip("服务关闭成功")
        } else {
            showTip("服务未开启")
        }
    }

    override fun onDestroy() {
        doUnbindService()
        super.onDestroy()
    }
}