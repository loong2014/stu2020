package com.sunny.module.web.ble.service

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.*
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import com.sunny.lib.base.log.SunLog
import com.sunny.module.web.ble.BleTools
import com.sunny.module.web.ble.client.IBleClientInterface

/**
 * 低功耗蓝牙
 * https://developer.android.com/guide/topics/connectivity/bluetooth-le?hl=zh-cn
 *
 * macbook : ac:bc:32:97:bc:38
 * Redmi K40 : 9C:5A:81:2F:19:39
 * FF 91 Driver : 22:22:ed:16:c2:52
 */
class PaxBleService : Service() {

    private lateinit var mHandler: Handler
    private var mScanning: Boolean = false

    private val SCAN_PERIOD: Long = 200_000

    private var bluetoothAdapter: BluetoothAdapter? = null

    private var bluetoothLeScanner: BluetoothLeScanner? = null

    private var isSupportBle = false

    private val mDeviceSet = mutableSetOf<BluetoothDevice>()
    private val mDeviceAllSet = mutableSetOf<BluetoothDevice>()

    override fun onCreate() {
        super.onCreate()
        log("onCreate")

        val ht = HandlerThread("pax_ble")
        ht.start()
        mHandler = Handler(ht.looper)

        initBluetooth()
    }

    private fun initBluetooth() {
        isSupportBle = false
        if (!BleTools.isSupportBLE(this)) {
            log("Device doesn't support BLE")
            return
        }

        if (!BleTools.isBleEnabled()) {
            log("need turn Bluetooth ON for this device")
            return
        }

        isSupportBle = true

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        bluetoothLeScanner = bluetoothAdapter?.bluetoothLeScanner

        showPairedDevices()
    }

    private fun showPairedDevices() {
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        log("### >>>> show paired devices count(${pairedDevices?.size}) ###")
        pairedDevices?.forEachIndexed { index, device ->
            val deviceName = device.name
            val deviceHardwareAddress = device.address
            log("$index >>> $deviceHardwareAddress --- $deviceName")
        }
        log("### <<<< show paired devices ###")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        log("onBind")
        return mClientBleBinder
    }

    private val mClientBleBinder = object : IBleClientInterface.Stub() {
        override fun sendMsg(msg: String?): Boolean {
            log("sendMsg :$msg")
            return false
        }

        override fun readMsg(): String {
            val sb = StringBuilder()
            sb.append(">>>> show scanResults count(${mDeviceSet.size}) ###")
            mDeviceSet.forEachIndexed { index, device ->
                sb.append("\n$index --->type(${device.type}),name(${device.name}),address(${device.address})")
            }
            sb.append("\n<<<< show scanResults ###")

            val msg = sb.toString()
            log("readMsg :$msg")
            return msg
        }

        override fun sendOpt(opt: Int): Boolean {
            log("sendOpt :$opt")
            return when (opt) {
                1 -> tryStartBleScan()
                2 -> tryStopBleScan()
                else -> false
            }
        }
    }

    private fun logScanResults() {
        log("\n### >>>> show scanResults count(${mDeviceSet.size}) ###")
        mDeviceSet.forEachIndexed { index, device ->
            log("$index >>>type(${device.type}) --- address(${device.address}) --- uuids(${device.uuids}) --- name(${device.name})")
        }
        log("### <<<< show scanResults ###")
    }

    private fun tryStartBleScan(restart: Boolean = false): Boolean {
        log("tryStartBleScan  isSupportBle($isSupportBle)")
        if (isSupportBle) {
            startLeScan()
            return true
        }
        return false
    }

    private fun tryStopBleScan(): Boolean {
        log("tryStopBleScan  isSupportBle($isSupportBle)")
        if (isSupportBle) {
            stopLeScan()
            return true
        }
        return false
    }

    private val mScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            result?.device?.let {
                if (mDeviceAllSet.add(it)) {
                    log("###allDev(${mDeviceAllSet.size}) ---> type(${it.type}) --- address(${it.address}) --- uuids(${it.uuids}) --- name(${it.name})")
                }

                it.takeIf { it.name != null }?.let {
                    if (mDeviceSet.add(it)) {
                        mHandler.removeCallbacks(showScanDevicesTask)
                        mHandler.postDelayed(showScanDevicesTask, 2_000)
                    }
                }
            }
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
//            log("### >>222>> show onBatchScanResults count(${results?.size}) ###")
//            results?.forEachIndexed { index, scanResult ->
//                val deviceName = scanResult.device.name
//                val deviceHardwareAddress = scanResult.device.address
//                log("$index >>> $deviceHardwareAddress --- $deviceName")
//            }
//            log("### <<222<< onBatchScanResults ###")
        }

        override fun onScanFailed(errorCode: Int) {
            log("onScanFailed :$errorCode")
        }
    }

    /**
     * 开始查找 BLE 设备
     * 注意：您仅能扫描蓝牙 LE 设备或传统蓝牙设备，正如蓝牙概览中所述。您无法同时扫描蓝牙 LE 设备和传统蓝牙设备。
     */
    private fun startLeScan() {
        log("startLeScan($mScanning)")

        val filters = mutableListOf<ScanFilter>()
        BleTools.getTargetDevice().let {
            filters.add(
                ScanFilter.Builder()
                    .setDeviceName(it.first)
                    .build()
            )
        }

        val setting = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
            .build()


        if (mScanning) {
            mHandler.removeCallbacks(delayStopTask)
        }
        mHandler.postDelayed(delayStopTask, SCAN_PERIOD)

        mScanning = true
        mDeviceSet.clear()
        bluetoothLeScanner?.startScan(filters, setting, mScanCallback)
    }

    private val delayStopTask = Runnable {
        stopLeScan()
        mHandler.post(showScanDevicesTask)
    }

    private val showScanDevicesTask = Runnable {
        logScanResults()
    }

    /**
     * 停止查找 BLE 设备
     */
    private fun stopLeScan() {
        log("stopLeScan")
        mScanning = false
        mHandler.removeCallbacks(delayStopTask)
        logScanResults()
        bluetoothLeScanner?.stopScan(mScanCallback)
    }

    private fun log(msg: String) {
        SunLog.i("PaxBleService", msg)
    }
}