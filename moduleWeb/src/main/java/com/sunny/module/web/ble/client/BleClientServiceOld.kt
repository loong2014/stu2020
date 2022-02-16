package com.sunny.module.web.ble.client

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import com.sunny.module.web.ble.BleConfig
import com.sunny.module.web.ble.BleTools
import com.sunny.module.web.ble.BleTools.PAX_BLE_UUID
import com.sunny.module.web.ble.PaxBleServiceBase
import java.io.OutputStream

class BleClientServiceOld : PaxBleServiceBase() {

    private var mBluetoothDevice: BluetoothDevice? = null
    private var mBluetoothSocket: BluetoothSocket? = null
    private var mOutputStream: OutputStream? = null

    override fun tryStartBleScan(restart: Boolean): Boolean {
        if (mBluetoothDevice == null) {
            mBluetoothDevice = bluetoothAdapter?.getRemoteDevice(BleConfig.curBleTarget.second)
            log("getRemoteDevice :$mBluetoothDevice")
        }

        if (mBluetoothSocket?.isConnected != true) {
            mBluetoothSocket?.close()
            mBluetoothSocket = null
        }

        if (mBluetoothSocket == null) {
//            mBluetoothSocket = mBluetoothDevice?.createInsecureRfcommSocketToServiceRecord(PAX_BLE_UUID)
            mBluetoothSocket = mBluetoothDevice?.createRfcommSocketToServiceRecord(PAX_BLE_UUID)
            mBluetoothSocket?.connect()
            mOutputStream = mBluetoothSocket?.outputStream
        }
        return true
    }

    override fun trySendMsg(msg: String): Boolean {
        if (mOutputStream != null) {
            try {
                mOutputStream?.write(msg.toByteArray())
                showTipInfo("trySendMsg succeed")
                return true
            } catch (e: Exception) {
                e.printStackTrace()
                showTipInfo("trySendMsg error :$e")
            }
        } else {
            showTipInfo("trySendMsg os is null")
        }
        return false
    }

    override fun tryStopBleScan(): Boolean {
        return true
    }
}