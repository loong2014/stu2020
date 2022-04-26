package com.sunny.module.ble.master.helper

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Handler
import android.os.ParcelUuid
import com.sunny.module.ble.SEAT_FPD_NAME
import com.sunny.module.ble.SEAT_RSD_NAME
import com.sunny.module.ble.master.PaxBleConnectCallback
import com.sunny.module.ble.master.PaxBleConnectModel
import timber.log.Timber

/**
 * BLE连接辅助类
 */
class PaxBleConnectHelper(
    val context: Context,
    val mWorkHandler: Handler,
    val mMainHandler: Handler
) {

    // 用户信息
    private val fpdBleUserInfo = PaxBleUserHelper(SEAT_FPD_NAME)
    private val rsdBleUserInfo = PaxBleUserHelper(SEAT_RSD_NAME)

    // 用于建立连接
    private var tmpConnectThread: PaxBleConnectModel? = null

    // 无效的BLE列表，不包含serviceUUID，且建立连接后与当前用户不匹配
    private val invalidFFDeviceList = mutableListOf<String>()

    /**
     * 是否有用户登陆
     */
    fun hasUserLogin(): Boolean {
        return fpdBleUserInfo.hasUserInfo() || rsdBleUserInfo.hasUserInfo()
    }

    /**
     * 更新用户信息
     */
    fun updateUserInfo(seatName: String?, uid: Int, ffid: String?) {
        val bleUserInfo = when (seatName) {
            SEAT_FPD_NAME -> fpdBleUserInfo
            SEAT_RSD_NAME -> rsdBleUserInfo
            else -> return
        }

        // 用户发生变化
        bleUserInfo.updateUserInfo(uid, ffid)
    }

    /**
     * 是否包含serviceUUID并且与当前用户不匹配
     */
    private fun hasServiceUuidsAndNotMatch(result: ScanResult): Boolean {
        val serviceUuids = result.scanRecord?.serviceUuids
        if (serviceUuids.isNullOrEmpty()) {
            // 不包含serviceUuids
            return false
        }
        var uuid = rsdBleUserInfo.curServiceUUID
        if (uuid != null && serviceUuids.contains(ParcelUuid(uuid))) {
            // 与RSD匹配
            return false
        }

        uuid = fpdBleUserInfo.curServiceUUID
        if (uuid != null && serviceUuids.contains(ParcelUuid(uuid))) {
            // 与FPD匹配
            return false
        }

        // 包含serviceUUID且与登录用户不匹配
        return true
    }

    /**
     * 停止连接
     */
    fun dealStopConnect() {
        showLog("doUserDisconnect  by stop connect")
        fpdBleUserInfo.doDisconnect()
        rsdBleUserInfo.doDisconnect()

        tmpConnectThread?.doDisconnect()
        tmpConnectThread = null

        invalidFFDeviceList.clear()
    }

    var num = 0

    /**
     * 处理扫描到的设备
     */
    fun dealScanResult(result: ScanResult) {
        // 1、正在进行连接(同一时刻，只能有一个设备处于正在连接状态)
        if (tmpConnectThread != null) {
            return
        }
        showLog("dealScanResult :${result.device.name} , ${result.device}")


        // 2、设备名称不以 ".FF" 结尾
        if (result.device.name?.endsWith(".FF") != true) {
            return
        }

        // 3、已经与RSD/FPD建立连接
        if (rsdBleUserInfo.isConnected(result) || fpdBleUserInfo.isConnected(result)) {
            showLog("${result.device.name} already connected")
            return
        }

        val serviceUuids = result.scanRecord?.serviceUuids
        val device = result.device

        if (serviceUuids.isNullOrEmpty()) {
            // 4、不包含serviceUuids，
            if (invalidFFDeviceList.contains(device.address)) {
                // 已经验证过的设备
                showLog("${device.name} already connect and not match")
            } else {
                // 未验证的设备
                doConnect(device)
            }

        } else {
            // 5、包含serviceUUID
            var uuid = rsdBleUserInfo.curServiceUUID
            if (uuid != null && serviceUuids.contains(ParcelUuid(uuid))) {
                // 与RSD匹配
                doConnect(device)
                return
            }

            uuid = fpdBleUserInfo.curServiceUUID
            if (uuid != null && serviceUuids.contains(ParcelUuid(uuid))) {
                // 与FPD匹配
                doConnect(device)
                return
            }
            showLog("${device.name} has serviceUUID but not match login user")

        }
    }

    /**
     * 进行connect
     * 1、serviceUUID为空，FFCtrl处于后台
     * 2、serviceUUID 与当前登录用户匹配
     */
    private fun doConnect(device: BluetoothDevice) {
        if (tmpConnectThread != null) return

        tmpConnectThread = PaxBleConnectModel(
            context,
            mWorkHandler,
            mMainHandler,
            mmConnectCallback
        )

        showLog("doConnect(${num++}) :${device.name} >>> ${device.address}")
        tmpConnectThread?.doConnect(device)
    }

    private val mmConnectCallback = object : PaxBleConnectCallback {

        override fun onServicesDiscovered(bluetoothGatt: BluetoothGatt) {

            val sb = StringBuilder()
            sb.append("onServicesDiscovered size(${bluetoothGatt.services?.size})")
            bluetoothGatt.services?.forEachIndexed { index, bluetoothGattService ->
                sb.append("\n$index >>> ${bluetoothGattService.uuid}")
            }
            showLog((sb.toString()))

            // 如果在无效BLE设备列表中，先移除
            invalidFFDeviceList.remove(bluetoothGatt.device.address)

            when {
                rsdBleUserInfo.isTargetDevice(bluetoothGatt) -> {
                    // 与RSD匹配
                    showLog("RSD BLE has connected")
                    rsdBleUserInfo.setBleConnectThread(tmpConnectThread)
                    tmpConnectThread = null
                }
                fpdBleUserInfo.isTargetDevice(bluetoothGatt) -> {
                    // 与FPD匹配
                    showLog("FPD BLE has connected")
                    fpdBleUserInfo.setBleConnectThread(tmpConnectThread)
                    tmpConnectThread = null
                }
                else -> {
                    // 设备与当前登录用户不匹配，添加到无效列表
                    showLog("add invalid device ${bluetoothGatt.device.name}(${bluetoothGatt.device}) by not match login user")
                    invalidFFDeviceList.add(bluetoothGatt.device.address)

                    // 断开连接
                    showLog("doDisconnect by invalid device")
                    tmpConnectThread?.doDisconnect()
                    tmpConnectThread = null
                }
            }
        }

        override fun onConnectFailed(device: BluetoothDevice) {
            tmpConnectThread = null
        }

        override fun onDisconnected(device: BluetoothDevice, byUser: Boolean) {
            showLog("onDisconnected byUser($byUser) :$device")
            tmpConnectThread = null
        }
    }

    /**
     * 显示日志
     */
    fun showLog(log: String) {
        Timber.i(log)
    }
}
