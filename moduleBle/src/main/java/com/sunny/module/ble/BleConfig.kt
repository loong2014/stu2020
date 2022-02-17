package com.sunny.module.ble

import android.bluetooth.BluetoothDevice
import android.os.ParcelUuid
import com.sunny.lib.base.log.SunLog
import java.util.*


//
//    private val targetDeviceArray = arrayOf<Pair<String, String>>(
//        Pair("FF 91 Driver", "22:22:ED:16:C2:52"),
//        Pair("Redmi K40", "9C:5A:81:2F:19:39"),
//        Pair("MI 6", "20:47:DA:9B:A2:73"),
//    )
object BleConfig {

    // 客户端与服务端的UUID需要一致
    val PAX_BLE_UUID: UUID by lazy {
        UUID.fromString("db764ac8-4b08-7f25-aafe-59d03c273333")
    }
    val PAX_BLE_P_UUID: ParcelUuid by lazy {
        ParcelUuid(PAX_BLE_UUID)
    }

    val MSG_SERVICE_CONNECTED: String = "sConnected"

    val MSG_CLIENT_DISCONNECT: String = "cDisconnect"

    val MsgTypeRecentTip = 1
    val MsgTypeScanDevices = 2
    val ConnectStateTypeConnected = 3

    val MSG_SERVICE_DEVICE_CONNECTED = 3

    const val pax_ff_device_uuid_prefix = "db764ac8-4b08"
    val PAX_UUID_SERVICE: UUID = UUID.fromString("db764ac8-4b08-7f25-aafe-59d03c271111")
    val PAX_UUID_WRITE: UUID = UUID.fromString("db764ac8-4b08-7f25-aafe-59d03c272222")

    var bleServerThreadCount: Int = 0
    fun getBleServerThreadName(): String {
        return "BleServer-${bleServerThreadCount++}"
    }

    var bleClientThreadCount: Int = 0
    fun getBleClientThreadName(): String {
        return "BleClient-${bleClientThreadCount++}"
    }


    fun bleLog(msg: String) {
        SunLog.i("BleSunny", msg)
    }

    fun bleLog(tag: String, msg: String) {
        SunLog.i("BleSunny", "$tag:$msg")
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

        BleTools.getBondedDevices()?.run {
            updateCurDiscoveryDevices(this)
        }
    }
}