package com.sunny.module.ble.pax

import android.app.Service
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import com.sunny.lib.base.log.SunLog
import com.sunny.module.ble.IBleCallbackInterface
import com.sunny.module.ble.IBleClientInterface

/**
 * 蓝牙服务的基类
 */
open class PaxBleCommonService : Service() {

    lateinit var mmContext: Context
    lateinit var mWorkHandler: Handler
    lateinit var mMainHandler: Handler

    var bleCallback: IBleCallbackInterface? = null

    open fun doRelease() {}
    open fun doInit() {}

    open fun doReadMsg(): String = ""
    open fun doSendMsg(msg: String) {}

    open fun doStartScan() {}
    open fun doStopScan() {}

    open fun doStartConnect(address: String) {}

    open fun doStopConnect() {}

    open fun doParseOption(opt: Int) {}

    fun showLog(log: String) {
        SunLog.i("PaxBle", log)
    }

    fun showUiInfo(info: String) {
        SunLog.i("PaxBle", info)
        bleCallback?.onRcvClientMsg(info)
    }

    fun getValue(descriptor: BluetoothGattDescriptor?): String {
        return descriptor?.value?.run {
            String(this)
        } ?: "None"
    }
    fun getValue(characteristic: BluetoothGattCharacteristic?): String {
        return characteristic?.value?.run {
            String(this)
        } ?: "None"
    }

    private val mClientBleBinder = object : IBleClientInterface.Stub() {
        override fun registerCallBack(callback: IBleCallbackInterface?) {
            bleCallback = callback
        }

        override fun unRegisterCallBack(callback: IBleCallbackInterface?) {
        }

        override fun sendMsg(msg: String?): Boolean {
            if (msg == null) return false
            doSendMsg(msg)
            return true
        }

        override fun readMsg(): String {
            return doReadMsg()
        }

        override fun readDeviceInfo(opt: Int): String {
            return ""
        }

        override fun startScan(): Boolean {
            doStartScan()
            return true
        }

        override fun stopScan(): Boolean {
            doStopScan()
            return true
        }

        override fun startConnect(address: String?): Boolean {
            if (address == null) return false
            doStartConnect(address)
            return true
        }

        override fun stopConnect(): Boolean {
            doStopConnect()
            return true
        }

        override fun sendOpt(opt: Int): Boolean {
            doParseOption(opt)
            return true
        }
    }

    override fun onCreate() {
        super.onCreate()
        mmContext = this
        val ht = HandlerThread("pax_ble")
        ht.start()
        mWorkHandler = Handler(ht.looper)

        mMainHandler = Handler(Looper.getMainLooper())
        doInit()
    }

    override fun onDestroy() {
        doRelease()
        mWorkHandler.looper.quit()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return mClientBleBinder
    }
}