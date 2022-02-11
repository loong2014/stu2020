package com.sunny.module.web.ble.service

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import com.sunny.lib.base.log.SunLog
import com.sunny.module.web.ble.BleConnectService
import com.sunny.module.web.ble.BleTools
import com.sunny.module.web.ble.client.IBleClientInterface

private val GoalDeviceName = "Redmi"

/**
 * 低功耗蓝牙
 * https://developer.android.com/guide/topics/connectivity/bluetooth-le?hl=zh-cn
 */
class PaxBleService : Service() {

    private lateinit var mHandler: Handler
    private var mScanning: Boolean = false

    private val SCAN_PERIOD: Long = 20_000

    private var bluetoothAdapter: BluetoothAdapter? = null

    private var bluetoothLeScanner: BluetoothLeScanner? = null

    private var isSupportBle = false

    private val mDeviceSet = mutableSetOf<BluetoothDevice>()

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

    override fun onBind(intent: Intent?): IBinder? {
        log("onBind")
        tryStartBleScan()
        return mClientBleBinder
    }

    private val mClientBleBinder = object : IBleClientInterface.Stub() {
        override fun sendMsg(msg: String?): Boolean {
            log("sendMsg :$msg")
            return bleControl?.sendMsg(msg) ?: false
        }

        override fun readMsg(): String {
            return bleControl?.readMsg() ?: "NotConnect"
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand")
        tryStartBleScan()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun tryStartBleScan(restart: Boolean = false) {
        log("tryStartBleScan  isSupportBle($isSupportBle)")
        if (isSupportBle) {
            startLeScan()
        }
    }

    /**
     * 开始查找 BLE 设备
     * 注意：您仅能扫描蓝牙 LE 设备或传统蓝牙设备，正如蓝牙概览中所述。您无法同时扫描蓝牙 LE 设备和传统蓝牙设备。
     */
    private fun startLeScan() {
        log("startLeScan")
        mDeviceSet.clear()
        mHandler.postDelayed(delayStopTask, SCAN_PERIOD)
        mScanning = true
        bluetoothLeScanner?.startScan(mScanCallback)
    }

    private val delayStopTask = Runnable {
        stopLeScan()
        mHandler.post(showScanDevicesTask)
    }

    /**
     * 停止查找 BLE 设备
     */
    private fun stopLeScan() {
        log("stopLeScan")
        mScanning = false
        bluetoothLeScanner?.stopScan(mScanCallback)
    }

    private val showScanDevicesTask = Runnable {
        log("\n### >>111>> show scanResults count(${mDeviceSet.size}) ###")
        var needConnectDevice: BluetoothDevice? = null
        mDeviceSet.forEachIndexed { index, device ->
            log("$index >>>type(${device.type}) --- address(${device.address}) --- uuids(${device.uuids}) --- name(${device.name})")
            if (device.name.contains(GoalDeviceName)) {
                needConnectDevice = device
            }
        }
        log("### <<111<< show scanResults ###")
        needConnectDevice?.let {
            stopLeScan()
            startDeviceConnectService(it)
        }
    }

    private fun startDeviceConnectService(device: BluetoothDevice) {
        log("startDeviceConnectService name(${device.name})")
        val bundle = Bundle()
        bundle.putParcelable("ble_device", device)
        val intent = Intent(this, BleConnectService::class.java)
        intent.putExtras(bundle)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    var bleControl: IBleClientInterface? = null
    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            bleControl = IBleClientInterface.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }
    }

    private val mScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
//            log("onScanResult($callbackType) >>> ${result?.device} --- ${result?.device?.uuids} --- ${result?.device?.name}")
            result?.device?.takeIf { it.name != null }?.let {
                if (mDeviceSet.add(it)) {
                    mHandler.removeCallbacks(showScanDevicesTask)
                    mHandler.postDelayed(showScanDevicesTask, 2_000)
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

    private fun log(msg: String) {
        SunLog.i("PaxBleService", msg)
    }
}