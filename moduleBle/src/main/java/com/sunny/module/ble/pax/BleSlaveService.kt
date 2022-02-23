package com.sunny.module.ble.pax

import android.bluetooth.*
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.ParcelUuid
import com.sunny.lib.base.log.SunLog

/**
 * 从设备 不停发送广播，等待从设备的连接
 *
 * 手机上的 FF Ctrl 启动后，不停发广播，等待 PaxLauncher 的连接
 */
class PaxSlaveService : PaxBleBaseService() {

    lateinit var mWorkHandler: Handler
//    lateinit var mMainHandler: Handler

    //    private var isSupportBle = false // 是否支持ble
    private var starting = false // 是否开始发送

    private var mmBluetoothManager: BluetoothManager? = null
    private var mmBluetoothGattServer: BluetoothGattServer? = null
    private var mmAdvertiser: BluetoothLeAdvertiser? = null

    // meeting 信息特征
    private var meetingGattCharacteristic: BluetoothGattCharacteristic? = null

    // 正在连接的设备
    private var mmDevice: BluetoothDevice? = null
    private var mmDeviceRequestId: Int = 0

    private fun showLog(msg: String) {
        SunLog.i("BleLog-PaxBleService", msg)
    }

    /**
     * 显示收到的消息
     */
    private fun showRcvMsg(msg: String) {
        showUiInfo("BleLog-Rcv:$msg")
    }

    /**
     * 显示发送的消息
     */
    private fun showSendMsg(msg: String) {
        showUiInfo("BleLog-Send:$msg")
    }

    private fun showConnectLog(msg: String) {
        showUiInfo("$mmDevice --- $msg")
    }

    override fun doSendMsg(msg: String): Boolean {
        sendMeetingData(msg)
        return true
    }

    override fun doReadMsg(): String {
        return readMeetingData()
    }

    /**
     * 发送meeting信息
     */
    private fun sendMeetingData(msg: String) {
//        meetingGattCharacteristic?.run {
//            value = msg.toByteArray()
//            mmBluetoothGattServer?.sendResponse(
//                mmDevice, instanceId, BluetoothGatt.GATT_SUCCESS,
//                0, value
//            )
//        }
        meetingGattCharacteristic?.value = msg.toByteArray()
        mmBluetoothGattServer?.notifyCharacteristicChanged(
            mmDevice,
            meetingGattCharacteristic,
            false
        )
//        meetingGattCharacteristic?.instanceId
//        // 响应客户端
//        mmBluetoothGattServer?.sendResponse(
//            mmDevice, requestId, BluetoothGatt.GATT_SUCCESS,
//            offset, characteristic.value
//        )
    }

    /**
     * 读取meeting消息
     */
    private fun readMeetingData(): String {
        return meetingGattCharacteristic?.let {
            String(it.value)
        } ?: "None"
    }

    override fun onCreate() {
        super.onCreate()

        val ht = HandlerThread("pax_ble")
        ht.start()
        mWorkHandler = Handler(ht.looper)

        mMainHandler = Handler()
        initBle()

        addGattServer()
    }

    override fun onDestroy() {
        if (starting) {
            mmAdvertiser?.stopAdvertising(advertisingSetCallback)
        }
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        tryStartAdvertising()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        tryStartAdvertising()
        return super.onBind(intent)
    }

    private fun initBle() {
        isSupportBle = false
        mmBluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            showLog("not support Bluetooth")
            return
        }
        if (!bluetoothAdapter.isEnabled) {
            showLog("Bluetooth not enabled")
            return
        }

