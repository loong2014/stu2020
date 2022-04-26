package com.sunny.module.ble.master.helper

import android.bluetooth.BluetoothGatt
import android.bluetooth.le.ScanResult
import com.sunny.module.ble.PaxBleConfig
import com.sunny.module.ble.master.PaxBleConnectModel
import timber.log.Timber
import java.util.*

class PaxBleUserHelper(val seatName: String) {

    // 用户信息
    var curUserId: Int = -1
    var curFFID: String? = null
    var curServiceUUID: UUID? = null
    var phoneAuthKey: ByteArray = byteArrayOf()
    var vehicleAuthKey: ByteArray = byteArrayOf()

    // 用户对应的BLE连接
    private var mmConnectThread: PaxBleConnectModel? = null

    /**
     * 是否包含用户信息
     */
    fun hasUserInfo(): Boolean {
        showLog("$curUserId :$curFFID >>> $curServiceUUID")
        return curFFID != null
    }

    /**
     * 用户发生变化
     */
    fun updateUserInfo(uid: Int, ffid: String?): Boolean {
        // 用户未变化
        if (curFFID == ffid) {
            return false
        }

        // 用户发生变化，断开当前连接
        doDisconnect()

        if (uid <= 0 || ffid.isNullOrEmpty()) {
            // 用户登出
            curUserId = -1
            curFFID = null
            curServiceUUID = null
            phoneAuthKey = byteArrayOf()
            vehicleAuthKey = byteArrayOf()
        } else {
            // 用户登录
            curUserId = uid
            curFFID = ffid
            curServiceUUID = PaxBleConfig.buildUUIDByFFID(curFFID)
            phoneAuthKey = PaxBleConfig.buildPhoneAuthKeyArray(curFFID)
            vehicleAuthKey = PaxBleConfig.buildVehicleAuthKeyArray(curFFID)
        }
        return true
    }

    /**
     * 是否是目标设备
     */
    fun isTargetDevice(bluetoothGatt: BluetoothGatt): Boolean {
        if (curServiceUUID == null) return false
        return bluetoothGatt.getService(curServiceUUID) != null
    }

    /**
     * 是否已经连接
     */
    fun isConnected(result: ScanResult): Boolean {
        return mmConnectThread?.isConnected(result.device) ?: false
    }

    /**
     * 设置当前用户对应的BLE连接
     */
    fun setBleConnectThread(connectThread: PaxBleConnectModel?) {
        if (mmConnectThread != null) {
            mmConnectThread?.doDisconnect(true)
            mmConnectThread = null
        }
        mmConnectThread = connectThread
        mmConnectThread?.setUserInfo(this)
        mmConnectThread?.dealStartNotifyAndAuth()
    }

    /**
     * 断开连接
     */
    fun doDisconnect() {
        // 先断开连接，再清数据
        mmConnectThread?.onPaxDisconnect()
        mmConnectThread = null
    }

    /**
     * 通知用户数据发生变化
     */
    fun notifyUserDataChanged(byteArray: ByteArray?) {
        showLog("notifyUserDataChanged($curUserId , $curFFID) len :${byteArray?.size}")
//        mWorkHandler.post {
//            if (curUserId > 0 && curFFID != null) {
//                val data = Bundle()
//                data.putByteArray(PaxUserConfig.EXT_MEETING_INFO, byteArray)
//
//                val intent = Intent(PaxUserConfig.ACTION_MEETING)
//                intent.putExtras(data)
//
//                val userHandle = PaxUserConfig.getUserHandle(curUserId)
//                LibCoreConfig.sendBroadcastAsUser(intent, userHandle)
//            }
//        }
    }

    /**
     * 显示日志
     */
    fun showLog(log: String) {
        Timber.i("$seatName-$log")
    }
}