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

    fun showLog(msg: String) {
        SunLog.i("BleLog-PaxBleService", msg)
    }

    /**
     * 显示收到的消息
     */
    fun showRcvMsg(msg: String) {
        showUiInfo("BleLog-Rcv:$msg")
    }

    /**
     * 显示发送的消息
     */
    fun showSendMsg(msg: String) {
        showUiInfo("BleLog-Send:$msg")
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