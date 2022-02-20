package com.sunny.module.ble.client.ble

import android.bluetooth.*
import com.sunny.module.ble.BleConfig
import com.sunny.module.ble.client.BleBaseClientService
import com.sunny.module.ble.client.SampleGattAttributes

/**
 * 低功耗蓝牙
 * https://developer.android.com/guide/topics/connectivity/bluetooth-le?hl=zh-cn
 *
 * 外围
 */
class BleClientBleOld : BleBaseClientService() {

    val mBluetoothManager: BluetoothManager by lazy {
        getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
    }

    var mGattServer: BluetoothGattServer? = null
    override fun doInit() {
        mGattServer = mBluetoothManager.openGattServer(this, gattServerCallback)
        val gattService = BluetoothGattService(
            BleConfig.PAX_BLE_UUID,
            BluetoothGattService.SERVICE_TYPE_PRIMARY
        )

        //描述:
        val gattDescriptor =
            BluetoothGattDescriptor(BleConfig.PAX_BLE_UUID, BleConfig.PAX_BLE_PERMISSIONS)

        //特性 :
        // 属性
        val properties = BluetoothGattCharacteristic.PROPERTY_READ or
                BluetoothGattCharacteristic.PROPERTY_WRITE or
                BluetoothGattCharacteristic.PROPERTY_NOTIFY

        // 权限
        val permissions = BluetoothGattCharacteristic.PERMISSION_READ or
                BluetoothGattCharacteristic.PERMISSION_WRITE

        val gattCharacteristic =
            BluetoothGattCharacteristic(BleConfig.PAX_BLE_CLIENT_UUID, properties, permissions)
        gattCharacteristic.value = "ClientPax".toByteArray()

        gattService.addCharacteristic(gattCharacteristic)

        mGattServer?.addService(gattService)

        doStartScan()
    }

    override fun doRelease() {
        doStopScan()
        doStopConnect()
        mmBluetoothGatt?.close()
        mmBluetoothGatt = null
    }

    override fun doStartScan() {
    }

    override fun doStopScan() {
    }

    // 回应特征值：
    val gattServerCallback = object : BluetoothGattServerCallback() {

        //监听设备连接状态
        override fun onConnectionStateChange(device: BluetoothDevice?, status: Int, newState: Int) {
            log("onConnectionStateChange $device --- $status >>> $newState")
        }

        // 监听中心设备读Characteristic的请求
        override fun onCharacteristicReadRequest(
            device: BluetoothDevice?,
            requestId: Int,
            offset: Int,
            characteristic: BluetoothGattCharacteristic?
        ) {
            log("onCharacteristicReadRequest $device --- $requestId --- ${characteristic?.properties} --- ${characteristic?.permissions}")

            // 最后一个参数可以设置传的数据，byte［］类型的。
            mGattServer?.sendResponse(
                device, requestId,
                BluetoothGatt.GATT_SUCCESS,
                offset, null
            )
        }

        // 监听中心设备写Characteristic的请求
        override fun onCharacteristicWriteRequest(
            device: BluetoothDevice?,
            requestId: Int,
            characteristic: BluetoothGattCharacteristic?,
            preparedWrite: Boolean,
            responseNeeded: Boolean,
            offset: Int,
            value: ByteArray?
        ) {
            log("onCharacteristicWriteRequest $device --- $requestId --- ${characteristic?.properties} --- ${characteristic?.permissions}")

            // 最后一个参数可以设置传的数据，byte［］类型的。
            mGattServer?.sendResponse(
                device, requestId,
                BluetoothGatt.GATT_SUCCESS,
                offset, null
            )
        }

    }

    fun dealFoundOneDevice(device: BluetoothDevice) {
        if (mDeviceAllSet.add(device)) {
            log(
                "FoundNewDevice allCount(${mDeviceAllSet.size}) -> " +
                        "$device(${device.type}) , ${device.name} , ${device.uuids}"
            )
        }
        device.takeIf { it.name != null }?.let {
            if (mDeviceSet.add(it)) {
                logScanResults()
                BleConfig.updateCurDiscoveryDevices(mDeviceSet)
                dealRcvMsg("${device.name}(${device.type})(${device.bondState})\n>>>${device.address} , ${device.uuids}")
                device.uuids?.forEachIndexed { index, uuid ->
                    showLog("${device.name}($index) >>> $uuid")
                    mHandler.post {
                        tryAutoConnect(device)
                    }
                }
            }
        }
    }

    override fun doStartConnect(address: String): Boolean {
        return bluetoothAdapter?.getRemoteDevice(address)?.let { dev ->
            tryAutoConnect(dev)
        } ?: false
    }


    private var mmBluetoothGatt: BluetoothGatt? = null

    private fun tryAutoConnect(device: BluetoothDevice): Boolean {
        log("tryAutoConnect($device) :$mmBluetoothGatt")
        if (mmBluetoothGatt != null) {
            doStopConnect()
        }

        try {
            mmBluetoothGatt = device.connectGatt(this, false, gattCallback)
            log("connectGatt")
        } catch (e: Exception) {
            log("connect failed :$e")
        }

        return true
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

    //    readCharacteristic
    private var gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            log("onConnectionStateChange status($status) , newState($newState)")
            when (newState) {
                // 连接成功
                BluetoothProfile.STATE_CONNECTED -> {
                    log("onConnectionStateChange   STATE_CONNECTED")

                    mmBluetoothGatt?.discoverServices()
                }

                BluetoothProfile.STATE_DISCONNECTED -> {
                    log("Disconnected from GATT server.")
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
                dealRcvMsg(String(it))
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

        var uuid: String?

        // Loops through available GATT Services.
        for (gattService in gattServices) {
            uuid = gattService.uuid.toString()
            log("gattService --- $uuid >>> ${SampleGattAttributes.lookup(uuid, "Unknown device")}")

            val gattCharacteristics = gattService.characteristics
            // Loops through available Characteristics.
            for (gattCharacteristic in gattCharacteristics) {
                uuid = gattCharacteristic.uuid.toString()
                log(
                    "gattCharacteristic --- $uuid >>> ${
                        SampleGattAttributes.lookup(
                            uuid,
                            "Unknown device"
                        )
                    }"
                )
            }
        }
    }

    override fun doStopConnect() {
        try {
            mmBluetoothGatt?.disconnect()
        } catch (e: Exception) {
            log("Could not close the client socket :$e")
        }
    }

    var msgSeq: Int = 0
    override fun doSendMsg(msg: String): Boolean {
        return true
//        return mDeviceConnectThread?.write(msg, (msgSeq++)) ?: false
    }


}