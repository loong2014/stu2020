package com.sunny.module.web.ble

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.content.pm.PackageManager
import java.util.*

const val PAX_UUID_PRE: String = "com.ff.iai.paxlauncher.uuid"
val PAX_UUID_SERVICE: UUID = UUID.fromString("$PAX_UUID_PRE.service")
val PAX_UUID_WRITE: UUID = UUID.fromString("$PAX_UUID_PRE.write")

object BleTools {


    /**
     * 是否支持蓝牙
     */
    fun isSupportBluetooth(): Boolean {
        return BluetoothAdapter.getDefaultAdapter() != null
    }

    /**
     * 是否支持低功耗蓝牙
     */
    fun isSupportBLE(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
    }

    /**
     * 蓝牙是否开启
     */
    fun isBleEnabled(): Boolean {
        return BluetoothAdapter.getDefaultAdapter()?.isEnabled == true
    }

    /**
     * 判断是否有访问位置的权限，没有权限，直接申请位置权限
     */
    fun checkBlePermission(activity: Activity, requestCode: Int): Boolean {
        if ((activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            || (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        ) {
            activity.requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), requestCode
            )
            return false
        }
        return true
    }

    /**
     * 发送信息
     */
    fun doMsgSend(bluetoothGatt: BluetoothGatt, msg: String) {
        val gattService = bluetoothGatt.getService(PAX_UUID_SERVICE)
        val characteristic = gattService.getCharacteristic(PAX_UUID_WRITE)

        val bytes: ByteArray = msg.toByteArray()
        characteristic.value = bytes
        characteristic.writeType = BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE;
        bluetoothGatt.writeCharacteristic(characteristic)
    }
}