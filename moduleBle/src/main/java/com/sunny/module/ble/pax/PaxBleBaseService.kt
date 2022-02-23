package com.sunny.module.ble.pax

import com.sunny.lib.base.log.SunLog
import com.sunny.module.ble.BleBaseService

/**
 * 蓝牙服务的基类
 */
open class PaxBleBaseService : BleBaseService() {


    fun showUiInfo(info: String) {
        SunLog.i("BleLog", info)
        bleCallback?.onRcvClientMsg(info)
    }

    override fun doRelease() {
    }

    override fun doInit() {
    }

    override fun doSendMsg(msg: String): Boolean {
        return false
    }

    override fun doReadMsg(): String {
        return ""
    }

    override fun doStartScan() {
    }

    override fun doStopScan() {
    }

    override fun doStartConnect(address: String): Boolean {
        return false
    }

    override fun doStopConnect() {
    }

}