package com.sunny.module.ble.slave

import android.bluetooth.*
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.content.Context
import android.os.ParcelUuid
import com.sunny.module.ble.PaxBleCommonService
import com.sunny.module.ble.PaxBleConfig
import com.sunny.module.ble.utils.PaxByteUtils


/**
 * 从设备 不停发送广播，等待从设备的连接
 *
 * 手机上的 FF Ctrl 启动后，不停发广播，等待 PaxLauncher 的连接
 */
class PaxBleSlaveService : PaxBleCommonService() {

    private var mmGattServer: BluetoothGattServer? = null
    private var mmGattService: BluetoothGattService? = null

    private var mmAdvertiser: BluetoothLeAdvertiser? = null

    // 正在连接的设备
    private var mmDevice: BluetoothDevice? = null

    private var curFFID = PaxBleConfig.BleDebugFFID

    private val mDataSendHelper = PaxBleCalendarDataHelper()

    private var authCharacteristic: BluetoothGattCharacteristic? = null
    private var notifyCharacteristic: BluetoothGattCharacteristic? = null
    private var readableCharacteristic: BluetoothGattCharacteristic? = null
    private var readCharacteristic: BluetoothGattCharacteristic? = null

    override fun doInit() {
        super.doInit()

        // 获取GATT服务
        val bm = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mmGattServer = bm.openGattServer(this, gattServerCallback)

        val serviceUUID = PaxBleConfig.buildUUIDByFFID(curFFID)

        // 创建服务
        mmGattService = BluetoothGattService(
            serviceUUID,
            BluetoothGattService.SERVICE_TYPE_PRIMARY
        )
        // 给服务添加特征
        doAddCharacteristic()

        // 添加服务到GATT服务
        mmGattServer?.addService(mmGattService)

        //开始广播
        mmAdvertiser = BluetoothAdapter.getDefaultAdapter().bluetoothLeAdvertiser
        mmAdvertiser?.stopAdvertising(advertisingCallback)

//        private static final int MAX_ADVERTISING_DATA_BYTES = 1650;
//        private static final int MAX_LEGACY_ADVERTISING_DATA_BYTES = 31;
        val maxDataSize = BluetoothAdapter.getDefaultAdapter().leMaximumAdvertisingDataLength
        showLog("最大数据长度(字节) :$maxDataSize")

        // 广播设置
        val settings = AdvertiseSettings.Builder()
            // 广播间隔
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_POWER)
            /*
            设置广告的传输功率级别。参数： txPowerLevel – 蓝牙 LE 广告的传输功率，以 dBm 为单位。有效范围为 [-127, 1]
            推荐值为：TX_POWER_ULTRA_LOW、TX_POWER_LOW、TX_POWER_MEDIUM 或 TX_POWER_HIGH。
             */
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
            .setTimeout(0)
            .setConnectable(true)
            .build()

        // 广播报文
        val advertiseData = AdvertiseData.Builder()
            /*
            是否包含设备名称
            false（默认）
             */
            .setIncludeDeviceName(true)
            /*
            传输功率级别是否应包含在广告数据包中。 TX功率级别字段在通告数据包中需要3个字节。
            false（默认）
             */
            .setIncludeTxPowerLevel(false)
            /*
            添加服务 UUID 以通告数据。
             */
            .addServiceUuid(ParcelUuid(serviceUUID))
            /*
            添加服务数据以宣传数据。
            */
//            .addServiceData(
//                ParcelUuid(PaxBleConfig.getTestUUID()),
//                "ff91".toByteArray()
//            )
            .build()

        // 与广告数据关联的扫描响应
        val scanResponse = AdvertiseData.Builder()
            .setIncludeDeviceName(false)
            .setIncludeTxPowerLevel(false)
            // 添加制造商数据
            .addManufacturerData(0x06, "ff".toByteArray())
            .addServiceData(
                ParcelUuid(PaxBleConfig.PAX_UUID_RESPONSE),
                "sunny".toByteArray()
            )
            .build()

