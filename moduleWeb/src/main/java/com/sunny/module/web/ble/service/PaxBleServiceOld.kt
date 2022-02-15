package com.sunny.module.web.ble.service

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.BluetoothLeScanner
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import com.sunny.lib.base.log.SunLog
import com.sunny.module.web.ble.BleTools
import com.sunny.module.web.ble.TargetDeviceMac
import com.sunny.module.web.ble.client.IBleClientInterface

/**
 * 传统蓝牙
 * https://developer.android.com/guide/topics/connectivity/bluetooth?hl=zh-cn#FindDevices
 */
class PaxBleServiceOld : Service() {

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

        // Register for broadcasts when a device is discovered.
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)
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

    // Create a BroadcastReceiver for ACTION_FOUND.
    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

                    device?.let {
                        if (mDeviceAllSet.add(it)) {
                            log("###allDev(${mDeviceAllSet.size}) ---> type(${it.type}) --- address(${it.address}) --- uuids(${it.uuids}) --- name(${it.name})")
                        }
                        it.takeIf { it.name != null }?.let {
                            if (mDeviceSet.add(it)) {
                                mHandler.removeCallbacks(showScanDevicesTask)
                                mHandler.postDelayed(showScanDevicesTask, 2_000)
                            }
                        }

                        // 发现目标设备
                        if (it.name == TargetDeviceMac) {
                            stopLeScan()
                            tryStartConnect(it)
                        }
                    }
                }
            }
        }
    }

    private val delayStopTask = Runnable {
        stopLeScan()
        mHandler.post(showScanDevicesTask)
    }

    private val showScanDevicesTask = Runnable {
        logScanResults()
    }

    private fun startLeScan() {
        log("startLeScan($mScanning),${bluetoothAdapter?.isDiscovering}")

        if (mScanning) {
            mHandler.removeCallbacks(delayStopTask)
        }
        mHandler.postDelayed(delayStopTask, SCAN_PERIOD)

        mScanning = true
        mDeviceSet.clear()
        val isOk = bluetoothAdapter?.startDiscovery()
        log("startDiscovery($isOk)")
    }

    /**
     * 停止查找 BLE 设备
     */
    private fun stopLeScan() {
        log("stopLeScan")
        mScanning = false
        mHandler.removeCallbacks(delayStopTask)
        logScanResults()

        bluetoothAdapter?.cancelDiscovery()
    }

    private fun tryStartConnect(dev: BluetoothDevice) {

    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    private fun log(msg: String) {
        SunLog.i("PaxBleService", msg)
    }
}