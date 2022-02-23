package com.sunny.module.ble.pax

import android.bluetooth.*
import android.bluetooth.le.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.ParcelUuid
import com.sunny.lib.base.log.SunLog

interface ConnectedCallback {
    fun onConnected(device: BluetoothDevice)

    fun onDisconnected(device: BluetoothDevice)

    fun onUiTip(info: String)
}

/**
 * 主设备 启动后开始扫描，发现目标获取广播信息
 *
 * 车机上的PaxLauncher 不停扫描 FF Ctrl 设备
 */
class PaxBleScanService : PaxBleBaseService() {

    lateinit var mWorkHandler: Handler
//    lateinit var mMainHandler: Handler

    //    private var isSupportBle = false // 是否支持ble
    private var scanning = false // 是否开始发送

//private    var bluetoothLeScanner: BluetoothLeScanner? = null

    // 等待连接的设备
    private val waitConnectDevices = mutableListOf<BluetoothDevice>()

    // 已连接的设备
    private val connectedDeviceList = mutableListOf<DeviceConnectedModel>()

    // 正在连接的设备
    private var mmConnectedDevice: BluetoothDevice? = null

    private fun showLog(msg: String) {
        SunLog.i("BleLog", msg)
    }

    /**
     * 显示收到的消息
     */
    private fun showRcvMsg(msg: String) {
        SunLog.i("BleLog-Rcv", msg)
    }

    /**
     * 显示发送的消息
     */
    private fun showSendMsg(msg: String) {
        SunLog.i("BleLog-Send", msg)
    }

    override fun doSendMsg(msg: String): Boolean {
        mmConnectedDevice?.let {
            getConnectedModel(it)?.sendMeetingMsg(msg)
        }
        return true
    }

    override fun doReadMsg(): String {
        return getConnectedModel(mmConnectedDevice)?.readMeetingMsg() ?: "None"
    }

    override fun onCreate() {
        super.onCreate()

        val ht = HandlerThread("pax_ble")
        ht.start()
        mWorkHandler = Handler(ht.looper)
        mMainHandler = Handler()

        initBle()
    }

    override fun onDestroy() {
        tryStopScan()
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        tryStartScan()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        tryStartScan()
        return super.onBind(intent)
    }

    private fun initBle() {
        isSupportBle = false
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

        bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
        if (bluetoothLeScanner == null) {
            showLog("not support scanner")
            return
        }
        isSupportBle = true
        showLog("initBle succeed, name(${bluetoothAdapter.name})")
    }

    /**
     * 过滤规则
     */
    private val mmFilters: List<ScanFilter> by lazy {
        listOf<ScanFilter>(
            ScanFilter.Builder()
                .setServiceUuid(ParcelUuid(PaxBleConfig.PAX_SERVICE_UUID))
                .build()
        )
    }

    /**
     * 过滤设置
     */
    private val mmSettings: ScanSettings by lazy {
        ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
            .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
            .setMatchMode(ScanSettings.MATCH_MODE_STICKY)
            .build()
    }

    /**
     * 开始扫描
     */
    private fun tryStartScan() {
        showLog("tryStartScan :isSupportBle($isSupportBle) , scanning($scanning)")
        if (!isSupportBle) return

        if (scanning) return

        bluetoothLeScanner?.startScan(mmFilters, mmSettings, scanCallback)
        scanning = true
        showLog("tryStartScan succeed")
    }

    /**
     * 停止扫描
     */
    private fun tryStopScan() {
        showLog("tryStopScan :isSupportBle($isSupportBle) , scanning($scanning)")
        if (isSupportBle && scanning) {
            scanning = false
            bluetoothLeScanner?.stopScan(scanCallback)
        }
    }

    /**
     * 延时开始扫描
     */
    private fun delayStartScan() {
        mMainHandler.postDelayed({
            showLog("tryStartScan by delay")
            tryStartScan()
        }, PaxBleConfig.DELAY_SCAN_INTERVAL)
    }

