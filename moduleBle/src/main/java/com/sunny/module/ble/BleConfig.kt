package com.sunny.module.ble

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.os.ParcelUuid
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sunny.lib.base.log.SunLog
import com.sunny.module.ble.master.PaxMeetingInfo
import com.sunny.module.ble.master.PaxZoomInfo
import java.io.BufferedReader
import java.io.InputStreamReader
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

    // BLE
    val PAX_BLE_CLIENT_UUID: UUID by lazy {
        UUID.fromString("db764ac8-4b08-7f25-aafe-59d03c271111")
    }

    val PAX_BLE_PERMISSIONS: Int = 1

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
        SunLog.i("BleLog-", msg)
    }

    fun bleLog(tag: String, msg: String) {
        SunLog.i("BleLog-", "$tag:$msg")
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

    fun doGetLocalMeetingInfoString(context: Context): String {
        return getMeetingInfoFromAssets(context, "meeting_info.json")
    }


    private fun buildMeetingInfo(context: Context): PaxMeetingInfo {
        val list: List<PaxZoomInfo>? = getIosMeetingInfo(context)?.asIterable()?.mapNotNull {
            buildZoomInfoByMeetingInfo(it)
        }
        return PaxMeetingInfo(list)
    }

    private fun buildZoomInfoByMeetingInfo(info: IosMeetingInfo): PaxZoomInfo? {
        if (info.os != "ios") return null
        return PaxZoomInfo(info.title, info.url, info.notes, info.startDate, info.endDate)
    }

    private fun getIosMeetingInfo(context: Context): List<IosMeetingInfo>? {
        val json = getMeetingInfoFromAssets(context, "meeting_info.json")
        return Gson().fromJson(json, object : TypeToken<ArrayList<IosMeetingInfo>>() {}.type)
    }

    private fun getMeetingInfoFromAssets(context: Context, name: String): String {
        val input = context.assets.open(name)
        BufferedReader(InputStreamReader(input)).use {
            val sb = StringBuilder()
            var line: String
            while (true) {
                line = it.readLine() ?: break
                sb.append(line)
            }
            return sb.toString()
        }
    }
}

data class IosMeetingInfo(
    var os: String,
    var title: String,
    var url: String,
    var notes: String,
    var startDate: String,
    var endDate: String
)

data class PaxBleMeetingInfo(
    var os: String,
    var list: List<IosMeetingInfo>
)