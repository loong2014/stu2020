package com.sunny.module.ble

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
import com.sunny.lib.base.log.SunLog
import com.sunny.module.ble.master.PaxBleScanService
import com.sunny.module.ble.utils.NumberUtils
import okhttp3.internal.EMPTY_BYTE_ARRAY
import java.security.MessageDigest
import java.util.*

/**
 * 常用的uuid格式
 * https://www.cnblogs.com/bulazhang/p/8450172.html
 * new ParcelUuid(UUID.randomUUID())
 */

object PaxBleConfig {

    // 延时扫描间隔
    const val DELAY_SCAN_INTERVAL: Long = 5_000
    private const val PAX_DATA_FF91 = "ff91"

    const val ServiceFFIDStr = "61bad4f56acce30010246260"

    private const val KWY_1_PRIVATE =
        "04efb3984ae2138b6c9c9b712978b33c1bf714a0c06b019b1b1983674af210b3833c8c53d953adc9c8c08c861a14ba43f72c3212e42ce1453332fddd4c7d0af08ae373a554c2ef805779de1f627a2bd0f04ed5ea6f185bc6ad76ef13ac78723a39"
    private const val KWY_2_PRIVATE =
        "04b430274845fd5a6363d4ef49474ccb29b0f36f6438ceea2c631506347ccbff5074d61d083e5a2e08e3206852225284f7c1d27da57025ed0ea116d058527eb099cab15e3fb82aeff69f432333ac41c1e179496fd343e34ac1bc289d3d01d7b5ec"

    fun buildPhoneAuthKeyArray(ffid: String? = null): ByteArray {
        val arrayK = NumberUtils.hexToBytes(KWY_1_PRIVATE)
        val arrayUserId = ServiceFFIDStr.toByteArray()
        return md5Array(arrayK + arrayUserId)
    }

    fun buildVehicleAuthKeyArray(ffid: String? = null): ByteArray {
        val arrayK = NumberUtils.hexToBytes(KWY_2_PRIVATE)
        val arrayUserId = ServiceFFIDStr.toByteArray()
        return md5Array(arrayK + arrayUserId)
    }

    const val ServiceUUIDStr = "99cce69d-8319-b4e3-85dd-737c1d497763"

    const val AuthUUIDStr = "5C81B02B-8C65-7C85-80E2-8EB24F1A4E15"

    const val CalendarReadableUUIDStr = "5C81B02B-8C67-7C85-80E2-8EB24F1A4E15"

    const val CalendarReadUUIDStr = "5C81B02B-8C68-7C85-80E2-8EB24F1A4E15"

    /**
     * 获取测试用的UUID,xin.zhang@ff.com
     * 61bad4f56acce30010246260 >>> 99cce69d-8319-b4e3-85dd-737c1d497763
     */
    fun getServiceUUID(): UUID {
        return UUID.fromString(ServiceUUIDStr)
    }

    fun getAuthUUID(): UUID {
        return UUID.fromString(AuthUUIDStr)
    }

    fun getCalendarReadableUUID(): UUID {
        return UUID.fromString(CalendarReadableUUIDStr)
    }

    fun getCalendarReadableDesUUID(): UUID {
        return UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    }

    fun getCalendarReadUUID(): UUID {
        return UUID.fromString(CalendarReadUUIDStr)
    }

    fun getUUIDName(uuid: UUID): String = when (uuid) {
        getServiceUUID() -> "ServiceUUID"
        getAuthUUID() -> "AuthUUID"
        getCalendarReadableUUID() -> "CalendarUUID"
        else -> "unknown"
    }

    fun buildMeetInfo(context: Context): String {
        return BleConfig.doGetLocalMeetingInfoString(context)
    }

    fun doStartMasterService(context: Context) {
        context.run {
            startService(Intent(this, PaxBleScanService::class.java))
        }
    }


    fun doStartSlaveService(context: Context) {
        context.run {
            startService(Intent(this, PaxBleScanService::class.java))
        }
    }


    fun doStopSlaveService(context: Context) {
        context.run {
            stopService(Intent(this, PaxBleScanService::class.java))
        }
    }

    fun doStopMasterService(context: Context) {
        context.run {
            stopService(Intent(this, PaxBleScanService::class.java))
        }
    }

