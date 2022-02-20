package com.sunny.module.ble.client

import com.sunny.module.ble.BleBaseService
import com.sunny.module.ble.BleConfig

/**
 * 蓝牙服务的基类
 */
abstract class BleBaseClientService : BleBaseService() {

    fun showLog(msg: String) {
        BleConfig.bleLog("Client :$msg")
    }

    fun dealRcvMsg(msg: String) {
        log("dealRcvMsg msg :$msg")
        bleCallback?.onRcvClientMsg(msg)
    }

    fun sendServerState(tip: String) {
        bleCallback?.onConnectStateChanged(1, tip)
        showLog(tip)
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