package com.sunny.module.ble.master

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Handler
import android.os.ParcelUuid
import com.sunny.module.ble.PaxBleConfig
import timber.log.Timber
import java.util.*

internal abstract class PaxBleConnectThreadBase(
    val context: Context,
    val mWorkHandler: Handler,
    val mMainHandler: Handler,
    val seatName: String
) {
    private var curUserId: Int = -1
    protected var curFFID: String? = null
    private var curServiceUUID: UUID? = null

    protected var curDevice: BluetoothDevice? = null

    // 是否正在连接
    private var isConnecting = false

    /**
     * 开始连接
     */
    abstract fun doConnect(device: BluetoothDevice)

    /**
     * 断开连接
     * @param byUser 是否是用户主动断开
     * 1、手机端主动断开
     * 2、用户端账号变化
     */
    abstract fun doDisconnect(byUser: Boolean = false)

    /**
     * 用户发生变化
     */
    fun updateUserInfo(uid: Int, ffid: String?) {
        // 用户未变化
        if (curFFID == ffid) {
            return
        }

        // 断开当前连接
        showLog("doDisconnect by user changed")
        doDisconnect(true)
        if (uid < 0 || ffid.isNullOrBlank()) {
            curUserId = -1
            curFFID = null
            curServiceUUID = null
        } else {
            curUserId = uid
            curFFID = ffid
            curServiceUUID = PaxBleConfig.buildUUIDByFFID(curFFID)
        }
    }

    /**
     * 获取当前用户的FFID 构建 ScanFilter
     */
    fun buildScanFilter(): ScanFilter? {
        showLog("buildScanFilter($curUserId) :$curFFID  >>> $curServiceUUID")
        return curServiceUUID?.let {
            ScanFilter.Builder()
                /*
                通过 ServiceUuid 进行过滤
                 */
                .setServiceUuid(ParcelUuid(it))
                /*
                在设备地址上设置过滤
                 */
//                .setDeviceAddress("")
                .build()
        }
    }

    /**
     * 处理BLE连接
     */
    fun dealConnect(result: ScanResult): Boolean {
        // 1、no user for connect
        if (curFFID == null) return false

        val hasMatch = result.scanRecord?.serviceUuids?.takeIf { it.isNotEmpty() }
            ?.contains(ParcelUuid(curServiceUUID))

        // 2、no match service
        if (hasMatch != true) return false

        // 3、already connected
        if (result.device == curDevice) {
            return true
        }

        showLog("tryConnect :${result.device.name} , ${result.device.address}")
        isConnecting = true
        curDevice = result.device
        doConnect(result.device)
        return true
    }

    /**
     * 是否正在连接中，BLE 中同一时刻，只能进行一台设备的连接，新的连接需要等当前设备连接成功/失败之后
     */
    fun inConnecting(): Boolean {
        return isConnecting
    }

    /**
     * 连接成功
     */
    protected fun onConnectSucceed() {
        isConnecting = false
    }

    /**
     * 连接失败
     */
    protected fun onConnectFiled() {
        isConnecting = false
        curDevice = null
    }

    /**
     * 鉴权失败
     */
    protected fun onAuthFiled() {
        isConnecting = false
        curDevice = null
    }

    /**
     * 断开连接
     */
    protected fun onDisconnect(byUser: Boolean = false) {
        isConnecting = false
        curDevice = null
        dealDisconnect()
    }

    /**
     * 开始监听手机端发送的信息
     */
    protected fun doSetNotify(gatt: BluetoothGatt?) {
        // 监听notify
        gatt?.getService(curServiceUUID)
            ?.getCharacteristic(PaxBleConfig.PAX_UUID_CALENDAR_NOTIFY)
            ?.run {
                if (gatt.setCharacteristicNotification(this, true)) {
                    getDescriptor(PaxBleConfig.PAX_UUID_NOTIFY_DESCRIPTOR)?.let { des ->
                        des.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                        gatt.writeDescriptor(des)
                    }
                }
            }

        // 监听readable
        gatt?.getService(curServiceUUID)
            ?.getCharacteristic(PaxBleConfig.PAX_UUID_CALENDAR_READABLE)
            ?.run {
                if (gatt.setCharacteristicNotification(this, true)) {

                    getDescriptor(PaxBleConfig.PAX_UUID_NOTIFY_DESCRIPTOR)?.let { des ->
                        des.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                        gatt.writeDescriptor(des)
                    }
                }
            }
    }

    /**
     * 获取鉴权对应的特征
     */
    protected fun getAuthCharacteristic(gatt: BluetoothGatt?) =
        gatt?.getService(curServiceUUID)
            ?.getCharacteristic(PaxBleConfig.PAX_UUID_CALENDAR_AUTH)


    /**
     * 获取日历读取对应的特征
     */
    protected fun getReadCharacteristic(gatt: BluetoothGatt?) =
        gatt?.getService(curServiceUUID)
            ?.getCharacteristic(PaxBleConfig.PAX_UUID_CALENDAR_READ)


    /**
     * 通知用户收到数据
     */
    protected fun dealRcvData(byteArray: ByteArray) {
        notifyUserDataChanged(byteArray)
    }

    /**
     * 通知用户连接断开
     */
    private fun dealDisconnect() {
        notifyUserDataChanged(null)
    }

    /**
     * 通知用户数据发生变化
     */
    private fun notifyUserDataChanged(byteArray: ByteArray?) {
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

    fun showLog(log: String) {
        Timber.i("PaxBle-$seatName-$log")
    }
}