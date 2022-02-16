package com.sunny.module.ble

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

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
     * 获取对配对设备
     */
    fun getBondedDevices(): Set<BluetoothDevice>? {
        val adapter = BluetoothAdapter.getDefaultAdapter()
        return adapter.bondedDevices
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

    fun buildOneDeviceShowMsg(device: BluetoothDevice, tag: String): String {
        return "$tag -> $device(${device.type}) , ${device.name} , ${device.uuids}"
    }

    fun buildDevicesShowMsg(
        devices: Collection<BluetoothDevice>?,
        tag: String,
        showUUID: Boolean = false
    ): String {
        val sb = StringBuilder()
        sb.append("### >>>> $tag(${devices?.size})")
        devices?.forEachIndexed { index, device ->
            sb.append("\n$index -> $device(${device.type}) , ${device.name}")
            if (showUUID) {
                sb.append(" , ${device.uuids}")
            }
        }
        sb.append("\n### <<<< $tag")
        return sb.toString()
    }

    fun isBleDiscoverableEnable(): Boolean {
        val adapter = BluetoothAdapter.getDefaultAdapter()
        return adapter.isDiscovering
    }

    /**
     * 打开设备的可见性
     */
    fun openDiscoverable(context: Context) {
        val adapter = BluetoothAdapter.getDefaultAdapter()
        try {
            val c = adapter.javaClass
            val setDiscoverableTimeout = c.getMethod("setDiscoverableTimeout", Int::class.java)
            setDiscoverableTimeout.isAccessible = true
            setDiscoverableTimeout.invoke(adapter, 300)

            val setScanMode = c.getMethod("setScanMode", Int::class.java)
            setScanMode.isAccessible = true
            // 设备处于可检测到模式。
            setScanMode.invoke(adapter, BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE)
        } catch (e: Exception) {
            e.printStackTrace()
            log("openDiscoverable error :$e")
            openDiscoverableWithOptDialog(context)
        }
    }

    /**
     * 关闭设备可见性
     */
    fun closeDiscoverable() {
        val adapter = BluetoothAdapter.getDefaultAdapter()
        try {
            val c = BluetoothAdapter::class.java
            val setDiscoverableTimeout = c.getMethod("setDiscoverableTimeout", Int::class.java)
            setDiscoverableTimeout.isAccessible = true
            setDiscoverableTimeout.invoke(adapter, 1)

            val setScanMode = c.getMethod("setScanMode", Int::class.java)
            setScanMode.isAccessible = true
            // 设备未处于可检测到模式，但仍能收到连接。
            setScanMode.invoke(adapter, BluetoothAdapter.SCAN_MODE_CONNECTABLE)
        } catch (e: Exception) {
            e.printStackTrace()
            log("closeDiscoverable error :$e")
        }
    }

    /**
     * 打开设备的可见性
     */
    private fun openDiscoverableWithOptDialog(context: Context) {
        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
            // 默认情况下，设备处于可检测到模式的时间为 120 秒（2 分钟）
            // 最高可达 3600 秒（1 小时）
            putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
        }
        context.startActivity(intent)
    }

    /**
     * 发送信息
     */
    fun doMsgSend(bluetoothGatt: BluetoothGatt, msg: String) {
        val gattService = bluetoothGatt.getService(BleConfig.PAX_UUID_SERVICE)
        val characteristic = gattService.getCharacteristic(BleConfig.PAX_UUID_WRITE)

        val bytes: ByteArray = msg.toByteArray()
        characteristic.value = bytes
        characteristic.writeType = BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE;
        bluetoothGatt.writeCharacteristic(characteristic)
    }

    private fun log(msg: String) {
        BleConfig.bleLog("BleTools", msg)
    }
}