package com.sunny.module.ble.slave

import android.bluetooth.*
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.content.Context
import android.os.ParcelUuid
import com.sunny.module.ble.PaxBleConfig
import com.sunny.module.ble.pax.PaxBleCommonService


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

    private fun getCalendarGattCharacteristic(): BluetoothGattCharacteristic? {
        return mmGattServer?.getService(PaxBleConfig.getServiceUUID())
            ?.getCharacteristic(PaxBleConfig.getCalendarUUID())
    }

    override fun doInit() {
        super.doInit()

        // 获取GATT服务
        val bm = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mmGattServer = bm.openGattServer(this, gattServerCallback)

        mmGattService = BluetoothGattService(
            PaxBleConfig.buildUUIDByFFID(PaxBleConfig.ServiceFFIDStr),
            BluetoothGattService.SERVICE_TYPE_PRIMARY
        ).apply {
            // auth
            val authGattCharacteristic = BluetoothGattCharacteristic(
                PaxBleConfig.getAuthUUID(),
                (BluetoothGattCharacteristic.PROPERTY_WRITE or
                        BluetoothGattCharacteristic.PROPERTY_NOTIFY or
                        BluetoothGattCharacteristic.PROPERTY_READ),
                (BluetoothGattCharacteristic.PERMISSION_WRITE or
                        BluetoothGattCharacteristic.PROPERTY_NOTIFY or
                        BluetoothGattCharacteristic.PERMISSION_READ)
            )
            addCharacteristic(authGattCharacteristic)

            // calendar
            /*
            properties – 此特征的属性
            permissions - 此特征的权限
             */
            val meetingGattCharacteristic = BluetoothGattCharacteristic(
                PaxBleConfig.getCalendarUUID(),
                (BluetoothGattCharacteristic.PROPERTY_WRITE or
//                        BluetoothGattCharacteristic.PROPERTY_NOTIFY or
                        BluetoothGattCharacteristic.PROPERTY_READ),
                (BluetoothGattCharacteristic.PERMISSION_WRITE or
//                        BluetoothGattCharacteristic.PROPERTY_NOTIFY or
                        BluetoothGattCharacteristic.PERMISSION_READ)
            )
            addCharacteristic(meetingGattCharacteristic)
        }
        // 添加服务
        mmGattServer?.addService(mmGattService)

        //开始广播
        mmAdvertiser = BluetoothAdapter.getDefaultAdapter().bluetoothLeAdvertiser
        mmAdvertiser?.stopAdvertising(advertisingCallback)
        mmAdvertiser?.startAdvertising(
            AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_POWER)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
                .setTimeout(0)
                .setConnectable(true)
                .build(),
            AdvertiseData.Builder()
                .setIncludeDeviceName(false)
                .setIncludeTxPowerLevel(false)
                .addServiceUuid(ParcelUuid(PaxBleConfig.getServiceUUID()))
                .build(),
            advertisingCallback
        )
        showUiInfo("开始广播")
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
            showUiInfo("ReadRequest requestId=$requestId , offset=$offset")
            if (PaxBleConfig.getAuthUUID() == characteristic.uuid) {
                val info = PaxBleConfig.buildPhonePrivateKey(null)
                showUiInfo("返回手机端认证信息 :$info")
                mmGattServer?.sendResponse(
                    device, requestId, BluetoothGatt.GATT_SUCCESS,
                    offset, info.toByteArray()
                )
            } else if (PaxBleConfig.getCalendarUUID() == characteristic.uuid) {
                val info: String = PaxBleConfig.buildMeetInfo(mmContext)
                val fullValue = info.toByteArray()
                val fullSize = fullValue.size
                showUiInfo("返回日历信息 fullSize=$fullSize , offset=$offset")
                if (offset > fullSize) {
                    mmGattServer?.sendResponse(
                        device, requestId, BluetoothGatt.GATT_SUCCESS,
                        0, info.toByteArray()
                    )
                    return
                }

                //
                val size = fullSize - offset
                val response = ByteArray(size)
                for (i in offset until fullSize) {
                    response[i - offset] = fullValue[i]
                }
                mmGattServer?.sendResponse(
                    device, requestId, BluetoothGatt.GATT_SUCCESS,
                    offset, response
                )
            } else {

                mmGattServer?.sendResponse(
                    device, requestId, BluetoothGatt.GATT_SUCCESS,
                    0, null
                )
            }
        }

        // 接收数据
        override fun onCharacteristicWriteRequest(
            device: BluetoothDevice, requestId: Int,
            characteristic: BluetoothGattCharacteristic, preparedWrite: Boolean,
            responseNeeded: Boolean, offset: Int, value: ByteArray
        ) {
            showUiInfo("WriteRequest requestId=$requestId , offset=$offset")
            when (characteristic.uuid) {
                PaxBleConfig.getAuthUUID() -> {
                    val info = String(value)
                    showUiInfo("收到车机端认证信息 :$info")
                    if (PaxBleConfig.buildVehiclePublicKey(null) == info) {
                        showUiInfo("车机认证成功")
//                        mWorkHandler.postDelayed({
//                            doWriteZoomData()
//                        }, 500)
                    } else {
                        showUiInfo("车机认证失败")
                    }

                    // 通知车机数据收到
                    mmGattServer?.sendResponse(
                        device, requestId, BluetoothGatt.GATT_SUCCESS,
                        offset, null
                    )
                }
            }
        }

        override fun onNotificationSent(device: BluetoothDevice?, status: Int) {
            super.onNotificationSent(device, status)
            if (status == BluetoothGatt.GATT_SUCCESS) {
                showUiInfo("发送成功")
//                trySendZoomData()
            } else {
                showUiInfo("发送失败")
            }
        }
    }

    private fun doWriteZoomData() {
        val info: String = PaxBleConfig.buildMeetInfo(mmContext)

        val gattService = mmGattServer?.getService(PaxBleConfig.getServiceUUID())
        gattService?.getCharacteristic(PaxBleConfig.getCalendarUUID())?.value = info.toByteArray()
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

    private fun doStartSendZoomData() {
        val info: String = PaxBleConfig.buildMeetInfo(mmContext)
        val len = info.length

        var count = len / 20
        if (len % 20 != 0) {
            count++
        }
        showUiInfo("开始发送数据 size=$len , count=$count")

        calendarData = info
        calendarLen = len
        totalCount = count
        index = -2
        isSending = true

        trySendZoomData()
    }

    override fun doSendMsg(msg: String) {
//        doStartSendZoomData()
    }

    private fun trySendZoomData() {
        if (!isSending) return

        mmGattServer?.getService(PaxBleConfig.getServiceUUID())
            ?.getCharacteristic(PaxBleConfig.getCalendarUUID())
            ?.let { characteristic ->
                val info = getNextData()
                if (info == null) {
                    isSending = false
                    showUiInfo("数据发送完毕")
                } else {
                    showUiInfo("发送数据($index) :$info")
                    characteristic.value = info.toByteArray()
                    mmGattServer?.notifyCharacteristicChanged(mmDevice, characteristic, true)
                }
            }
    }
}