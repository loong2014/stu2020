package com.sunny.module.web.ble.service

import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import com.sunny.module.web.ble.BleTools.PAX_BLE_UUID
import com.sunny.module.web.ble.PAX_BLE_NAME
import com.sunny.module.web.ble.PaxBleServiceBase
import java.io.InputStream
import java.io.OutputStream

class BleConnectServiceOld : PaxBleServiceBase() {

    var mAcceptThread: AcceptThread? = null

    override fun tryStartBleScan(restart: Boolean): Boolean {
        if (mAcceptThread != null) {
            mAcceptThread?.doExit()
            mAcceptThread = null
        }
        mAcceptThread = AcceptThread()
        mAcceptThread?.start()
        return true
    }

    inner class AcceptThread : Thread() {
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
                    bluetoothSocket = mBluetoothServerSocket?.accept();
                    inputStream = bluetoothSocket?.inputStream
                    outputStream = bluetoothSocket?.outputStream

                    isContinue = true
                    while (isContinue) {
                        val buffer = ByteArray(128)
                        val count = inputStream?.read(buffer) ?: 0

                        val msg = String(buffer, 0, count)
                        log("read msg :$msg")
                        showTipInfo("read msg :$msg")
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

    override fun trySendMsg(msg: String): Boolean {
        return false
    }

    override fun tryStopBleScan(): Boolean {

        return true
    }
}