    /**
     * 扫描结果回调
     */
    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            if (result == null) return
            mWorkHandler.post {
                dealScanResult(result)
            }
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
        }

        override fun onScanFailed(errorCode: Int) {
            showLog("onScanFailed :$errorCode")
            tryStopScan()
            delayStartScan()
        }
    }

    /**
     * 处理扫描到的设备
     */
    private fun dealScanResult(result: ScanResult?) {
        if (result == null) return

        showLog("${result.device} , rssi(${result.rssi}) , dataStatus(${result.dataStatus})")

        if (waitConnectDevices.contains(result.device)) {
            // 已经添加到等待连接队列
            return
        }

        //解析蓝牙广播数据报文
        var isFFDevice = false
        result.scanRecord?.serviceData?.forEach { (parcelUuid, value) ->
            if (PaxBleConfig.isFF91Service(parcelUuid.uuid, value)) {
                isFFDevice = true
                return@forEach
            }
        }

        // FF 设备
        if (!isFFDevice) {
            // 不是 FF Ctrl 设备
            return
        }
        if (hasConnected(result.device)) {
            // 设备已连接
            return
        }

        // 添加到等待连接队列
        waitConnectDevices.add(result.device)
        showLog("add device(${result.device}) to waitConnectDevices(${waitConnectDevices.size})")
        tryConnectNext()
    }

    /**
     * 是否正在连接
     */
    private fun hasConnected(device: BluetoothDevice): Boolean {
        val each = connectedDeviceList.iterator()
        while (each.hasNext()) {
            if (each.next().isSame(device)) {
                return true
            }
        }
        return false
    }

    /**
     * 获取正在连接的设备
     */
    private fun getConnectedModel(device: BluetoothDevice?): DeviceConnectedModel? {
        if (device == null) return null
        val each = connectedDeviceList.iterator()
        var model: DeviceConnectedModel? = null
        while (each.hasNext()) {
            model = each.next()
            if (model.isSame(device)) {
                break
            }
        }
        return model
    }

    /**
     * 删除一个正在连接的设备
     */
    private fun delConnectedModel(device: BluetoothDevice) {
        val each = connectedDeviceList.iterator()
        var model: DeviceConnectedModel? = null
        while (each.hasNext()) {
            model = each.next()
            if (model.isSame(device)) {
                each.remove()
                break
            }
        }
        // 再次停止连接，防止删除正在连接的设备
        model?.stopConnect()
    }

    private fun stopConnected(device: BluetoothDevice) {
        getConnectedModel(device)?.stopConnect()
    }

    private fun stopAllConnected() {
        val each = connectedDeviceList.iterator()
        while (each.hasNext()) {
            each.next().stopConnect()
        }
    }

    /**
     * 尝试连接下一个设备
     */
    private fun tryConnectNext() {
        if (mmConnectedDevice != null) {
            // 有设备正在连接，需等待上一个设备连接成功
            return
        }
        showUiInfo("already for connect next")
        if (waitConnectDevices.size > 0) {
            waitConnectDevices.removeFirst().run {
                showUiInfo("start connect($this)")
                mWorkHandler.post {
                    doConnected(this)
                }
            }
        }
    }

    private val mmConnectedCallback = object : ConnectedCallback {
        override fun onConnected(device: BluetoothDevice) {
            showUiInfo("$device connected")
            // 设备连接成功，可进行下一个设备的连接
//            mmConnectedDevice = null
//            tryConnectNext()
        }

        override fun onDisconnected(device: BluetoothDevice) {
            showUiInfo("$device onDisconnected")
            delConnectedModel(device)
            // 设备断开连接，可以进行下一个设备连接
            mmConnectedDevice = null
            tryConnectNext()
        }

        override fun onUiTip(info: String) {
            showUiInfo(info)
        }
    }

    private fun doConnected(device: BluetoothDevice) {
        mmConnectedDevice = device

        val connectedModel = DeviceConnectedModel(this, device, mmConnectedCallback)
        connectedModel.startConnect()
        connectedDeviceList.add(connectedModel)
    }
}

class DeviceConnectedModel(
    private val context: Context,
    private val device: BluetoothDevice,
    private val connectedCallback: ConnectedCallback
) {

    private var mmBluetoothGatt: BluetoothGatt? = null

    // meeting 信息特征
    private var meetingGattCharacteristic: BluetoothGattCharacteristic? = null

    private var isConnected = false

    fun isSame(device: BluetoothDevice): Boolean {
        return this.device == device
    }

    private fun showConnectLog(msg: String) {
        connectedCallback.onUiTip("$device --- $msg")
    }

    /**
     * 发送meeting信息给从设备
     */
    fun sendMeetingMsg(msg: String) {
        if (!isConnected) return
        meetingGattCharacteristic?.run {
            setValue(msg)
            mmBluetoothGatt?.writeCharacteristic(this)
        }
    }

    /**
     * 读取meeting信息
     */
    fun readMeetingMsg(): String {
        if (!isConnected) return ""

        return meetingGattCharacteristic?.let {
            mmBluetoothGatt?.readCharacteristic(it)
            String(it.value)
        } ?: ""
    }

    fun startConnect() {
        showConnectLog("connectGatt start")
        try {
            gattCallback.curDevice = device
            mmBluetoothGatt =
                device.connectGatt(context, false, gattCallback, BluetoothDevice.TRANSPORT_LE)
        } catch (e: Exception) {
            showConnectLog("connectGatt error :$e")
            stopConnect()

            connectedCallback.onDisconnected(device)
        }
    }

    fun stopConnect() {

    }


    private fun dealServicesDiscovered(gattServices: List<BluetoothGattService>?) {
        meetingGattCharacteristic = PaxBleConfig.findMeetingGattCharacteristic(gattServices)
    }

    private var gattCallback = object : BluetoothGattCallback() {

        var curDevice: BluetoothDevice? = null

        // 连接设备状态
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            showConnectLog("onConnectionStateChange status($status) , newState($newState)")
            when (newState) {
                // 连接成功
                BluetoothProfile.STATE_CONNECTED -> {
                    showConnectLog("STATE_CONNECTED")
                    isConnected = true

                    mmBluetoothGatt?.discoverServices()
                }

                BluetoothProfile.STATE_DISCONNECTED -> {
                    showConnectLog("STATE_DISCONNECTED")
                    isConnected = false
                    stopConnect()
                    connectedCallback.onDisconnected(device)
                }
            }
        }

        // 发现设备服务
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            when (status) {
                BluetoothGatt.GATT_SUCCESS -> {
                    showConnectLog("连接成功")
                    dealServicesDiscovered(gatt.services)
                    connectedCallback.onConnected(device)

                    sendMeetingMsg("连接成功")
                }
                else -> {
                    showConnectLog("连接失败($status)")
                    stopConnect()
                }
            }
        }

        //特征读取回调
        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            if (BluetoothGatt.GATT_SUCCESS == status) {
                showConnectLog("读取成功:" + String(characteristic.value))
            } else {
                showConnectLog("读取失败($status):" + String(characteristic.value))

            }
        }

        //特征写入回调
        override fun onCharacteristicWrite(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic, status: Int
        ) {
            if (BluetoothGatt.GATT_SUCCESS == status) {
                showConnectLog("写入成功:" + String(characteristic.value))
            } else {
                showConnectLog("写入失败($status):" + String(characteristic.value))
            }
        }
    }
}
