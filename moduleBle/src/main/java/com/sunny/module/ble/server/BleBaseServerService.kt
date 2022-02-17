package com.sunny.module.ble.server

import com.sunny.module.ble.BleBaseService

/**
 * 蓝牙服务的基类
 */
abstract class BleBaseServerService : BleBaseService() {

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