        /*
        启动蓝牙 LE 广告。如果操作成功，advertData 将被广播。
        scanResponse 在扫描设备发送主动扫描请求时返回。该方法立即返回，操作状态通过回调传递。
         */
        mmAdvertiser?.startAdvertising(
            // 设置 - 蓝牙 LE 广告的设置
            settings,
            // 广告数据——要在广告数据包中广告的广告数据
            advertiseData,
            // scanResponse – 与广告数据关联的扫描响应(可选)
            scanResponse,
            // callback – 广告状态的回调
            advertisingCallback
        )
        showUiInfo("开始广播")
    }

    /**
     * uuid - 此特征的UUID
     * properties – 此特征的属性
     * permissions - 此特征的权限
     */
    private fun doAddCharacteristic() {

        // auth
        authCharacteristic = BluetoothGattCharacteristic(
            PaxBleConfig.PAX_UUID_CALENDAR_AUTH,

            (BluetoothGattCharacteristic.PROPERTY_WRITE or
                    BluetoothGattCharacteristic.PROPERTY_READ),

            (BluetoothGattCharacteristic.PERMISSION_WRITE or
                    BluetoothGattCharacteristic.PERMISSION_READ)
        )
        mmGattService?.addCharacteristic(authCharacteristic)


        // calendar notify
        notifyCharacteristic = BluetoothGattCharacteristic(
            PaxBleConfig.PAX_UUID_CALENDAR_NOTIFY,
            BluetoothGattCharacteristic.PROPERTY_NOTIFY,
            BluetoothGattCharacteristic.PROPERTY_NOTIFY
        ).apply {
            addDescriptor(
                /*
                PERMISSION_WRITE
                    写入权限
                PERMISSION_WRITE_ENCRYPTED
                    允许加密写入
                PERMISSION_WRITE_ENCRYPTED_MITM
                    允许具有中间人保护的加密写入
                PERMISSION_WRITE_SIGNED
                    允许签名写入操作
                PERMISSION_WRITE_SIGNED_MITM
                    允许带有中间人保护的签名写入操作
                 */
                BluetoothGattDescriptor(
                    PaxBleConfig.PAX_UUID_SUNNY,
                    BluetoothGattDescriptor.PERMISSION_WRITE or
                            BluetoothGattDescriptor.PERMISSION_READ
                )
                    .apply {
                        value = "sunny".toByteArray()
                    }
            )
        }
        mmGattService?.addCharacteristic(notifyCharacteristic)

        // calendar readable
        readableCharacteristic = BluetoothGattCharacteristic(
            PaxBleConfig.PAX_UUID_CALENDAR_READABLE,

            BluetoothGattCharacteristic.PROPERTY_NOTIFY,

            BluetoothGattCharacteristic.PROPERTY_NOTIFY
        )
        mmGattService?.addCharacteristic(readableCharacteristic)

        // calendar read
        readCharacteristic = BluetoothGattCharacteristic(
            PaxBleConfig.PAX_UUID_CALENDAR_READ,

            (BluetoothGattCharacteristic.PROPERTY_WRITE or
                    BluetoothGattCharacteristic.PROPERTY_READ),

            (BluetoothGattCharacteristic.PERMISSION_WRITE or
                    BluetoothGattCharacteristic.PERMISSION_READ)
        )
        mmGattService?.addCharacteristic(readCharacteristic)
    }

    override fun doRelease() {
        super.doRelease()
        showUiInfo("断开连接")

        // 清除服务
        mmGattServer?.removeService(mmGattService)
        mmGattServer?.close()
        mmGattServer = null

        // 停止广播
        mmAdvertiser?.stopAdvertising(advertisingCallback)
        mmAdvertiser = null
    }

    // 开始广播回调
    private val advertisingCallback = object : AdvertiseCallback() {
        override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
            showUiInfo("开始广播——成功")
        }

        override fun onStartFailure(errorCode: Int) {
            when (errorCode) {
                ADVERTISE_FAILED_DATA_TOO_LARGE -> showUiInfo("开始广播——失败——数据太大")
                ADVERTISE_FAILED_TOO_MANY_ADVERTISERS -> showUiInfo("开始广播——失败——没有可用实例")
                ADVERTISE_FAILED_ALREADY_STARTED -> showUiInfo("开始广播——失败——广播已开始")
                ADVERTISE_FAILED_INTERNAL_ERROR -> showUiInfo("开始广播——失败——内部错误")
                ADVERTISE_FAILED_FEATURE_UNSUPPORTED -> showUiInfo("开始广播——失败——平台不支持")
                else -> showUiInfo("开始广播——失败——$errorCode")
            }
        }
    }

    // 服务回调
    private val gattServerCallback = object : BluetoothGattServerCallback() {

        override fun onServiceAdded(status: Int, service: BluetoothGattService) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                showUiInfo("服务添加成功")
            } else {
                showUiInfo("服务添加失败 :$status")
            }
        }

        override fun onConnectionStateChange(device: BluetoothDevice, status: Int, newState: Int) {
            showUiInfo("服务连接状态变化  status:$status , newState:$newState")
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //连接成功
                when (newState) {
                    BluetoothProfile.STATE_CONNECTED -> {
                        mmDevice = device
                        showUiInfo("设备连接成功")
                    }
                    BluetoothProfile.STATE_DISCONNECTED -> {
                        showUiInfo("设备连接断开")
                    }
                }
            }
        }

        // 返回数据
        override fun onCharacteristicReadRequest(
            device: BluetoothDevice,
            requestId: Int,
            offset: Int,
            characteristic: BluetoothGattCharacteristic
        ) {
            when (characteristic.uuid) {
                PaxBleConfig.PAX_UUID_CALENDAR_AUTH -> {
                    // 返回手机auth信息
                    val phoneAuthData = PaxBleConfig.buildPhoneAuthKeyArray(curFFID)
                    showLog("send auth data :${PaxByteUtils.bytesToHex(phoneAuthData)}")

                    mmGattServer?.sendResponse(
                        device, requestId, BluetoothGatt.GATT_SUCCESS,
                        offset, phoneAuthData
                    )
                }
                PaxBleConfig.PAX_UUID_CALENDAR_READ -> {
                    // 返回日历信息
                    val sendData = mDataSendHelper.getNextSendData(offset)
                    showLog("read data , offset :$offset , ${sendData?.size}")
                    mmGattServer?.sendResponse(
                        device, requestId, BluetoothGatt.GATT_SUCCESS,
                        offset, sendData
                    )
//
//                val info: String = PaxBleConfig.buildMeetInfo(mmContext)
//                val fullValue = info.toByteArray()
//                val fullSize = fullValue.size
//                showUiInfo("返回日历信息 fullSize=$fullSize , offset=$offset")
//                if (offset > fullSize) {
//                    mmGattServer?.sendResponse(
//                        device, requestId, BluetoothGatt.GATT_SUCCESS,
//                        0, info.toByteArray()
//                    )
//                    return
//                }
//
//                //
//                val size = fullSize - offset
//                val response = ByteArray(size)
//                for (i in offset until fullSize) {
//                    response[i - offset] = fullValue[i]
//                }
//                mmGattServer?.sendResponse(
//                    device, requestId, BluetoothGatt.GATT_SUCCESS,
//                    offset, response
//                )
                }
                else -> {
                    mmGattServer?.sendResponse(
                        device, requestId, BluetoothGatt.GATT_SUCCESS,
                        0, null
                    )
                }
            }
        }

        // 接收数据
        override fun onCharacteristicWriteRequest(
            device: BluetoothDevice, requestId: Int,
            characteristic: BluetoothGattCharacteristic, preparedWrite: Boolean,
            responseNeeded: Boolean, offset: Int, value: ByteArray
        ) {
            when (characteristic.uuid) {
                PaxBleConfig.PAX_UUID_CALENDAR_AUTH -> {
                    showTip("readAuthInfo :${PaxByteUtils.bytesToHex(value)}")

                    if (PaxBleConfig.buildVehicleAuthKeyArray(curFFID).contentEquals(value)) {
                        showUiInfo("车机认证成功")

                        mmGattServer?.sendResponse(
                            device, requestId, BluetoothGatt.GATT_SUCCESS,
                            0, null
                        )

                        dealAuthPass()
                    } else {
                        showUiInfo("车机认证失败")
                        dealAuthFailed()
                    }
                }
                PaxBleConfig.PAX_UUID_CALENDAR_READ -> {
                    val dataInfo = mDataSendHelper.buildResponseData(value)
                    mmGattServer?.sendResponse(
                        device, requestId, BluetoothGatt.GATT_SUCCESS,
                        0, dataInfo
                    )
                }
            }
        }

        override fun onNotificationSent(device: BluetoothDevice?, status: Int) {
            super.onNotificationSent(device, status)
            if (status == BluetoothGatt.GATT_SUCCESS) {
                showUiInfo("发送成功")
            } else {
                showUiInfo("发送失败")
            }
        }
    }

    var calendarData: String? = null
    var calendarLen = 0
    var totalCount = 0
    var index = -2
    var isSending = false

    private fun getNextData(): String? {
        if (calendarData == null) return null
        index++
        if (index == -2) {

            return ""
        }
        if (index < totalCount) {
            val start = index * 20

            val end = if (start + 20 < calendarLen) {
                start + 20
            } else {
                calendarLen
            }

            return calendarData?.substring(start, end)
        }
        return null
    }

    private fun dealAuthFailed() {
        readableCharacteristic?.let { characteristic ->
            characteristic.value = mDataSendHelper.getAuthFailedByteArray()
            mmGattServer?.notifyCharacteristicChanged(mmDevice, characteristic, true)
        }
    }

    private fun dealCloseService() {
        readableCharacteristic?.let { characteristic ->
            characteristic.value = mDataSendHelper.getCloseByteArray()
            mmGattServer?.notifyCharacteristicChanged(mmDevice, characteristic, true)
        }
    }

    private fun dealAuthPass() {
        mWorkHandler.post {
            mDataSendHelper.doInitData()
//            doSendNotifyData()
            readCharacteristic?.let { characteristic ->
                characteristic.value = mDataSendHelper.getAllData()
                mmGattServer?.notifyCharacteristicChanged(mmDevice, characteristic, true)
            }
        }
    }
