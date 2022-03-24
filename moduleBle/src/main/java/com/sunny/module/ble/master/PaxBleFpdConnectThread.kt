package com.sunny.module.ble.master

import android.bluetooth.BluetoothDevice
import android.content.Context

class PaxBleFpdConnectThread(context: Context) : PaxBleConnectThread(context, "fpd") {

    override fun getFFID(): String? {
        return null
    }

    override fun doConnect(device: BluetoothDevice) {
    }

    override fun doDisConnect() {
    }
}