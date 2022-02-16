package com.sunny.module.ble

import android.bluetooth.BluetoothDevice
import com.sunny.lib.base.log.SunLog
import java.util.*

/**
 * [BluetoothDevice.getType]
 * 1==经典
 * 2==低能耗
 * 3==双模式
 */


object BleConfig {

    val MsgTypeRecentTip = 1
    val MsgTypeScanDevices = 2

    const val PAX_BLE_NAME = "bluetooth_socket_sunny"
    val PAX_UUID_SERVICE: UUID = UUID.fromString("db764ac8-4b08-7f25-aafe-59d03c271111")
    val PAX_UUID_WRITE: UUID = UUID.fromString("db764ac8-4b08-7f25-aafe-59d03c272222")
    val PAX_BLE_UUID: UUID = UUID.fromString("db764ac8-4b08-7f25-aafe-59d03c273333")

    //
//    private val targetDeviceArray = arrayOf<Pair<String, String>>(
//        Pair("FF 91 Driver", "22:22:ED:16:C2:52"),
//        Pair("Redmi K40", "9C:5A:81:2F:19:39"),
//        Pair("MI 6", "20:47:DA:9B:A2:73"),
//    )

    fun bleLog(msg: String, tag: String = "") {
        SunLog.i("BleSunny", "$tag:$msg")
    }

    //
    var curBleDemoIsClient = true
    fun updateBleDemoType() {
        curBleDemoIsClient = !curBleDemoIsClient
    }

    //
    var curBluetoothTypeIsClassic = false
    fun updateBluetoothType() {
        curBluetoothTypeIsClassic = !curBluetoothTypeIsClassic
    }

    //
    var curDiscoveryDevices: List<BluetoothDevice> = mutableListOf()
    fun updateCurDiscoveryDevices(devices: Set<BluetoothDevice>) {
        curDiscoveryDevices = devices.map { it }
    }

    //
    private var curTargetIndex = -1
    var curTargetDevice: BluetoothDevice? = null
    fun updateTargetDevice(): BluetoothDevice? {
        curTargetDevice = curDiscoveryDevices.takeIf { it.isNotEmpty() }?.let {
            curTargetIndex++
            if (curTargetIndex >= curDiscoveryDevices.size) {
                curTargetIndex = 0
            }
            curDiscoveryDevices[curTargetIndex]
        }
        return curTargetDevice
    }

    fun resetCurTargetDevice() {
        curTargetIndex = -1
        curTargetDevice = null
    }
}