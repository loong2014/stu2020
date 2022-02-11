package com.sunny.module.web.ble

import android.app.Service
import android.bluetooth.*
import android.content.Intent
import android.os.IBinder
import com.sunny.lib.base.log.SunLog
import com.sunny.module.web.ble.client.IBleClientInterface

class BleConnectService : Service() {
    companion object {
        const val STATE_DISCONNECTED = 0
        const val STATE_CONNECTING = 1
        const val STATE_CONNECTED = 2
    }

    private var curBluetoothGatt: BluetoothGatt? = null
    private var curConnectDevice: BluetoothDevice? = null
    private var connectionState = STATE_DISCONNECTED
    private var curResultMsg: String = ""

    private fun isConnected(): Boolean {
        return connectionState == STATE_CONNECTED
    }

    private fun resetConnect() {
        curBluetoothGatt = null
        curConnectDevice = null
        connectionState = STATE_DISCONNECTED
        curResultMsg = ""
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.extras?.getParcelable<BluetoothDevice>("ble_device")?.let {
            doConnect(it)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder {
        return mClientBleBinder
    }

    private val mClientBleBinder = object : IBleClientInterface.Stub() {
        override fun sendMsg(msg: String?): Boolean {
            return trySeenMsg(msg)
        }

        override fun readMsg(): String {
            return buildResultMsg()
        }
    }

    private fun trySeenMsg(msg: String?): Boolean {
        log("trySeenMsg :$msg")
        if (msg != null && isConnected()) {
            return curBluetoothGatt?.let {
                BleTools.doMsgSend(it, msg)
                true
            } ?: false
        }
        return false
    }

    private fun buildResultMsg(): String {
        val sb = StringBuilder()
        sb.append("isConnected(${isConnected()})")
        sb.append("\n==>name(${curConnectDevice?.name})")
        sb.append("\n==>result from service(${System.currentTimeMillis()})\n$curResultMsg")
        return sb.toString()
    }

    private fun doConnect(device: BluetoothDevice) {
        log("doConnect ${device.name}")
        if (isConnected() && curConnectDevice?.name == device.name) {
            log("device(${device.name}) is Already Connected")
            return
        }

        resetConnect()

        curConnectDevice = device
        connectionState = STATE_CONNECTING
        curBluetoothGatt = device.connectGatt(this, false, gattCallback)
    }

    private fun dealConnected(bluetoothGatt: BluetoothGatt) {
        log("dealConnected")
        val gattService = bluetoothGatt.getService(PAX_UUID_SERVICE)
        val characteristic = gattService?.getCharacteristic(PAX_UUID_WRITE)
        if (bluetoothGatt.setCharacteristicNotification(characteristic, true)) {
            characteristic?.descriptors?.forEachIndexed { index, descriptor ->
                if (descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE)) {
                    bluetoothGatt.writeDescriptor(descriptor)
                    log("$index ===> start read msg")
                }
            }
        }
    }

    private var gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            log("onConnectionStateChange status($status) , newState($newState)")
            when (newState) {
                // 连接成功
                BluetoothProfile.STATE_CONNECTED -> {
                    connectionState = STATE_CONNECTED
                    log("onConnectionStateChange   STATE_CONNECTED :" + curBluetoothGatt?.discoverServices())
                    curBluetoothGatt?.let {
                        dealConnected(it)
                    }
                }

                BluetoothProfile.STATE_DISCONNECTED -> {
                    connectionState = STATE_DISCONNECTED
                    log("Disconnected from GATT server.")
                }
            }
        }

        // New services discovered
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            log("onServicesDiscovered  status($status)")
            when (status) {
                BluetoothGatt.GATT_SUCCESS -> log("GATT_SUCCESS")
                else -> log("onServicesDiscovered received: $status")
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
                curResultMsg = String(it)
            }
            when (status) {
                BluetoothGatt.GATT_SUCCESS -> {
                    log("GATT_SUCCESS")
                }
            }
        }
    }

    private fun log(msg: String) {
        SunLog.i("PaxBleService", msg)
    }
}