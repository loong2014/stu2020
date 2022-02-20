package com.sunny.module.ble.client.classic

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import com.sunny.module.ble.BleConfig
import com.sunny.module.ble.client.ClientConnectCallback
import com.sunny.module.ble.client.ClientConnectThread
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 * 进行与服务端进行socket的线程
 */
class ClientConnectThreadClassic(
    device: BluetoothDevice,
    private val callback: ClientConnectCallback, name: String
) : ClientConnectThread(name) {

    var curState = "None"

    private fun updateState(state: String) {
        log(state)
        callback.onStateChanged(curState, state)
        curState = state
    }

    private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
        device.createRfcommSocketToServiceRecord(BleConfig.PAX_BLE_UUID)
    }

    private var mmOutStream: OutputStream? = null
    private var mmInStream: InputStream? = null

    override fun write(msg: String, seq: Int): Boolean {
        log("write($seq) :$msg")

        val isOk: Boolean = try {
            mmOutStream?.write(msg.toByteArray())
            true
        } catch (e: Exception) {
            log("write failed :$e")
            false
        }
        callback.onWriteState(isOk, seq)
        return isOk
    }

    override fun doCancel() {
        super.doCancel()
        updateState("close")
        try {
            mmSocket?.close()
        } catch (e: Exception) {
            log("Could not close the client socket :$e")
        }
    }

    override fun run() {
        updateState("run")

        try {
            mmSocket?.connect()
            updateState("connectSucceed")
        } catch (e: Exception) {
            updateState("connectFailed")
            log("connect failed :$e")
        }

        if (mmSocket?.isConnected == true) {
            mmOutStream = mmSocket?.outputStream
            mmInStream = mmSocket?.inputStream
            updateState("isConnected os($mmOutStream) , is($mmInStream)")

            val buffer = ByteArray(1024)
            var numBytes: Int

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                // Read from the InputStream.
                numBytes = try {
                    mmInStream?.read(buffer) ?: -1
                } catch (e: IOException) {
                    log("read failed :$e")
                    break
                }

                val msg = String(buffer, 0, numBytes)
                log("readMsg :$msg")
                callback.onReadMsg(msg)
            }
        }
    }

    private fun log(msg: String) {
        val log = "$this --- $mmSocket --- :$msg"
        BleConfig.bleLog(log)
    }
}