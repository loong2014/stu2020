package com.sunny.module.ble

import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.sunny.lib.base.log.SunLog
import com.sunny.module.ble.master.PaxBleMasterService
import com.sunny.module.ble.utils.PaxByteUtils
import java.security.MessageDigest
import java.util.*

/**
 * 常用的uuid格式
 * https://www.cnblogs.com/bulazhang/p/8450172.html
 * new ParcelUuid(UUID.randomUUID())
 *
 * 注册 RSD 后排用户，触发BLE服务启动
 * adb shell am broadcast -a com.ff.iai.pax.action.RegisterUser --es ext_user_display "RSD" --ei ext_user_id 10 --es ext_user_ffid "61bad4f56acce30010246260"
 */


const val SEAT_FPD_NAME = "FPD"
const val SEAT_RSD_NAME = "RSD"


internal fun showLog(log: String) {
    SunLog.i("PaxBle", log)
}

internal fun showLog(tag: String, log: String) {
    SunLog.i("PaxBle-$tag", log)
}

object PaxBleConfig {

    const val BleDebugUserID = 10
    const val BleDebugFFID = "61bad4f56acce30010246260"
    // 61bad4f56acce30010246260  >>> 99cce69d-8319-b4e3-85dd-737c1d497763

    const val EXT_USER_DISPLAY = "ext_user_display"
    const val EXT_USER_ID = "ext_user_id"
    const val EXT_USER_FFID = "ext_user_ffid"

    // 手机端发送的消息类型
    const val SessionTypeCloseShare = 0 // 断开连接

    /**
     * 传输最大值（字节）
     */
    const val PAX_MTU = 512

    // key for phone
    private const val KWY_1_PRIVATE =
        "04efb3984ae2138b6c9c9b712978b33c1bf714a0c06b019b1b1983674af210b3833c8c53d953adc9c8c08c861a14ba43f72c3212e42ce1453332fddd4c7d0af08ae373a554c2ef805779de1f627a2bd0f04ed5ea6f185bc6ad76ef13ac78723a39"

    // key for vehicle
    private const val KWY_2_PRIVATE =
        "04b430274845fd5a6363d4ef49474ccb29b0f36f6438ceea2c631506347ccbff5074d61d083e5a2e08e3206852225284f7c1d27da57025ed0ea116d058527eb099cab15e3fb82aeff69f432333ac41c1e179496fd343e34ac1bc289d3d01d7b5ec"

    val PAX_UUID_RESPONSE: UUID = UUID.fromString("00004000-0000-1000-8000-00805f9b34fb")
    val PAX_UUID_SUNNY: UUID = UUID.fromString("00004001-0000-1000-8000-00805f9b34fb")

    /**
     * 鉴权
     */
    val PAX_UUID_CALENDAR_AUTH: UUID = UUID.fromString("5C81B02B-8C65-7C85-80E2-8EB24F1A4E15")

    /**
     * 通知，日历有变化时，通过notify发送日历信息
     * sessionId/pageSize/pageIndex/content data
     * int/short/short/data...
     */
    val PAX_UUID_CALENDAR_NOTIFY: UUID =
        UUID.fromString("5C81B02B-8C66-7C85-80E2-8EB24F1A4E15")

    /**
     * 通知，日历有变化时，通过notify发送日历基本信息
     * sessionId/totalSize
     * int/int
     */
    val PAX_UUID_CALENDAR_READABLE: UUID = UUID.fromString("5C81B02B-8C67-7C85-80E2-8EB24F1A4E15")

    // 通用descriptor UUID
    val PAX_UUID_NOTIFY_DESCRIPTOR: UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")

    /**
     * 主动读取日历信息，当收到@[PAX_UUID_CALENDAR_READABLE]的通知后，主动读取日历数据
     * 写入数据:sessionId/startPos/readSize
     * int/int/short
     * 读取后返回数据:sessionId/startPos/readSize/content data
     */
    val PAX_UUID_CALENDAR_READ: UUID = UUID.fromString("5C81B02B-8C68-7C85-80E2-8EB24F1A4E15")

    /**
     * 开启ble服务
     */
    internal fun doStartService(context: Context, intent: Intent? = null) {
        val bleIntent = intent ?: Intent()
        bleIntent.component = ComponentName(context, PaxBleMasterService::class.java)
        context.startService(bleIntent)
    }

    /**
     * 关闭ble服务
     */
    internal fun doStopService(context: Context) {
        context.stopService(Intent(context, PaxBleMasterService::class.java))
    }

    /**
     * 根据FFID构建UUID
     */
    fun buildUUIDByFFID(ffid: String?): UUID? {
        if (ffid.isNullOrBlank()) return null

        // 根据ffid生成对应的md5
        val ffidMd5 = md5String(ffid)

        // 根据ffid的md5生成uuid
        val uuid = StringBuilder()
        ffidMd5.forEachIndexed { index, c ->
            uuid.append(c)
            if (index == 7 || index == 11 || index == 15 || index == 19) {
                uuid.append("-")
            }
        }
        return try {
            UUID.fromString(uuid.toString())
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 构建手机端的auth信息，用于校验手机是否合法
     */
    fun buildPhoneAuthKeyArray(ffid: String?): ByteArray {
        val arrayUserId = ffid?.toByteArray() ?: return byteArrayOf()
        val arrayK = PaxByteUtils.hexToBytes(KWY_1_PRIVATE)
        return md5Array(arrayK + arrayUserId)
    }

    /**
     * 构建车机端的auth信息，用于发送给手机端
     */
    fun buildVehicleAuthKeyArray(ffid: String?): ByteArray {
        val arrayUserId = ffid?.toByteArray() ?: return byteArrayOf()
        val arrayK = PaxByteUtils.hexToBytes(KWY_2_PRIVATE)
        return md5Array(arrayK + arrayUserId)
    }

    /**
     * 判断是否有访问位置的权限，没有权限，直接申请位置权限
     */
    fun checkBlePermission(activity: Activity, requestCode: Int): Boolean {
        if ((activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            || (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            || (activity.checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
            || (activity.checkSelfPermission(Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        ) {
            activity.requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS
                ), requestCode
            )
            return false
        }
        return true
    }

    private fun md5String(str: String?): String {
        if (str.isNullOrBlank()) return ""
        return try {
            val md5 = MessageDigest.getInstance("MD5")
            val result = StringBuilder()
            md5.digest(str.toByteArray()).forEach { b ->
                val temp: String = Integer.toHexString(b.toInt() and (0xff))
                if (temp.length == 1) {
                    result.append("0")
                }
                result.append(temp)
            }
            result.toString()
        } catch (e: Exception) {
            ""
        }
    }

    private fun md5Array(array: ByteArray): ByteArray {
        return try {
            val md5 = MessageDigest.getInstance("MD5")
            return md5.digest(array)
        } catch (e: Exception) {
            byteArrayOf()
        }
    }
}