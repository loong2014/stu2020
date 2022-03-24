package com.sunny.module.ble.pax

import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.os.ParcelUuid
import com.sunny.lib.base.log.SunLog
import com.sunny.module.ble.PaxBleConfig

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
class PaxBleMasterService : PaxBleBaseService() {

    private var scanning = false // 是否开始发送

    // 等待连接的设备
    private val waitConnectDevices = mutableListOf<BluetoothDevice>()

    // 已连接的设备
    private val connectedDeviceList = mutableListOf<DeviceConnectedModel>()

    // 正在连接的设备
    private var mmConnectedDevice: BluetoothDevice? = null

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
                // 通过 ServiceUuid 进行过滤
                .setServiceUuid(ParcelUuid(PaxBleConfig.getServiceUUID()))
                /*
                在设备地址上设置过滤器。
                 */
                .setDeviceAddress("")
                .build(),
        )
    }


    fun buildScanFilters() = listOf<ScanFilter>(
        ScanFilter.Builder()
            // 通过 ServiceUuid 进行过滤
            .setServiceUuid(ParcelUuid(PaxBleConfig.getServiceUUID()))
            .build(),
    )

    /**
     * 构建过滤设置
     */
    fun buildScanSettings() = ScanSettings.Builder()
        // 扫描模式
        .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
        // 返回类型
        .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
        // 匹配模式
        .setMatchMode(ScanSettings.MATCH_MODE_STICKY)
        // 匹配数
        .setNumOfMatches(ScanSettings.MATCH_NUM_MAX_ADVERTISEMENT)
        // 是否返回旧版广告
        .setLegacy(true)
        // 设置物理层模式，仅在 legacy=false时生效
        .setPhy(ScanSettings.PHY_LE_ALL_SUPPORTED)
        // 延时返回扫描结果，0表示立即返回
        .setReportDelay(0)
        .build()


    /**
     * 过滤设置
     */
    private val mmSettings: ScanSettings by lazy {
        ScanSettings.Builder()
            /*
            扫描模式
            SCAN_MODE_OPPORTUNISTIC
                一种特殊的蓝牙 LE 扫描模式。使用此扫描模式的应用程序将被动地侦听其他扫描结果，而无需自行启动 BLE 扫描。
            SCAN_MODE_LOW_POWER(默认)
                在低功耗模式下执行蓝牙 LE 扫描。这是默认扫描模式，因为它消耗的电量最少。如果扫描应用程序不在前台，则强制执行此模式。
            SCAN_MODE_BALANCED
                在平衡功率模式下执行蓝牙 LE 扫描。扫描结果以在扫描频率和功耗之间提供良好折衷的速率返回。
            SCAN_MODE_LOW_LATENCY
                使用最高占空比进行扫描。建议仅在应用程序在前台运行时使用此模式。
             */
            .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
            /*
            返回类型
            CALLBACK_TYPE_ALL_MATCHES(默认)
                为找到的每个与过滤条件匹配的蓝牙广告触发回调。如果没有过滤器处于活动状态，则报告所有广告数据包。
            CALLBACK_TYPE_FIRST_MATCH
                仅针对收到的第一个与过滤条件匹配的广告数据包触发结果回调。
            CALLBACK_TYPE_MATCH_LOST
                当不再从先前已由第一次匹配回调报告的设备接收到广告时接收回调。
             */
            .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
            /*
            匹配模式
            MATCH_MODE_AGGRESSIVE(默认)
                在 Aggressive 模式下，即使信号强度微弱且在一段时间内只有少量的瞄准匹配，硬件也会更快地确定匹配。
            MATCH_MODE_STICKY
                对于粘性模式，在硬件报告之前需要更高的信号强度和目击阈值
             */
            .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
            /*
            匹配数
            MATCH_NUM_MAX_ADVERTISEMENT(默认)
                每个过滤器匹配硬件允许的尽可能多的广告，这取决于硬件中资源的当前能力和可用性
            MATCH_NUM_FEW_ADVERTISEMENT
                每个过滤器匹配少量广告，取决于硬件中资源的当前能力和可用性
            MATCH_NUM_ONE_ADVERTISEMENT
                确定每个过滤器匹配多少广告，因为这是稀缺的硬件资源
             */
            .setNumOfMatches(ScanSettings.MATCH_NUM_MAX_ADVERTISEMENT)
            /*
            设置是否应在扫描结果中仅返回旧版广告。传统广告包括蓝牙核心规范 4.2 及以下规定的广告。默认情况下这是为了与旧应用程序兼容。
            true 如果只返回遗留广告
            true（默认）
             */
            .setLegacy(true)
            /*
            设置此扫描期间要使用的物理层。这仅在 {@link ScanSettings.Builder.setLegacy} 设置为 false 时使用。
            {@link android.bluetooth.BluetoothAdapter。isLeCodedPhySupported} 可以通过调用 {@link android.bluetooth.BluetoothAdapterisLeCodedPhySupported} 来检查是否支持 LE Coded phy。选择不支持的 phy 将导致无法开始扫描。
            ScanSettings.PHY_LE_ALL_SUPPORTED(默认)
                使用所有支持的 PHY 进行扫描。这将检查控制器功能，并在 1Mbit 和 LE 编码 PHY（如果支持）或仅在 1Mbit PHY 上开始扫描。
            BluetoothDevice.PHY_LE_1M
                蓝牙 LE 1M PHY。用于指代 LE 1M 物理通道进行广告、扫描或连接。
            BluetoothDevice.PHY_LE_CODED
                蓝牙 LE 编码 PHY。用于指代用于广告、扫描或连接的 LE 编码物理通道。
             */
            .setPhy(ScanSettings.PHY_LE_ALL_SUPPORTED)
            /*
            设置蓝牙 LE 扫描的报告延迟时间戳。
            reportDelayMillis
            =0(默认) : 立即返回结果
            >0 : 导致扫描结果排队并在请求的延迟之后或内部缓冲区填满时交付
             */
            .setReportDelay(0)
            .build()
    }

    /**
     * 开始扫描
     */
    private fun tryStartScan() {
        showLog("tryStartScan :isSupportBle($isSupportBle) , scanning($scanning)")
        if (!isSupportBle) return

        if (scanning) return

        bluetoothLeScanner?.startScan(buildScanFilters(), buildScanSettings(), scanCallback)
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
        var isFFDevice = true
//        result.scanRecord?.serviceData?.forEach { (parcelUuid, value) ->
//            if (PaxBleConfig.isFF91Service(parcelUuid.uuid, value)) {
//                isFFDevice = true
//                return@forEach
//            }
//        }

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

    private fun showLog(log: String) {
        SunLog.i("BleLog-$device", log)
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

        /*
        回调指示特征写入操作的结果。
        如果在可靠写入事务正在进行时调用此回调，则特征值表示远程设备报告的值。
        应用程序应将此值与要写入的所需值进行比较。如果值不匹配，应用程序必须中止可靠的写入事务。
         */
        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            showLog("读取 :" + String(characteristic.value))
            if (BluetoothGatt.GATT_SUCCESS == status) {
                showConnectLog("读取成功:")
            } else {
                showConnectLog("读取失败($status):")
            }
        }

        //特征写入回调
        override fun onCharacteristicWrite(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic, status: Int
        ) {
            showLog("写入 :" + String(characteristic.value))
            if (BluetoothGatt.GATT_SUCCESS == status) {
                showConnectLog("写入成功:")
            } else {
                showConnectLog("写入失败($status):")
            }
        }

        // 由于远程特征通知而触发的回调。
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?
        ) {
            showLog("特征变化")
            super.onCharacteristicChanged(gatt, characteristic)
        }
    }
}
