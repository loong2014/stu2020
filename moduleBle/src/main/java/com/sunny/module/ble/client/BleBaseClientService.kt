package com.sunny.module.ble.client

import android.bluetooth.BluetoothDevice
import com.sunny.module.ble.BleBaseService
import com.sunny.module.ble.BleConfig

/**
 * 蓝牙服务的基类
 */
abstract class BleBaseClientService : BleBaseService() {

    override fun log(msg: String) {
        BleConfig.bleLog("Client :$msg")
    }

    fun showLog(msg: String) {
        BleConfig.bleLog("Client :$msg")
    }

    fun dealRcvMsg(msg: String) {
        log("dealRcvMsg msg :$msg")
        bleCallback?.onRcvClientMsg(msg)
    }

    fun showDeviceInfo(device: BluetoothDevice?, tag: String) {
        if (device == null) {
            log("$tag >>> device is null")
            return
        }

        device.run {
            val sb = StringBuilder()
            sb.append(
                "$tag : $address --- $name" +
                        " --- type($type) --- bondState($bondState)"
            )
            uuids?.forEachIndexed { index, pUuid ->
                sb.append("\n>>> pUuid($index) :${pUuid}")
            } ?: sb.append("\n>>> uuids is empty")
            log(sb.toString())
        }
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