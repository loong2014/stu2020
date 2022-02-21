package com.sunny.module.ble.pax

import android.bluetooth.*
import android.content.Context
import com.sunny.module.ble.BleConfig
import com.sunny.module.ble.client.SampleGattAttributes


interface BleConnectCallback {
    fun showRcvTip(msg: String)
}

private var slaveConnectThreadCount: Int = 0
fun buildSlaveConnectThread(
    context: Context,
    device: BluetoothDevice,
    callback: BleConnectCallback
): SlaveConnectThread {
    return SlaveConnectThread(context, device, callback, "Slave-${slaveConnectThreadCount++}")
}

/**
 * 从设备 发起与 主设备 的 socket 连接
 */
class SlaveConnectThread(
    private val context: Context,
    private val device: BluetoothDevice,
    private val callback: BleConnectCallback, name: String
) : Thread(name) {

    private var canExit = false
    private var mmBluetoothGatt: BluetoothGatt? = null

    private fun log(msg: String) {
        val log = "$this --- :$msg"
        BleConfig.bleLog(log)
    }

    private fun updateState(state: String) {
        log(state)
        callback.showRcvTip(state)
    }

    fun doCancel() {
        updateState("close")
        try {
            canExit = true
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
                    updateState("STATE_CONNECTED")
                    /*
                    发现远程设备提供的服务及其特征和描述符。
                     */
                    mmBluetoothGatt?.discoverServices()
                }

                BluetoothProfile.STATE_DISCONNECTED -> {
                    updateState("STATE_DISCONNECTED")
                }
            }
        }

        // New services discovered
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            updateState("onServicesDiscovered($status)")
            when (status) {
                BluetoothGatt.GATT_SUCCESS -> {
                    updateState("GATT_SUCCESS")
                    displayGattServices(mmBluetoothGatt?.services)
                }
                else -> {
                    updateState("GATT_OTHER($status)")
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
            updateState("onCharacteristicRead($status)")
            characteristic.value?.let {
                callback.showRcvTip("Read:${String(it)}")
            }
            when (status) {
                BluetoothGatt.GATT_SUCCESS -> {
                    log("GATT_SUCCESS")
                }
            }
        }
    }

    /**
     * 显示远程设备的服务特征
     */
    private fun displayGattServices(gattServices: List<BluetoothGattService>?) {
        if (gattServices == null) return
        log("displayGattServices size(${gattServices?.size})")

        var uuid: String?

        // Loops through available GATT Services.
        for (gattService in gattServices) {
            uuid = gattService.uuid.toString()
            updateState("$device >>> $uuid")
            log(
                "displayGattServices gattService --- $uuid >>> ${
                    SampleGattAttributes.lookup(
                        uuid,
                        "Unknown device"
                    )
                }"
            )

            val gattCharacteristics = gattService.characteristics
            // Loops through available Characteristics.
            for (gattCharacteristic in gattCharacteristics) {
                uuid = gattCharacteristic.uuid.toString()
                updateState("$device >>>>>> $uuid")

                log(
                    "displayGattServices gattCharacteristic --- $uuid >>> ${
                        SampleGattAttributes.lookup(
                            uuid,
                            "Unknown device"
                        )
                    }"
                )
            }
        }
    }

}