//
//    private fun doSendNotifyData() {
//        val info: String = PaxBleConfig.buildMeetInfo(mmContext)
//        val len = info.length
//
//        var count = len / 20
//        if (len % 20 != 0) {
//            count++
//        }
//        showUiInfo("开始发送数据 size=$len , count=$count")
//
//        calendarData = info
//        calendarLen = len
//        totalCount = count
//        index = -2
//        isSending = true
//
//        trySendZoomData()
//        readableCharacteristic?.let { characteristic ->
//            val info = getNextData()
//            if (info == null) {
//                isSending = false
//                showUiInfo("数据发送完毕")
//            } else {
//                showUiInfo("发送数据($index) :$info")
//                characteristic.value = info.toByteArray()
//                mmGattServer?.notifyCharacteristicChanged(mmDevice, characteristic, true)
//            }
//        }
//    }


    override fun doSendMsg(msg: String) {
//        doStartSendZoomData()
    }
//
//    private fun trySendZoomData() {
//        if (!isSending) return
//
//        mmGattServer?.getService(PaxBleConfig.getServiceUUID())
//            ?.getCharacteristic(PaxBleConfig.getCalendarReadUUID())
//            ?.let { characteristic ->
//                val info = getNextData()
//                if (info == null) {
//                    isSending = false
//                    showUiInfo("数据发送完毕")
//                } else {
//                    showUiInfo("发送数据($index) :$info")
//                    characteristic.value = info.toByteArray()
//                    mmGattServer?.notifyCharacteristicChanged(mmDevice, characteristic, true)
//                }
//            }
//    }
}