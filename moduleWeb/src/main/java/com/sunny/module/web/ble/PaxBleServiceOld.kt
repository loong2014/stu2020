package com.sunny.module.web.ble

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import com.sunny.lib.base.log.SunLog

/**
 * 传统蓝牙
 * https://developer.android.com/guide/topics/connectivity/bluetooth?hl=zh-cn#FindDevices
 */
class PaxBleServiceOld : Service() {

    private lateinit var mHandler: Handler
    private var mScanning: Boolean = false

    private val SCAN_PERIOD: Long = 3_000


    private lateinit var bluetoothAdapter: BluetoothAdapter

    private lateinit var bluetoothLeScanner: BluetoothLeScanner

    override fun onCreate() {
        super.onCreate()
        log("onCreate")

        val ht = HandlerThread("pax_ble")
        ht.start()
        mHandler = Handler(ht.looper)

        initBluetooth()
    }

    private fun initBluetooth() {
        if (!BleTools.isSupportBluetooth()) {
            log("Device doesn't support Bluetooth")
            return
        }

        if (!BleTools.isBleEnabled()) {
            log("need turn Bluetooth ON for this device")
            return
        }

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner

        showPairedDevices()

        // Register for broadcasts when a device is discovered.
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)
    }

    private fun showPairedDevices() {
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices
        log("### >>> show paired devices count(${pairedDevices?.size}) ###")
        pairedDevices?.forEachIndexed { index, device ->
            val deviceName = device.name
            val deviceHardwareAddress = device.address
            log("$index >>> $deviceHardwareAddress --- $deviceName")
        }
        log("### <<<< show paired devices ###")
    }

    override fun onBind(intent: Intent?): IBinder? {
        log("onBind")
        tryStartBleScan()
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand")
        tryStartBleScan()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun tryStartBleScan(restart: Boolean = false) {
        val isOk = bluetoothAdapter.startDiscovery()
        log("startDiscovery($isOk)")
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    log("onReceive >>> $device --- ${device?.name}")
                }
            }
        }
    }

    /**
     * 查找 BLE 设备
     * 注意：您仅能扫描蓝牙 LE 设备或传统蓝牙设备，正如蓝牙概览中所述。您无法同时扫描蓝牙 LE 设备和传统蓝牙设备。
     */
    private fun startLeScan() {
        log("startLeScan isEnabled :${bluetoothAdapter.isEnabled}")
        mHandler.postDelayed(delayStopTask, SCAN_PERIOD)
        mScanning = true
//        如果您想扫描特定类型的外围设备，则可调用 startLeScan(UUID[], BluetoothAdapter.LeScanCallback)，
//        它会提供一组 UUID 对象，用于指定您的应用支持的 GATT 服务。

//        bluetoothAdapter.startLeScan(mLeScanCallback)
        bluetoothLeScanner.startScan(mScanCallback)
    }

    private fun stopLeScan() {
        log("stopLeScan")
        mScanning = false
//        bluetoothAdapter.startLeScan(mLeScanCallback)
        bluetoothLeScanner.stopScan(mScanCallback)
    }

    private val mLeScanCallback = BluetoothAdapter.LeScanCallback { device, rssi, scanRecord ->
        log("onLeScan  rssi($rssi) , $device")
        device?.let { dev: BluetoothDevice ->
            log("dev($dev) , name(${dev.name}) , type(${dev.type}) , uuids(${dev.uuids})")
        }
    }
    private val mScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            log("onScanResult  callbackType($callbackType)")
            result?.device?.let { dev: BluetoothDevice ->
                log("dev($dev) , name(${dev.name}) , type(${dev.type}) , uuids(${dev.uuids})")
            }
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            log("onBatchScanResults size :${results?.size}")
//            results?.forEachIndexed { index, scanResult ->
//                scanResult.device?.let { dev: BluetoothDevice ->
//                    log("$index -> dev($dev) , name(${dev.name}) , type(${dev.type}) , uuids(${dev.uuids})")
//                }
//            }
        }

        override fun onScanFailed(errorCode: Int) {
            log("onScanFailed :$errorCode")
        }
    }
    private val delayStopTask = Runnable {
        stopLeScan()
    }


