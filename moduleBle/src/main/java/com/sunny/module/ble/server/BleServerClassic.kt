package com.sunny.module.ble.server

import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import com.sunny.module.ble.BleBaseService
import com.sunny.module.ble.BleConfig.PAX_BLE_NAME
import com.sunny.module.ble.BleConfig.PAX_BLE_UUID
import java.io.InputStream
import java.io.OutputStream

/**
 * 传统蓝牙
 * https://developer.android.com/guide/topics/connectivity/bluetooth?hl=zh-cn#FindDevices
 */
class BleServerClassic : BleBaseService() {

    var mConnectThread: ConnectThread? = null

    override fun doInit() {
        mConnectThread = ConnectThread()
        mConnectThread?.start()
        showTipInfo("接收服务已开启")
    }

    override fun doRelease() {
        mConnectThread?.doExit()
        super.doRelease()
    }

    inner class ConnectThread : Thread() {
        private var mBluetoothServerSocket: BluetoothServerSocket? = null
        private var bluetoothSocket: BluetoothSocket? = null
        private var outputStream: OutputStream? = null
        private var inputStream: InputStream? = null
        private var isContinue = false

        private var isExit = false

        init {
            mBluetoothServerSocket = bluetoothAdapter?.listenUsingRfcommWithServiceRecord(
                PAX_BLE_NAME, PAX_BLE_UUID
            )
        }

        fun doExit() {
            isExit = true
        }

        override fun run() {
            while (!isExit) {
                if (mBluetoothServerSocket == null) {
                    showTipInfo("mBluetoothServerSocket is null")
                    return
                }
                try {
                    showTipInfo("准备建立连接")
                    bluetoothSocket = mBluetoothServerSocket?.accept();
                    inputStream = bluetoothSocket?.inputStream
                    outputStream = bluetoothSocket?.outputStream

                    isContinue = true
                    while (isContinue) {
                        val buffer = ByteArray(128)
                        val count = inputStream?.read(buffer) ?: 0

                        val msg = String(buffer, 0, count)
                        log("read msg :$msg")
//                        showTipInfo("read msg :$msg")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    showTipInfo("run error :$e")
                    isContinue = false
                } finally {
                    bluetoothSocket?.close()
                }
            }
        }
    }

}