//package com.sunny.module.ble.pax
//
//import android.bluetooth.*
//import android.bluetooth.le.*
//import android.content.Context
//import android.os.ParcelUuid
//import com.sunny.module.ble.pax.BluetoothUtils.bytesToHexString
//import com.sunny.module.ble.server.BleBaseServerService
//
///**
// * 主设备/中心设备，不停发送广播，等待从设备的连接
// *
// * 车机上的PaxLauncher 不停发广播，等待 FF Ctrl 的连接
// */
//class BleMasterService2 : BleBaseServerService() {
//
//    private var starting = false
//
//    private var mmDevice: BluetoothDevice? = null
//    private var mmBluetoothAdapter: BluetoothAdapter? = null
//    private var mmAdvertiser: BluetoothLeAdvertiser? = null
//
//    private lateinit var mmAdvertiseSettings: AdvertiseSettings
//    private lateinit var mmAdvertiseData: AdvertiseData
//    private lateinit var mmScanResponseData: AdvertiseData
//
//    private lateinit var mGattService: BluetoothGattService
//    private lateinit var mGattCharacteristic: BluetoothGattCharacteristic
//    private lateinit var mGattDescriptor: BluetoothGattDescriptor
//
//    private var mmBluetoothManager: BluetoothManager? = null
//    private var mmBluetoothGattServer: BluetoothGattServer? = null
//
//    private var currentAdvertisingSet: AdvertisingSet? = null
//
//    override fun doInit() {
//        mmBluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
//        mmBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
//        mmAdvertiser = BluetoothAdapter.getDefaultAdapter().bluetoothLeAdvertiser
//
//        addGattServer()
//    }
//
//    override fun doRelease() {
//        doStopScan()
//    }
//
//    // 该示例应用使用 BLE 2M PHY 进行通告
//    override fun doStartScan() {
//        dealRcvMsg("startAdvertising")
//        tryStartAdvertising()
//    }
//
//    override fun doStopScan() {
//        dealRcvMsg("stopAdvertising")
//        if (starting) {
//            mmAdvertiser?.stopAdvertising(advertisingSetCallback)
//        }
//    }
//
//    private fun tryStartAdvertising() {
//        dealRcvMsg("tryStartAdvertising :$starting")
//        if (starting) {
//            return
//        }
//        //初始化广播设置
//        mmAdvertiseSettings = AdvertiseSettings.Builder()
//            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
//            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
//            .setTimeout(0)
//            .setConnectable(true)
//            .build()
//
//        //设置广播报文
//        mmAdvertiseData = AdvertiseData.Builder()
//            .setIncludeDeviceName(true)
//            .setIncludeTxPowerLevel(true)
//            .addServiceUuid(ParcelUuid(PaxBleConfig.PAX_SERVICE_UUID))
//            .build()
//
//        //设置广播扫描响应报文(可选)
//        mmScanResponseData = AdvertiseData.Builder()
//            .setIncludeDeviceName(false)
//            .setIncludeTxPowerLevel(false)
////            .addManufacturerData(0x06, PaxBleConfig.PAX_DATA_FF91.toByteArray())
//            .addServiceData(
//                ParcelUuid(PaxBleConfig.PAX_SERVICE_UUID_FFID),
//                PaxBleConfig.PAX_DATA_FFFID.toByteArray()
//            )
//            .build()
//
//        mmAdvertiser?.startAdvertising(
//            mmAdvertiseSettings, mmAdvertiseData,
//            mmScanResponseData, advertisingSetCallback
//        )
//
//        starting = true
//        dealRcvMsg("startAdvertisingSet succeed")
//    }
//
//    private val advertisingSetCallback = object : AdvertiseCallback() {
//        override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
//            dealRcvMsg("onStartSuccess , isConnectable(${settingsInEffect?.isConnectable})")
//        }
//
//        override fun onStartFailure(errorCode: Int) {
//            if (errorCode == ADVERTISE_FAILED_DATA_TOO_LARGE) {
//                dealRcvMsg("onStartFailure data too large")
//            } else {
//                dealRcvMsg("onStartFailure errorCode($errorCode))")
//            }
//        }
//    }
//
//    /**
//     * 添加Gatt 服务和特征
//     */
//    private fun addGattServer() {
//        dealRcvMsg("addGattServer")
//        //创建服务，并初始化服务的UUID和服务类型。
//        mGattService = BluetoothGattService(
//            PaxBleConfig.PAX_SERVICE_UUID,
//            BluetoothGattService.SERVICE_TYPE_PRIMARY
//        )
//
//        //初始化特征(添加读写权限)
//        mGattCharacteristic = BluetoothGattCharacteristic(
//            PaxBleConfig.PAX_UUID_CHARACTERISTIC,
//            BluetoothGattCharacteristic.PROPERTY_WRITE or
//                    BluetoothGattCharacteristic.PROPERTY_NOTIFY or
//                    BluetoothGattCharacteristic.PROPERTY_READ,
//            (BluetoothGattCharacteristic.PERMISSION_WRITE or BluetoothGattCharacteristic.PERMISSION_READ)
//        ).apply {
//            value = "ff91".toByteArray()
//
//            //特征值添加描述
//            addDescriptor(
//                BluetoothGattDescriptor(
//                    PaxBleConfig.PAX_UUID_DESCRIPTOR,
//                    BluetoothGattDescriptor.PERMISSION_READ or BluetoothGattDescriptor.PERMISSION_WRITE
//                ).apply {
//                    value = "paxLauncher".toByteArray()
//                }
//            )
//        }
//        //Service添加特征值
//        mGattService.addCharacteristic(mGattCharacteristic)
//
//        mmBluetoothGattServer =
//            mmBluetoothManager?.openGattServer(this, bluetoothGattServerCallback)
//        mmBluetoothGattServer?.addService(mGattService)
//    }
//
//    private val bluetoothGattServerCallback = object : BluetoothGattServerCallback() {
//        //设备连接/断开连接回调
//        override fun onConnectionStateChange(device: BluetoothDevice, status: Int, newState: Int) {
//
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                //连接成功
//                when (newState) {
//                    BluetoothProfile.STATE_CONNECTED -> {
//                        mmDevice = device
//                        dealRcvMsg("onConnectionStateChange($device) : connected")
//                    }
//                    BluetoothProfile.STATE_DISCONNECTED -> {
//                        dealRcvMsg("onConnectionStateChange($device) : disconnected")
//                    }
//                    else -> {
//                        dealRcvMsg("onConnectionStateChange($device) : newState($newState)")
//                    }
//                }
//            } else {
//                dealRcvMsg("onConnectionStateChange status($status)")
//            }
//        }
//
//        //添加本地服务回调
//        override fun onServiceAdded(status: Int, service: BluetoothGattService) {
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                dealRcvMsg("onServiceAdded success , UUUID = ${service.uuid}")
//            } else {
//                dealRcvMsg("onServiceAdded failed")
//            }
//        }
//
//        //特征值读取回调
//        override fun onCharacteristicReadRequest(
//            device: BluetoothDevice,
//            requestId: Int,
//            offset: Int,
//            characteristic: BluetoothGattCharacteristic
//        ) {
//            dealRcvMsg(
//                "onCharacteristicReadRequest :${characteristic.uuid} >>> ${
//                    String(characteristic.value)
//                }"
//            )
//
//            // 响应客户端
//            mmBluetoothGattServer?.sendResponse(
//                device, requestId, BluetoothGatt.GATT_SUCCESS,
//                offset, characteristic.value
//            )
//
//        }
//
//        //特征值写入回调
//        override fun onCharacteristicWriteRequest(
//            device: BluetoothDevice, requestId: Int,
//            characteristic: BluetoothGattCharacteristic, preparedWrite: Boolean,
//            responseNeeded: Boolean, offset: Int, value: ByteArray
//        ) {
//            dealRcvMsg(
//                "onCharacteristicWriteRequest :${characteristic.uuid} >>> ${
//                    String(value)
//                }"
//            )
//
//            //刷新该特征值
//            characteristic.value = value
//
//            // 响应客户端
//            mmBluetoothGattServer?.sendResponse(
//                device, requestId, BluetoothGatt.GATT_SUCCESS,
//                offset, value
//            )
//        }
//
//        //描述读取回调
//        override fun onDescriptorReadRequest(
//            device: BluetoothDevice, requestId: Int, offset: Int,
//            descriptor: BluetoothGattDescriptor
//        ) {
//            // 响应客户端
//            mmBluetoothGattServer?.sendResponse(
//                device, requestId, BluetoothGatt.GATT_SUCCESS,
//                offset, descriptor.value
//            )
//            dealRcvMsg(
//                "onDescriptorReadRequest($device) , UUID = ${descriptor.uuid} , value = ${
//                    bytesToHexString(
//                        descriptor.value
//                    )
//                }"
//            )
//        }
//
//        //描述写入回调
//        override fun onDescriptorWriteRequest(
//            device: BluetoothDevice, requestId: Int, descriptor: BluetoothGattDescriptor,
//            preparedWrite: Boolean, responseNeeded: Boolean,
//            offset: Int, value: ByteArray
//        ) {
//            //刷新描述值
//            descriptor.value = value
//            // 响应客户端
//            mmBluetoothGattServer?.sendResponse(
//                device, requestId, BluetoothGatt.GATT_SUCCESS,
//                offset, value
//            )
//            dealRcvMsg(
//                "onDescriptorWriteRequest($device) , UUID = ${descriptor.uuid} , value = ${
//                    bytesToHexString(
//                        descriptor.value
//                    )
//                }"
//            )
//        }
//
//        override fun onNotificationSent(device: BluetoothDevice?, status: Int) {
//            super.onNotificationSent(device, status)
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                dealRcvMsg("onNotificationSent($device) success")
//            } else {
//                dealRcvMsg("onNotificationSent($device) failed($status)")
//            }
//        }
//    }
//
//    override fun doSendMsg(msg: String): Boolean {
//        mGattCharacteristic.value = msg.toByteArray()
//
//        mmBluetoothGattServer?.notifyCharacteristicChanged(mmDevice, mGattCharacteristic, false)
//        mmBluetoothGattServer?.getService(PaxBleConfig.PAX_SERVICE_UUID)
//            ?.getCharacteristic(PaxBleConfig.PAX_UUID_CHARACTERISTIC)
////
////        // 响应客户端
////        mmBluetoothGattServer?.sendResponse(
////            device, requestId, BluetoothGatt.GATT_SUCCESS,
////            offset, characteristic.value
////        )
////        setCharacteristicNotification
////        mGattCharacteristic?.value = msg.toByteArray()
////        mmBluetoothGatt?.writeCharacteristic(mGattCharacteristic)
//        return true
//    }
//
//}