//
//    private var connectionState = STATE_DISCONNECTED
//
//    // Various callback methods defined by the BLE API.
//    private val gattCallback = object : BluetoothGattCallback() {
//        override fun onConnectionStateChange(
//            gatt: BluetoothGatt,
//            status: Int,
//            newState: Int
//        ) {
//            val intentAction: String
//            when (newState) {
//                BluetoothProfile.STATE_CONNECTED -> {
//                    intentAction = ACTION_GATT_CONNECTED
//                    connectionState = STATE_CONNECTED
//                    broadcastUpdate(intentAction)
//                    Timber.i("Connected to GATT server.")
//                    Timber.i(
//                        "Attempting to start service discovery: " +
//                                bluetoothGatt?.discoverServices()
//                    )
//                }
//                BluetoothProfile.STATE_DISCONNECTED -> {
//                    intentAction = ACTION_GATT_DISCONNECTED
//                    connectionState = STATE_DISCONNECTED
//                    Timber.i("Disconnected from GATT server.")
//                    broadcastUpdate(intentAction)
//                }
//            }
//        }
//
//        // New services discovered
//        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
//            when (status) {
//                BluetoothGatt.GATT_SUCCESS -> {
//                    broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED)
//
//                    displayGattServices(gatt.services)
//                }
//                else -> Timber.w("onServicesDiscovered received: $status")
//            }
//        }
//
//        // Result of a characteristic read operation
//        override fun onCharacteristicRead(
//            gatt: BluetoothGatt,
//            characteristic: BluetoothGattCharacteristic,
//            status: Int
//        ) {
//            when (status) {
//                BluetoothGatt.GATT_SUCCESS -> {
//                    broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic)
//                }
//            }
//        }
//    }
//
//    lateinit var mGattCharacteristics: List<BluetoothGattCharacteristic>
//
//    /**
//     * 读取 BLE 属性
//     * 当您的 Android 应用成功连接到 GATT 服务器并发现服务后，应用便可在支持的位置读取和写入属性
//     */
//    private fun displayGattServices(gattServices: List<BluetoothGattService>?) {
//        if (gattServices == null) return
//        var uuid: String?
//        val unknownServiceString: String = "resources.getString(R.string.unknown_service)"
//        val unknownCharaString: String = "resources.getString(R.string.unknown_characteristic)"
//        val gattServiceData: MutableList<HashMap<String, String>> = mutableListOf()
//        val gattCharacteristicData: MutableList<ArrayList<HashMap<String, String>>> =
//            mutableListOf()
//        mGattCharacteristics = mutableListOf()
//
//        // Loops through available GATT Services.
//        gattServices.forEach { gattService ->
//            val currentServiceData = HashMap<String, String>()
//            uuid = gattService.uuid.toString()
////            currentServiceData["LIST_NAME"] = SampleGattAttributes.lookup(uuid, unknownServiceString)
////            currentServiceData["LIST_UUID"] = uuid
//            gattServiceData += currentServiceData
//
//            val gattCharacteristicGroupData: ArrayList<HashMap<String, String>> = arrayListOf()
//            val gattCharacteristics = gattService.characteristics
//            val charas: MutableList<BluetoothGattCharacteristic> = mutableListOf()
//
//            // Loops through available Characteristics.
//            gattCharacteristics.forEach { gattCharacteristic ->
//                charas += gattCharacteristic
//                val currentCharaData: HashMap<String, String> = hashMapOf()
//                uuid = gattCharacteristic.uuid.toString()
////                currentCharaData["LIST_NAME"] = SampleGattAttributes.lookup(uuid, unknownCharaString)
////                currentCharaData["LIST_UUID"] = uuid
//                gattCharacteristicGroupData += currentCharaData
//            }
//            mGattCharacteristics += charas
//            gattCharacteristicData += gattCharacteristicGroupData
//        }
//    }
//
//    private fun broadcastUpdate(
//        action: String,
//        characteristic: BluetoothGattCharacteristic? = null
//    ) {
//        val intent = Intent(action)
//        characteristic?.let {
//            when (characteristic.uuid) {
//                UUID_HEART_RATE_MEASUREMENT -> {
//                    val flag = characteristic.properties
//                    val format = when (flag and 0x01) {
//                        0x01 -> {
//                            Log.d(TAG, "Heart rate format UINT16.")
//                            BluetoothGattCharacteristic.FORMAT_UINT16
//                        }
//                        else -> {
//                            Log.d(TAG, "Heart rate format UINT8.")
//                            BluetoothGattCharacteristic.FORMAT_UINT8
//                        }
//                    }
//                    val heartRate = characteristic.getIntValue(format, 1)
//                    Log.d(TAG, String.format("Received heart rate: %d", heartRate))
//                    intent.putExtra(EXTRA_DATA, (heartRate).toString())
//
//                }
//                else -> {
//                    // For all other profiles, writes the data formatted in HEX.
//                    val data: ByteArray? = characteristic.value
//                    if (data?.isNotEmpty() == true) {
//                        val hexString: String = data.joinToString(separator = " ") {
//                            String.format("%02X", it)
//                        }
//                        intent.putExtra(EXTRA_DATA, "$data\n$hexString")
//                    } else {
//
//                    }
//                }
//            }
//        }
//        sendBroadcast(intent)
//    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private fun log(msg: String) {
        SunLog.i("PaxBleService", msg)
    }
}