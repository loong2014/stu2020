package com.sunny.module.ble.master

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.ParcelUuid
import com.sunny.lib.base.log.SunLog
import com.sunny.module.ble.PaxBleConfig
import java.util.*

abstract class PaxBleConnectThread(val context: Context, val name: String) {

    var curFFID: String? = null
    var curServiceUUID: UUID? = null
    var curConnectedDevice: BluetoothDevice? = null

    // 是否已经连接
    var mmConnected = false

    // 是否正在连接
    var isConnecting = false

    /**
     * 是否正在连接中，BLE 中同一时刻，只能进行一台设备的连接，新的连接需要等当前设备连接成功/失败之后
     */
    fun inConnecting(): Boolean {
        return isConnecting
    }

    fun updateConnectState(connected: Boolean) {
        isConnecting = false
        mmConnected = connected
    }

    /**
     * 获取当前用户的FFID
     */
    abstract fun getFFID(): String?

    /**
     * 开始连接
     */
    abstract fun doConnect(device: BluetoothDevice)

    abstract fun doDisConnect()

    /**
     * 获取当前用户的 ScanFilter
     */
    fun buildScanFilter(): ScanFilter? {
//        curFFID = getFFID()
        curFFID = PaxBleConfig.ServiceFFIDStr
        curServiceUUID = PaxBleConfig.buildUUIDByFFID(curFFID)
        showLog("getServiceUUID $curFFID  >>> $curServiceUUID")
        return curServiceUUID?.let {
            ScanFilter.Builder()
                .setServiceUuid(ParcelUuid(it))
                .build()
        }
    }

    fun dealConnected(result: ScanResult): Boolean {
        if (curServiceUUID == null) return false

        val hasMatch =
            result.scanRecord?.serviceUuids?.also { it.isNotEmpty() }
                ?.contains(ParcelUuid(curServiceUUID)) ?: false
        showLog("tryConnected ${result.device} , hasMatch = $hasMatch")

        if (hasMatch) {
            tryConnected(result.device)
        }
        return hasMatch
    }

    private fun tryConnected(device: BluetoothDevice) {
        if (device == curConnectedDevice) {
            showLog("$device Already Connected")
            return
        }
        isConnecting = true
        curConnectedDevice = device
        showLog("start connect")
        doConnect(device)
    }

    fun showLog(log: String) {
        SunLog.i("PaxBle", log)
    }

    fun showUiInfo(info: String) {
        SunLog.i("PaxBle", info)
    }
}