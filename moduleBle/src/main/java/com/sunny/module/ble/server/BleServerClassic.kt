package com.sunny.module.ble.server

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import com.sunny.module.ble.BleConfig
import com.sunny.module.ble.BleConfig.MSG_SERVICE_CONNECTED
import com.sunny.module.ble.BleConfig.PAX_BLE_UUID
import com.sunny.module.ble.BleTools
import java.io.InputStream
import java.io.OutputStream

/**
 * 传统蓝牙
 * https://developer.android.com/guide/topics/connectivity/bluetooth?hl=zh-cn#FindDevices
 */
class BleServerClassic : BleBaseServerService() {

    var mConnectThread: ConnectThread? = null

    override fun doInit() {
        showLog("doInit")
        mConnectThread = ConnectThread(BleConfig.getBleServerThreadName())
        mConnectThread?.start()
        showLog("服务已开启")
    }

    override fun doRelease() {
        showLog("doRelease")
        mConnectThread?.doExit()
        mConnectThread?.interrupt()
        mConnectThread = null
        super.doRelease()
    }

    fun sendServerState(tip: String) {
        bleCallback?.onConnectStateChanged(1, tip)
        showLog(tip)
    }

    private fun showLog(msg: String) {
        BleConfig.bleLog("$mConnectThread -> ", msg)
    }

    private fun dealRcvMsg(msg: String) {
        showLog("dealRcvMsg msg :$msg")
        bleCallback?.onRcvClientMsg(msg)
    }

    inner class ConnectThread(name: String) : Thread(name) {
        private var mBluetoothServerSocket: BluetoothServerSocket? = null
        private var bluetoothSocket: BluetoothSocket? = null
        private var remoteDevice: BluetoothDevice? = null
        private var outputStream: OutputStream? = null
        private var inputStream: InputStream? = null

        // 循环连接设备
        private var loopAcceptClient = false

        // 是否继续读取当前连接设备的信息
        private var isContinueClientRead = false

        init {
            mBluetoothServerSocket = bluetoothAdapter?.listenUsingRfcommWithServiceRecord(
                "com.ff.iai.pax.server", PAX_BLE_UUID
            )
        }

        fun doExit() {
            loopAcceptClient = true
            doCloseClientSocket()

            doCloseBleSocket()
        }

        /**
         * 关闭与蓝牙设备建立的连接，蓝牙设备最多可建立的连接有限，退出时，需要关闭
         */
        private fun doCloseBleSocket() {
            mBluetoothServerSocket?.close()
            mBluetoothServerSocket = null
        }

        /**
         * 关闭当前与客户端相关的连接
         */
        private fun doCloseClientSocket() {
            log("doCloseClientSocket : $bluetoothSocket")
            inputStream?.close()
            inputStream = null

            outputStream?.close()
            outputStream = null

            bluetoothSocket?.close()
            bluetoothSocket = null

            isContinueClientRead = false
        }

        private fun onRemoteDeviceConnected() {
            showLog("S设备连接成功(${bluetoothSocket?.connectionType})")
            remoteDevice?.run {
                val tip = "$name --- $address"
                bleCallback?.onConnectStateChanged(BleConfig.MSG_SERVICE_DEVICE_CONNECTED, tip)
                showLog(tip)
            }
        }

        override fun run() {

            while (!loopAcceptClient) {

                if (mBluetoothServerSocket == null) {
                    sendServerState("S蓝牙服务连接失败")
                    doExit()
                    return
                }

                try {
                    sendServerState("S等待设备连接")
                    bluetoothSocket = mBluetoothServerSocket?.accept()
                    remoteDevice = bluetoothSocket?.remoteDevice
                    onRemoteDeviceConnected()
                    inputStream = bluetoothSocket?.inputStream
                    outputStream = bluetoothSocket?.outputStream

                    // 回复连接成功的消息给client
                    BleTools.doSendMsg(MSG_SERVICE_CONNECTED, outputStream)

                    isContinueClientRead = true
                    val buffer = ByteArray(128)
                    while (isContinueClientRead) {
                        val count = inputStream?.read(buffer) ?: 0

                        if (count > 0) {
                            val msg = String(buffer, 0, count)
                            dealRcvMsg(msg)
                        }
                    }
                    sendServerState("S设备连接断开")
                } catch (e: Exception) {
                    e.printStackTrace()
                    sendServerState("C设备连接断开")
                } finally {
                    doCloseClientSocket()
                }
            }
        }
    }
}