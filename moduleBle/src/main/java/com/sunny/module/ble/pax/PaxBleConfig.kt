package com.sunny.module.ble.pax

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.bluetooth.le.AdvertiseData
import android.os.ParcelUuid
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


    // 用于和FF Ctrl配对的UUID
    // 客户端与服务端的UUID需要一致
    val PAX_SERVICE_UUID: UUID by lazy {
        UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb")
    }

    private val PAX_SERVICE_FF: UUID by lazy {
        UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb")
    }

    //
    private val PAX_UUID_CHARACTERISTIC_FF: UUID by lazy {
        UUID.fromString("0000ff11-0000-1000-8000-00805f9b34fb")
    }

    private val PAX_UUID_CHARACTERISTIC_MEETING: UUID by lazy {
        UUID.fromString("0000ff12-0000-1000-8000-00805f9b34fb")
    }

    //
    val PAX_UUID_CHARACTERISTIC_VEHICLE_ID: UUID by lazy {
        UUID.fromString("0000ff13-0000-1000-8000-00805f9b34fb")
    }

    val PAX_UUID_DESCRIPTOR: UUID by lazy {
        UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    }


    /**
     * 是否是FF91服务
     */
    fun isFF91Service(uuid: UUID, value: ByteArray): Boolean {
        return PAX_SERVICE_FF == uuid && PAX_DATA_FF91 == String(value)
    }

    /**
     * 构建FF91广播信息
     */
    fun buildFF91AdvertiseData(): AdvertiseData {
        return AdvertiseData.Builder()
            .setIncludeDeviceName(false)
            .setIncludeTxPowerLevel(false)
            .addServiceData(
                ParcelUuid(PAX_SERVICE_FF),
                PAX_DATA_FF91.toByteArray()
            )
            .build()
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
}