        if (packageManager?.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE) != true) {
            showLog("not support ble")
            return
        }

        mmAdvertiser = BluetoothAdapter.getDefaultAdapter().bluetoothLeAdvertiser
        if (mmAdvertiser == null) {
            showLog("not support Advertiser")
            return
        }

        // 获取广播服务
        mmBluetoothGattServer =
            mmBluetoothManager?.openGattServer(this, bluetoothGattServerCallback)

        isSupportBle = true
        showUiInfo("initBle succeed, name(${bluetoothAdapter.name})")
    }

    /**
     * 添加Gatt 服务和特征
     */
    private fun addGattServer() {
        showUiInfo("addGattServer isSupportBle($isSupportBle)")
        if (!isSupportBle) return

        //创建服务，并初始化服务的UUID和服务类型。
        val gattService = BluetoothGattService(
            PaxBleConfig.PAX_SERVICE_UUID,
            BluetoothGattService.SERVICE_TYPE_PRIMARY
        )

        // 添加FF信息特征
        gattService.addCharacteristic(PaxBleConfig.buildFFGattCharacteristic())

        //添加meeting信息特征
        meetingGattCharacteristic = PaxBleConfig.buildMeetingGattCharacteristic()
        gattService.addCharacteristic(meetingGattCharacteristic)

        mmBluetoothGattServer?.addService(gattService)
    }

    /**
     * 开始广播
     */
    private fun tryStartAdvertising() {
        showLog("tryStartAdvertising :$starting")
        if (starting) {
            return
        }

        // 广播配置
        //初始化广播设置
        val mmAdvertiseSettings = AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
            .setTimeout(0)
            .setConnectable(true)
            .build()

        //设置广播报文
        val mmAdvertiseData = AdvertiseData.Builder()
            .setIncludeDeviceName(true)
            .setIncludeTxPowerLevel(true)
            .addServiceUuid(ParcelUuid(PaxBleConfig.PAX_SERVICE_UUID))
            .build()

        //设置广播扫描响应报文(可选)
        val mmScanResponseData = PaxBleConfig.buildFF91AdvertiseData()

        //开始广播
        mmAdvertiser?.startAdvertising(
            mmAdvertiseSettings, mmAdvertiseData,
            mmScanResponseData, advertisingSetCallback
        )

        starting = true
        showUiInfo("startAdvertising succeed")
    }

    private val advertisingSetCallback = object : AdvertiseCallback() {
        override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
            showUiInfo("onStartSuccess , isConnectable(${settingsInEffect?.isConnectable})")
        }

        override fun onStartFailure(errorCode: Int) {
            if (errorCode == ADVERTISE_FAILED_DATA_TOO_LARGE) {
                showUiInfo("onStartFailure data too large")
            } else {
                showUiInfo("onStartFailure errorCode($errorCode))")
            }
        }
    }

    private val bluetoothGattServerCallback = object : BluetoothGattServerCallback() {
        //设备连接/断开连接回调
        override fun onConnectionStateChange(device: BluetoothDevice, status: Int, newState: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //连接成功
                when (newState) {
                    BluetoothProfile.STATE_CONNECTED -> {
                        mmDevice = device
                        showUiInfo("onConnectionStateChange($device) : connected")
                    }
                    BluetoothProfile.STATE_DISCONNECTED -> {
                        showUiInfo("onConnectionStateChange($device) : disconnected")
                    }
                    else -> {
                        showUiInfo("onConnectionStateChange($device) : newState($newState)")
                    }
                }
            } else {
                showUiInfo("onConnectionStateChange status($status)")
            }
        }

        //添加本地服务回调
        override fun onServiceAdded(status: Int, service: BluetoothGattService) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                showUiInfo("onServiceAdded success , UUID(${service.uuid})")
            } else {
                showUiInfo("onServiceAdded failed")
            }
        }

        //特征值读取回调
        override fun onCharacteristicReadRequest(
            device: BluetoothDevice,
            requestId: Int,
            offset: Int,
            characteristic: BluetoothGattCharacteristic
        ) {
            val msg = String(characteristic.value)
            showLog("onCharacteristicReadRequest :${characteristic.uuid} >>> $msg")

            showConnectLog("返回数据:$msg")

            // 响应客户端
            mmBluetoothGattServer?.sendResponse(
                device, requestId, BluetoothGatt.GATT_SUCCESS,
                offset, characteristic.value
            )
        }

        //特征值写入回调
        override fun onCharacteristicWriteRequest(
            device: BluetoothDevice, requestId: Int,
            characteristic: BluetoothGattCharacteristic, preparedWrite: Boolean,
            responseNeeded: Boolean, offset: Int, value: ByteArray
        ) {
            val msg = String(characteristic.value)
            showLog("onCharacteristicWriteRequest :${characteristic.uuid} >>> $msg")

            showRcvMsg("Cwrite >>> $msg")

            //刷新该特征值
            characteristic.value = value
            // 响应客户端
            mmBluetoothGattServer?.sendResponse(
                device, requestId, BluetoothGatt.GATT_SUCCESS,
                offset, value
            )
        }

        //描述读取回调
        override fun onDescriptorReadRequest(
            device: BluetoothDevice, requestId: Int, offset: Int,
            descriptor: BluetoothGattDescriptor
        ) {
            val msg = String(descriptor.value)
            showUiInfo("onCharacteristicReadRequest :${descriptor.uuid} >>> $msg")

            showSendMsg("Dread >>> $msg")

            // 响应客户端
            mmBluetoothGattServer?.sendResponse(
                device, requestId, BluetoothGatt.GATT_SUCCESS,
                offset, descriptor.value
            )
        }

        //描述写入回调
        override fun onDescriptorWriteRequest(
            device: BluetoothDevice, requestId: Int, descriptor: BluetoothGattDescriptor,
            preparedWrite: Boolean, responseNeeded: Boolean,
            offset: Int, value: ByteArray
        ) {
            val msg = String(descriptor.value)
            showUiInfo("onDescriptorWriteRequest :${descriptor.uuid} >>> $msg")

            showRcvMsg("Dwrite >>> $msg")

            //刷新描述值
            descriptor.value = value
            // 响应客户端
            mmBluetoothGattServer?.sendResponse(
                device, requestId, BluetoothGatt.GATT_SUCCESS,
                offset, value
            )
        }

        override fun onNotificationSent(device: BluetoothDevice?, status: Int) {
            super.onNotificationSent(device, status)
            if (status == BluetoothGatt.GATT_SUCCESS) {
                showLog("onNotificationSent($device) success")
            } else {
                showLog("onNotificationSent($device) failed($status)")
            }
        }
    }
}