    /**
     *   // 用于和FF Ctrl配对的UUID
    // 客户端与服务端的UUID需要一致
    // 根据FFID+00000001构成
    // 59cc8a7d9353ad000ee9e0a9
    // 59cc8a7d-9353-ad00-0ee9-e0a9
    // 59cc8a7d-9353-ad00-0ee9-e0a900000001
    // 8-4-4-12(4+8)
     * 根据FFID构建UUID
     */
    fun buildUUIDByFFID(ffid: String?): UUID? {
        if (ffid.isNullOrBlank()) return null

        // 根据ffid生成对应的md5
        val ffidMd5 = md5(ffid)

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
     * 构建过滤设置
     */
    fun buildScanSettings(): ScanSettings {
        return ScanSettings.Builder()
            // 扫描模式
            .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
            // 返回类型
            .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
            // 匹配模式
            .setMatchMode(ScanSettings.MATCH_MODE_STICKY)
            // 匹配数
            .setNumOfMatches(ScanSettings.MATCH_NUM_MAX_ADVERTISEMENT)
            // 是否返回旧版广告
            .setLegacy(true)
            // 设置物理层模式，仅在 legacy=false时生效
            .setPhy(ScanSettings.PHY_LE_ALL_SUPPORTED)
            // 延时返回扫描结果，0表示立即返回
            .setReportDelay(0)
            .build()
    }

    // 设备认证特征，用户车机和手机之间的验证
    private val PAX_UUID_CHARACTERISTIC_FF: UUID by lazy {
        UUID.fromString("5C81B02B-8C65-7C85-80E2-8EB24F1A4E15")
    }

    // 日历信息特征，用户日历信息的发送/接收
    private val PAX_UUID_CHARACTERISTIC_MEETING: UUID by lazy {
        UUID.fromString("5C81B02B-8C66-7C85-80E2-8EB24F1A4E15")
    }

    /**
     * 构建 FF 数据特征
     */
    fun buildFFGattCharacteristic(): BluetoothGattCharacteristic {
        return BluetoothGattCharacteristic(
            PAX_UUID_CHARACTERISTIC_FF,
            (BluetoothGattCharacteristic.PROPERTY_WRITE or
                    BluetoothGattCharacteristic.PROPERTY_NOTIFY or
                    BluetoothGattCharacteristic.PROPERTY_READ),
            (BluetoothGattCharacteristic.PERMISSION_WRITE or
                    BluetoothGattCharacteristic.PROPERTY_NOTIFY or
                    BluetoothGattCharacteristic.PERMISSION_READ)
        ).apply {
            value = PAX_DATA_FF91.toByteArray()
        }
    }

    /**
     * 构建 Meeting 数据特征
     */
    fun buildMeetingGattCharacteristic(): BluetoothGattCharacteristic {
        return BluetoothGattCharacteristic(
            PAX_UUID_CHARACTERISTIC_MEETING,
            (BluetoothGattCharacteristic.PROPERTY_WRITE or
                    BluetoothGattCharacteristic.PROPERTY_NOTIFY or
                    BluetoothGattCharacteristic.PROPERTY_READ),
            (BluetoothGattCharacteristic.PERMISSION_WRITE or
                    BluetoothGattCharacteristic.PROPERTY_NOTIFY or
                    BluetoothGattCharacteristic.PERMISSION_READ)
        ).apply {
            value = "ff meeting".toByteArray()
        }
    }

    /**
     * 获取 meeting 数据特征
     */
    fun findMeetingGattCharacteristic(
        gattServices: List<BluetoothGattService>?
    ): BluetoothGattCharacteristic? {
        gattServices?.forEach { gattService ->
            gattService.getCharacteristic(PAX_UUID_CHARACTERISTIC_MEETING)?.also {
                return it
            }
        }
        return null
    }

    /**
     * 生成MD5值
     */
    fun md5(array: ByteArray?): String {
        if (array == null) return ""
        return try {
            val md5 = MessageDigest.getInstance("MD5")
            val result = StringBuilder()
            md5.digest(array).forEach { b ->
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

    fun md5Array(array: ByteArray): ByteArray {
        return try {
            val md5 = MessageDigest.getInstance("MD5")
            return md5.digest(array)
        } catch (e: Exception) {
            EMPTY_BYTE_ARRAY
        }
    }

    fun md5(str: String?): String {
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

    /**
     * 字节数组转换为十六进制字符串
     */
    fun byteArray2HEXString(byteArray: ByteArray?): String {
        val sb = StringBuilder()
        var tmp: String
        byteArray?.forEach { b ->
            tmp = Integer.toHexString(b.toInt() and (0xff))
            if (tmp.length == 1) {
                tmp = "0$tmp"
            }
            sb.append(tmp)
        }
//        String hs = "";
//        String stmp = "";
//        for (int n = 0; n < b.length; n++) {
//            stmp = Integer.toHexString(b[n] & 0xff);
//            if (stmp.length() == 1) {
//                hs = hs + "0" + stmp;
//            } else {
//                hs = hs + stmp;
//            }
//        }
        return sb.toString()
    }

    fun showLog(log: String) {
        SunLog.i("PaxBle", log)
    }

    fun buildByteArrayLogStr(byteArray: ByteArray?): String {
        val sb = StringBuilder()
        byteArray?.forEach { byte ->
            sb.append("($byte)")
        }
        return sb.toString()
    }
}