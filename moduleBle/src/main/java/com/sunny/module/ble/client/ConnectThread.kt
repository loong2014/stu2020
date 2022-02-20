package com.sunny.module.ble.client

import android.bluetooth.BluetoothDevice
import android.content.Context
import com.sunny.module.ble.BleConfig
import com.sunny.module.ble.client.ble.ClientConnectThreadBle
import com.sunny.module.ble.client.classic.ClientConnectThreadClassic

fun buildConnectThreadBle(
    context:Context,
    device: BluetoothDevice,
    callback: ClientConnectCallback
): ClientConnectThread {
    return ClientConnectThreadBle(context,device, callback, BleConfig.getBleClientThreadName())
}

fun buildConnectThreadClassic(
    device: BluetoothDevice,
    callback: ClientConnectCallback
): ClientConnectThread {
    return ClientConnectThreadClassic(device, callback, BleConfig.getBleClientThreadName())
}

interface ClientConnectCallback {
    fun onStateChanged(oldState: String, newState: String)

    fun onReadMsg(msg: String)

    fun onWriteState(isOk: Boolean, seq: Int)
}

/**
 * 进行与服务端进行socket的线程
 */
open class ClientConnectThread(name: String) : Thread(name) {

    open fun write(msg: String, seq: Int): Boolean = false

    open fun doCancel() {}

}