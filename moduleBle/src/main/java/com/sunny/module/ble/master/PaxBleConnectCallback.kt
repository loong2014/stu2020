package com.sunny.module.ble.master

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt

/**
 * BLE连接回调
 */
interface PaxBleConnectCallback {

    /**
     * 发现服务
     */
    fun onServicesDiscovered(bluetoothGatt: BluetoothGatt)

    /**
     * 连接失败
     */
    fun onConnectFailed(device: BluetoothDevice)

    /**
     * 断开连接
     */
    fun onDisconnected(device: BluetoothDevice, byUser: Boolean)
}