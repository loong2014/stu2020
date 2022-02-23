//package com.sunny.module.ble.pax
//
//import android.bluetooth.*
//import android.content.Context
//import com.sunny.module.ble.BleConfig
//import com.sunny.module.ble.client.SampleGattAttributes
//
//
//interface BleConnectCallback {
//    fun showRcvTip(msg: String)
//}
//
//private var slaveConnectThreadCount: Int = 0
//fun buildSlaveConnectThread(
//    context: Context,
//    device: BluetoothDevice,
//    callback: BleConnectCallback
//): SlaveConnectThread {
//    return SlaveConnectThread(context, device, callback, "Slave-${slaveConnectThreadCount++}")
//}
//
///**
// * 从设备 发起与 主设备 的 socket 连接
// */
//class SlaveConnectThread(
//    private val context: Context,
//    private val device: BluetoothDevice,
//    private val callback: BleConnectCallback, name: String
//) : Thread(name) {
//
//    private var canExit = false
//    private var isConnected = false
//    private var mmBluetoothGatt: BluetoothGatt? = null
//    private val mmGattServiceList = ArrayList<BluetoothGattService>()
//
//    private fun log(msg: String) {
//        val log = "$this --- :$msg"
//        BleConfig.bleLog(log)
//    }
//
//    private fun updateState(state: String) {
//        log(state)
//        callback.showRcvTip(state)
//    }
//
//    private var mReadGattCharacteristic: BluetoothGattCharacteristic? = null
//    private var mGattCharacteristic: BluetoothGattCharacteristic? = null
//
//    /**
//     * 写数据
//     */
//    fun doWriteData(msg: String) {
//        mGattCharacteristic?.value = msg.toByteArray()
//        mmBluetoothGatt?.writeCharacteristic(mGattCharacteristic)
//    }
//
//    /**
//     * 读数据
//     */
//    fun doReadData(): String {
//        mmBluetoothGatt?.readCharacteristic(mGattCharacteristic)
//        return mGattCharacteristic?.value.toString()
//    }
//
//    fun doCancel() {
//        updateState("close")
//        try {
//            if (isConnected) {
//                isConnected = false
//                mmBluetoothGatt?.disconnect()
//            }
//
//            canExit = true
//            mmBluetoothGatt?.close()
//            mmBluetoothGatt = null
//        } catch (e: Exception) {
//            log("Could not close the client socket :$e")
//        }
//    }
//
//    override fun run() {
//        updateState("run")
//
//        try {
//            mmBluetoothGatt =
//                device.connectGatt(context, false, gattCallback, BluetoothDevice.TRANSPORT_LE)
//
//            updateState("connectGatt")
//        } catch (e: Exception) {
//            updateState("connectFailed")
//            log("connect failed :$e")
//        }
//    }
//
//    private var gattCallback = object : BluetoothGattCallback() {
//
//        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
//            log("onConnectionStateChange status($status) , newState($newState)")
//            when (newState) {
//                // 连接成功
//                BluetoothProfile.STATE_CONNECTED -> {
//                    updateState("STATE_CONNECTED")
//                    isConnected = true
//
//                    mmBluetoothGatt?.discoverServices()
//                }
//
//                BluetoothProfile.STATE_DISCONNECTED -> {
//                    updateState("STATE_DISCONNECTED")
//                    if (isConnected) {
//                        isConnected = false
//                        mmBluetoothGatt?.disconnect()
//                    }
//                }
//            }
//        }
//
//        // New services discovered
//        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
//            updateState("onServicesDiscovered($status)")
//            when (status) {
//                BluetoothGatt.GATT_SUCCESS -> {
//                    updateState("GATT_SUCCESS")
//                    mmGattServiceList.clear()
//                    mmGattServiceList.addAll(gatt.services)
//
//                    mGattCharacteristic = PaxBleConfig.findGattCharacteristic(
//                        mmGattServiceList,
//                        PaxBleConfig.PAX_UUID_CHARACTERISTIC
//                    )
//                }
//                else -> {
//                    updateState("GATT_OTHER($status)")
//                }
//            }
//        }
//
//        //特征读取回调
//        override fun onCharacteristicRead(
//            gatt: BluetoothGatt,
//            characteristic: BluetoothGattCharacteristic,
//            status: Int
//        ) {
//            updateState("onCharacteristicRead($status)")
//
//            if (BluetoothGatt.GATT_SUCCESS == status) {
//                updateState("CharacteristicRead :${characteristic.uuid} >>> ${String(characteristic.value)}")
//            }
//        }
//
//        //特征写入回调
//        override fun onCharacteristicWrite(
//            gatt: BluetoothGatt,
//            characteristic: BluetoothGattCharacteristic, status: Int
//        ) {
//            updateState("onCharacteristicWrite($status)")
//
//            if (BluetoothGatt.GATT_SUCCESS == status) {
//                updateState("CharacteristicWrite :${characteristic.uuid} >>> ${String(characteristic.value)}")
//            }
//        }
//
//        //特征改变回调（主要由外设回调）
//        override fun onCharacteristicChanged(
//            gatt: BluetoothGatt?,
//            characteristic: BluetoothGattCharacteristic?
//        ) {
//            updateState("onCharacteristicChanged")
//
//            characteristic?.run {
//                updateState("onCharacteristicChanged :${uuid} >>> ${String(value)}")
//            }
//        }
//
//        //描述写入回调
//        override fun onDescriptorWrite(
//            gatt: BluetoothGatt?,
//            descriptor: BluetoothGattDescriptor?,
//            status: Int
//        ) {
//            updateState("onDescriptorWrite($status)")
//
//            if (BluetoothGatt.GATT_SUCCESS == status) {
//                descriptor?.run {
//                    updateState("onDescriptorWrite :${uuid} >>> ${String(value)}")
//                } ?: updateState("onDescriptorWrite :descriptor is null")
//            }
//        }
//
//        //描述读取回调
//        override fun onDescriptorRead(
//            gatt: BluetoothGatt?,
//            descriptor: BluetoothGattDescriptor?,
//            status: Int
//        ) {
//            updateState("onDescriptorRead($status)")
//
//            if (BluetoothGatt.GATT_SUCCESS == status) {
//                descriptor?.run {
//                    updateState("onDescriptorRead :${uuid} >>> ${String(value)}")
//                } ?: updateState("onDescriptorRead :descriptor is null")
//            }
//        }
//
//    }
//
//    /**
//     * 显示远程设备的服务特征
//     */
//    private fun displayGattServices(gattServices: List<BluetoothGattService>?) {
//        if (gattServices == null) return
//        log("displayGattServices size(${gattServices?.size})")
//
//        var uuid: String?
//
//        // Loops through available GATT Services.
//        for (gattService in gattServices) {
//            uuid = gattService.uuid.toString()
//            updateState("$device >>> $uuid")
//            log(
//                "displayGattServices gattService --- $uuid >>> ${
//                    SampleGattAttributes.lookup(
//                        uuid,
//                        "Unknown device"
//                    )
//                }"
//            )
//
//            val gattCharacteristics = gattService.characteristics
//            // Loops through available Characteristics.
//            for (gattCharacteristic in gattCharacteristics) {
//                uuid = gattCharacteristic.uuid.toString()
//                updateState("$device >>>>>> $uuid")
//
//                log(
//                    "displayGattServices gattCharacteristic --- $uuid >>> ${
//                        SampleGattAttributes.lookup(
//                            uuid,
//                            "Unknown device"
//                        )
//                    }"
//                )
//            }
//        }
//    }
//
//}