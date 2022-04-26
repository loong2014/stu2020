package com.sunny.module.ble.master

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.content.Context
import android.os.Handler
import com.sunny.module.ble.master.helper.PaxBleUserHelper
import timber.log.Timber

abstract class PaxBleConnectModelBase(
    val context: Context,
    val mWorkHandler: Handler,
    val mMainHandler: Handler,
    private val callback: PaxBleConnectCallback
) {
    protected var logTag = "NONE"

    protected var mmUserInfo: PaxBleUserHelper? = null

    protected var curDevice: BluetoothDevice? = null

    /**
     * 设置用户信息
     */
    fun setUserInfo(userInfo: PaxBleUserHelper) {
        logTag = userInfo.seatName
        mmUserInfo = userInfo
    }

    /**
     * 是否已连接
     */
    fun isConnected(device: BluetoothDevice): Boolean {
        return device == curDevice
    }

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
     * 连接失败
     * 1、建立连接失败
     * 2、连接断开
     */
    protected fun onConnectFiled() {
        curDevice?.let {
            showLog("doDisconnect by connect failed")
            doDisconnect()
            callback.onConnectFailed(it)
        }
        curDevice = null
    }

    /**
     * 发现服务
     */
    protected fun onDiscoveredResponse(gatt: BluetoothGatt?) {
        showLog("onDiscoveredResponse :$gatt")
        if (gatt != null) {
            callback.onServicesDiscovered(gatt)
        } else {
            curDevice?.let {
                showLog("doDisconnect by discovered failed")
                doDisconnect()
                callback.onConnectFailed(it)
            }
            curDevice = null
        }
    }

    /**
     * 鉴权结果
     */
    protected fun onAuthResponse(succeed: Boolean) {
        showLog("onAuthResponse :$succeed")
        if (succeed) return
        curDevice?.let {
            showLog("doDisconnect by auth failed")
            doDisconnect()
            callback.onConnectFailed(it)
        }
        curDevice = null
    }

    /**
     * FFCtrl 断开连接
     */
    protected fun onPhoneDisconnect() {
        showLog("onPhoneDisconnect")

        // 更新日历信息
        mmUserInfo?.notifyUserDataChanged(null)

        curDevice?.let {
            showLog("doDisconnect phone disconnect")
            doDisconnect()
            callback.onDisconnected(it, true)
        }
        curDevice = null
    }

    /**
     * PaxLauncher 断开连接
     */
    fun onPaxDisconnect() {
        showLog("onPaxDisconnect")

        // 更新日历信息
        mmUserInfo?.notifyUserDataChanged(null)

        curDevice?.let {
            showLog("doDisconnect pax disconnect")
            doDisconnect()
            callback.onDisconnected(it, true)
        }
        curDevice = null
    }

    /**
     * 收到日历信息
     */
    protected fun onRevCalendarData(byteArray: ByteArray) {
        showLog("onRevCalendarData size :${byteArray.size}")
        mmUserInfo?.notifyUserDataChanged(byteArray)
    }

    protected fun showLog(log: String) {
        Timber.i("$logTag-$log")
    }
}