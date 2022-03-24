//package com.sunny.module.ble.pax
//
//import android.bluetooth.*
//import android.bluetooth.le.AdvertiseCallback
//import android.bluetooth.le.AdvertiseData
//import android.bluetooth.le.AdvertiseSettings
//import android.bluetooth.le.BluetoothLeAdvertiser
//import android.content.Context
//import android.os.ParcelUuid
//import com.sunny.module.ble.BleConfig
//import com.sunny.module.ble.PaxBleConfig
//
///**
// * 从设备 不停发送广播，等待从设备的连接
// *
// * 手机上的 FF Ctrl 启动后，不停发广播，等待 PaxLauncher 的连接
// */
//class PaxBleSlaveService2 : PaxBleCommonService() {
//
//    private var starting = false // 是否开始发送
//
//    private var mmBluetoothManager: BluetoothManager? = null
//    private var mmBluetoothGattServer: BluetoothGattServer? = null
//
//    private var mmBluetoothGattService: BluetoothGattService? = null
//    private var mmAdvertiser: BluetoothLeAdvertiser? = null
//
//    private var mmConnectedGatt: BluetoothGatt? = null
//
//
//    // meeting 信息特征
//    private var meetingGattCharacteristic: BluetoothGattCharacteristic? = null
//
//    // 正在连接的设备
//    private var mmDevice: BluetoothDevice? = null
//
//
//    private fun getAuthGattCharacteristic(): BluetoothGattCharacteristic? {
//        return null
//    }
//
//    private fun getCalendarGattCharacteristic(): BluetoothGattCharacteristic? {
//        return null
//    }
//
//    override fun doSendMsg(msg: String) {
//        val meetingInfo = BleConfig.doGetLocalMeetingInfoString(this)
//        log("doSendMsg meetingInfo :$meetingInfo")
//
//        // 发送数据
//        meetingGattCharacteristic?.value = meetingInfo.toByteArray()
//        /*
//        发送本地特征已更新的通知或指示。
//        向远程设备发送通知或指示以表明特性已更新。通过写入给定特征的“客户端配置”描述符，应为每个请求通知指示的客户端调用此函数。
//         */
//        mmBluetoothGattServer?.notifyCharacteristicChanged(
//            mmDevice,
//            meetingGattCharacteristic,
//            /*
//            Confirm – true 请求客户端确认（指示）， false 发送通知
//             */
//            true
//        )
//    }
//
//    override fun doReadMsg(): String {
//        // 读取数据
//        val msg = meetingGattCharacteristic?.value?.run {
//            String(this)
//        } ?: "None"
//        log("doReadMsg msg :$msg")
//        return msg
//    }
//
//    override fun doInit() {
//        super.doInit()
//
//        mmBluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
//
//        mmAdvertiser = BluetoothAdapter.getDefaultAdapter().bluetoothLeAdvertiser
//
//        // 获取广播服务
//        mmBluetoothGattServer =
//            mmBluetoothManager?.openGattServer(this, bluetoothGattServerCallback)
//
//        // 创建 GattServer
//        mmBluetoothGattService = BluetoothGattService(
//            PaxBleConfig.buildUUIDByFFID(PaxBleConfig.ServiceFFIDStr),
//            BluetoothGattService.SERVICE_TYPE_PRIMARY
//        ).apply {
//            // auth
//            authGattCharacteristic = BluetoothGattCharacteristic(
//                PaxBleConfig.getAuthUUID(),
//                (BluetoothGattCharacteristic.PROPERTY_WRITE or
//                        BluetoothGattCharacteristic.PROPERTY_NOTIFY or
//                        BluetoothGattCharacteristic.PROPERTY_READ),
//                (BluetoothGattCharacteristic.PERMISSION_WRITE or
//                        BluetoothGattCharacteristic.PROPERTY_NOTIFY or
//                        BluetoothGattCharacteristic.PERMISSION_READ)
//            ).apply {
//                value = "hello master".toByteArray()
//            }
//            addCharacteristic(authGattCharacteristic)
//
//            // calendar
//            meetingGattCharacteristic = BluetoothGattCharacteristic(
//                PaxBleConfig.getCalendarUUID(),
//                (BluetoothGattCharacteristic.PROPERTY_WRITE or
//                        BluetoothGattCharacteristic.PROPERTY_NOTIFY or
//                        BluetoothGattCharacteristic.PROPERTY_READ),
//                (BluetoothGattCharacteristic.PERMISSION_WRITE or
//                        BluetoothGattCharacteristic.PROPERTY_NOTIFY or
//                        BluetoothGattCharacteristic.PERMISSION_READ)
//            )
//            addCharacteristic(meetingGattCharacteristic)
//        }
//
//        mmBluetoothGattServer?.addService(mmBluetoothGattService)
//
//        tryStartAdvertising()
//    }
//
//    override fun doRelease() {
//        super.doRelease()
//        mmBluetoothGattServer?.clearServices()
//        mmBluetoothGattServer?.close()
//        mmBluetoothGattServer = null
//
//        mmAdvertiser?.stopAdvertising(advertisingSetCallback)
//        mmAdvertiser = null
//    }
//
//    /**
//     * 广播设置
//     */
//    private fun buildAdvertiseSettings(): AdvertiseSettings {
//        return AdvertiseSettings.Builder()
//            /*
//            设置广告模式以控制广告功率和延迟。
//            ADVERTISE_MODE_LOW_POWER（默认）
//                在低功耗模式下执行蓝牙 LE 广告。这是默认和首选的广告模式，因为它消耗的电量最少。
//            ADVERTISE_MODE_BALANCED
//                在平衡功率模式下执行蓝牙 LE 广播。这是广告频率和功耗之间的平衡。
//            ADVERTISE_MODE_LOW_LATENCY
//                在低延迟、高功率模式下执行蓝牙 LE 广告。这具有最高的功耗，不应该用于连续的背景广告
//             */
//            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_POWER)
//            /*
//            设置广告 TX 功率级别以控制广告的传输功率级别。
//            ADVERTISE_TX_POWER_ULTRA_LOW
//                使用最低传输 (TX) 功率电平进行广告宣传。低传输功率可用于限制广告包的可见范围。
//            ADVERTISE_TX_POWER_LOW
//                使用低 TX 功率电平做广告。
//            ADVERTISE_TX_POWER_MEDIUM(默认)
//                使用中等 TX 功率水平进行广告宣传。
//            ADVERTISE_TX_POWER_HIGH
//                使用高 TX 功率电平做广告。这对应于广告包的最大可见范围。
//             */
//            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
//            /*
//            将广告限制在给定的时间范围内。
//            [0 , 180 * 1000]
//            0 将禁用时间限制(默认)
//             */
//            .setTimeout(0)
//            /*
//            设置广告类型是可连接的还是不可连接的。
//            false
//            true(more)
//             */
//            .setConnectable(true)
//            .build()
//    }
//
//    /**
//     * 开始广播
//     */
//    private fun tryStartAdvertising() {
//        showLog("tryStartAdvertising :$starting")
//        if (starting) {
//            return
//        }
//
//        // 广播设置
//        val mmAdvertiseSettings = buildAdvertiseSettings()
//
//        //设置广播报文
//        val mmAdvertiseData = AdvertiseData.Builder()
//            /*
//            是否包含设备名称
//            false（默认）
//             */
//            .setIncludeDeviceName(true)
//            /*
//            传输功率级别是否应包含在广告数据包中。 TX功率级别字段在通告数据包中需要3个字节。
//            false（默认）
//             */
//            .setIncludeTxPowerLevel(false)
//            /*
//            添加服务 UUID 以通告数据。
//             */
//            .addServiceUuid(ParcelUuid(PaxBleConfig.buildUUIDByFFID(PaxBleConfig.ServiceFFIDStr)))
//            /*
//            添加服务数据以宣传数据。
//            */
////            .addServiceData(
////                ParcelUuid(PaxBleConfig.getTestUUID()),
////                "ff91".toByteArray()
////            )
//            .build()
//
//        val mmScanResponseData = null
////        = PaxBleConfig.buildFF91AdvertiseData()
//
//        //开始广播
//        mmAdvertiser?.startAdvertising(
//            mmAdvertiseSettings, mmAdvertiseData,
//            null, advertisingSetCallback
//        )
//
//        starting = true
//        showUiInfo("开始广播")
//    }
//
//    private val advertisingSetCallback = object : AdvertiseCallback() {
//        override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
//            showUiInfo("onStartSuccess , isConnectable(${settingsInEffect?.isConnectable})")
//        }
//
//        override fun onStartFailure(errorCode: Int) {
//            if (errorCode == ADVERTISE_FAILED_DATA_TOO_LARGE) {
//                showUiInfo("onStartFailure data too large")
//            } else {
//                showUiInfo("onStartFailure errorCode($errorCode))")
//            }
//        }
//    }
//
//    private fun closeGattServer() {
//
//    }
//
//    private fun doConnect(device: BluetoothDevice) {
//        mmDevice = device
//        closeGattServer()
//
//        mmDevice?.connectGatt(this, false, mmGattCallback)
//    }
//
//    private val mmGattCallback = object : BluetoothGattCallback() {
//        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
//            //连接成功
//            when (newState) {
//                BluetoothProfile.STATE_CONNECTED -> {
//                    showUiInfo("STATE_CONNECTED")
//                    mmConnectedGatt = gatt
//                    gatt?.discoverServices()
//                }
//                BluetoothProfile.STATE_DISCONNECTED -> {
//                    showUiInfo("STATE_DISCONNECTED")
//                }
//            }
//        }
//    }
//
//    private val bluetoothGattServerCallback = object : BluetoothGattServerCallback() {
//        //设备连接/断开连接回调
//        override fun onConnectionStateChange(device: BluetoothDevice, status: Int, newState: Int) {
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                //连接成功
//                when (newState) {
//                    BluetoothProfile.STATE_CONNECTED -> {
//                        showUiInfo("onConnectionStateChange($device) : connected")
//                        doConnect(device)
//                    }
//                    BluetoothProfile.STATE_DISCONNECTED -> {
//                        showUiInfo("onConnectionStateChange($device) : disconnected")
//                    }
//                    else -> {
//                        showUiInfo("onConnectionStateChange($device) : newState($newState)")
//                    }
//                }
//            } else {
//                showUiInfo("onConnectionStateChange($device) : status($status)")
//            }
//        }
//
//        //添加本地服务回调
//        override fun onServiceAdded(status: Int, service: BluetoothGattService) {
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                showUiInfo("onServiceAdded success , UUID(${service.uuid})")
//            } else {
//                showUiInfo("onServiceAdded failed")
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
//            showUiInfo("数据被读取 ${PaxBleConfig.getUUIDName(characteristic.uuid)}")
//
//            val msg = String(characteristic.value)
//            showLog("onCharacteristicReadRequest :${characteristic.uuid} >>> $msg")
//
//            // 响应客户端
//            mmBluetoothGattServer?.sendResponse(
//                device, requestId, BluetoothGatt.GATT_SUCCESS,
//                offset, characteristic.value
//            )
//        }
//
//        //特征值写入回调
//        override fun onCharacteristicWriteRequest(
//            device: BluetoothDevice, requestId: Int,
//            characteristic: BluetoothGattCharacteristic, preparedWrite: Boolean,
//            responseNeeded: Boolean, offset: Int, value: ByteArray
//        ) {
//            showUiInfo("数据被写入 ${PaxBleConfig.getUUIDName(characteristic.uuid)}")
//
//            val msg = String(characteristic.value)
//            showLog("onCharacteristicWriteRequest :${characteristic.uuid} >>> $msg")
//
//            //刷新该特征值
//            characteristic.value = value
//            // 响应客户端
//            mmBluetoothGattServer?.sendResponse(
//                device, requestId, BluetoothGatt.GATT_SUCCESS,
//                offset, value
//            )
//        }
//
////        //描述读取回调
////        override fun onDescriptorReadRequest(
////            device: BluetoothDevice, requestId: Int, offset: Int,
////            descriptor: BluetoothGattDescriptor
////        ) {
////            val msg = String(descriptor.value)
////            showUiInfo("onCharacteristicReadRequest :${descriptor.uuid} >>> $msg")
////
////            showSendMsg("Dread >>> $msg")
////
////            // 响应客户端
////            mmBluetoothGattServer?.sendResponse(
////                device, requestId, BluetoothGatt.GATT_SUCCESS,
////                offset, descriptor.value
////            )
////        }
////
////        //描述写入回调
////        override fun onDescriptorWriteRequest(
////            device: BluetoothDevice, requestId: Int, descriptor: BluetoothGattDescriptor,
////            preparedWrite: Boolean, responseNeeded: Boolean,
////            offset: Int, value: ByteArray
////        ) {
////            val msg = String(descriptor.value)
////            showUiInfo("onDescriptorWriteRequest :${descriptor.uuid} >>> $msg")
////
////            showRcvMsg("Dwrite >>> $msg")
////
////            //刷新描述值
////            descriptor.value = value
////            // 响应客户端
////            mmBluetoothGattServer?.sendResponse(
////                device, requestId, BluetoothGatt.GATT_SUCCESS,
////                offset, value
////            )
////        }
//
//        override fun onNotificationSent(device: BluetoothDevice?, status: Int) {
//            super.onNotificationSent(device, status)
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                showUiInfo("onNotificationSent($device) success")
//            } else {
//                showUiInfo("onNotificationSent($device) failed($status)")
//            }
//        }
//    }
//}