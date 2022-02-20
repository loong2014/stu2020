package com.sunny.module.ble.client.ble

import android.bluetooth.*
import android.content.Context
import com.sunny.module.ble.BleConfig
import com.sunny.module.ble.client.ClientConnectCallback
import com.sunny.module.ble.client.ClientConnectThread
import com.sunny.module.ble.client.SampleGattAttributes

/**
 * 进行与服务端进行socket的线程
 */
class ClientConnectThreadBle(
    private val context: Context,
    private val device: BluetoothDevice,
    private val callback: ClientConnectCallback, name: String
) : ClientConnectThread(name) {

    var curState = "None"

    private var canExit = false

    private fun updateState(state: String) {
        log(state)
        callback.onStateChanged(curState, state)
        curState = state
    }

    private var mmBluetoothGatt: BluetoothGatt? = null

    override fun write(msg: String, seq: Int): Boolean {
        return false
    }

    override fun doCancel() {
        super.doCancel()
        updateState("close")
        try {
            mmBluetoothGatt?.disconnect()
            mmBluetoothGatt?.close()
            mmBluetoothGatt = null
        } catch (e: Exception) {
            log("Could not close the client socket :$e")
        }
    }

    fun readCharacteristic(characteristic: BluetoothGattCharacteristic) {
        mmBluetoothGatt?.readCharacteristic(characteristic)
    }

    fun setCharacteristicNotification(
        characteristic: BluetoothGattCharacteristic,
        enabled: Boolean
    ) {
        mmBluetoothGatt?.setCharacteristicNotification(characteristic, enabled)
    }

    override fun run() {
        updateState("run")

        try {
            mmBluetoothGatt = device.connectGatt(context, false, gattCallback)
            updateState("connectGatt")
        } catch (e: Exception) {
            updateState("connectFailed")
            log("connect failed :$e")
        }
        canExit = true
        while (canExit) {

        }
    }

    //    readCharacteristic
    private var gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            log("onConnectionStateChange status($status) , newState($newState)")
            when (newState) {
                // 连接成功
                BluetoothProfile.STATE_CONNECTED -> {
                    log("onConnectionStateChange   STATE_CONNECTED")

                    log("onConnectionStateChange discoverServices")
                    mmBluetoothGatt?.discoverServices()
                }

                BluetoothProfile.STATE_DISCONNECTED -> {
                    log("onConnectionStateChange Disconnected from GATT server.")
                }
            }
        }

        // New services discovered
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            log("onServicesDiscovered  status($status)")
            when (status) {
                BluetoothGatt.GATT_SUCCESS -> {
                    log("GATT_SUCCESS")
                    displayGattServices(mmBluetoothGatt?.services)
                }
                else -> {
                    log("onServicesDiscovered received: $status")
                    canExit = true
                }
            }
        }

        // Result of a characteristic read operation
        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            log("onCharacteristicRead($status) , characteristic :$characteristic")
            characteristic.value?.let {
                callback.onReadMsg(String(it))
            }
            when (status) {
                BluetoothGatt.GATT_SUCCESS -> {
                    log("GATT_SUCCESS")
                }
            }
        }
    }

    private fun displayGattServices(gattServices: List<BluetoothGattService>?) {
        log("displayGattServices size(${gattServices?.size})")
        if (gattServices == null) return

        val sb = StringBuilder()
        sb.append("\ndisplayGattServices size(${gattServices.size})")
        // Loops through available GATT Services.
        gattServices.forEachIndexed { index1, gattService ->
            val characteristics = gattService.characteristics
            sb.append("\n>>>($index1) --- gattService :${gattService.uuid} --- characteristics(${characteristics?.size})")

            // Loops through available Characteristics.
            characteristics?.forEachIndexed { index2, gattCharacteristic ->
                sb.append("\n>>>>>>($index2) --- gattCharacteristic :${gattCharacteristic.uuid}")
            }
            sb.append("\n")
        }
        log(sb.toString())
    }

    private fun log(msg: String) {
        val log = "$this --- $device --- :$msg"
        BleConfig.bleLog(log)
    }
}