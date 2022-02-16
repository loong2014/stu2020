package com.sunny.module.web.ble

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import com.sunny.module.web.ble.client.IBleClientInterface

/**
 * 低功耗蓝牙
 * https://developer.android.com/guide/topics/connectivity/bluetooth-le?hl=zh-cn
 *
 * macbook : ac:bc:32:97:bc:38
 * Redmi K40 : 9C:5A:81:2F:19:39
 * FF 91 Driver : 22:22:ed:16:c2:52
 */
open class PaxBleServiceBase : Service() {

    lateinit var mHandler: Handler

    val SCAN_PERIOD: Long = 200_000

    var bluetoothAdapter: BluetoothAdapter? = null

    var bluetoothLeScanner: BluetoothLeScanner? = null

    var isSupportBle = false

    val mDeviceSet = mutableSetOf<BluetoothDevice>()
    val mDeviceAllSet = mutableSetOf<BluetoothDevice>()

    var tipInfo: String = ""

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

        doInit()
    }

    open fun doInit() {
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
            if (msg.isNullOrBlank()) {
                return false
            }
            return doSendMsg(msg)
        }

        override fun readMsg(): String {
            log("readMsg")
            return doReadMsg()
        }

        override fun readDeviceInfo(opt: Int): String {
            log("readDeviceInfo :$opt")
            if (!isSupportBle) return "NotSupport"

            return when (opt) {
                BleConfig.MsgTypeRecentTip -> tipInfo

                BleConfig.MsgTypePairedDevices -> bluetoothAdapter?.bondedDevices?.let {
                    BleTools.buildDevicesShowMsg(it, "showPairedDevices")
                } ?: "No Paired Devices"

                BleConfig.MsgTypeScanDevices -> BleTools.buildDevicesShowMsg(
                    mDeviceSet,
                    "showScannedDevices"
                )

                else -> "UnSupportOpt :$opt"
            }
        }

        override fun sendOpt(opt: Int): Boolean {
            log("sendOpt :$opt")
            if (!isSupportBle) return false

            when (opt) {
                BleConfig.OptTypeScanStart -> doStartScan()
                BleConfig.OptTypeScanStop -> doStopScan()
                BleConfig.OptTypeConnectStart -> doStartConnect()
                BleConfig.OptTypeConnectStop -> doStopConnect()
            }
            return true
        }
    }

    open fun doSendMsg(msg: String): Boolean {
        return false
    }

    open fun doReadMsg(): String {
        return ""
    }

    open fun doStartScan() {

    }

    open fun doStopScan() {

    }

    open fun doStartConnect() {

    }

    open fun doStopConnect() {

    }

    open fun tryStartBleScan(restart: Boolean = false): Boolean {
        log("tryStartBleScan  isSupportBle($isSupportBle)")
        if (isSupportBle) {
            return true
        }
        return false
    }

    fun showTipInfo(msg: String) {
        tipInfo = msg
        log(msg)
    }

    fun tryReadMsg(): String {
        return tipInfo
//        val sb = StringBuilder()
//        sb.append(">>>> show scanResults count(${mDeviceSet.size}) ###")
//        mDeviceSet.forEachIndexed { index, device ->
//            sb.append("\n$index --->type(${device.type}),name(${device.name}),address(${device.address})")
//        }
//        sb.append("\n<<<< show scanResults ###")
//
//        val msg = sb.toString()
//        log("readMsg :$msg")
//        return msg
    }

    open fun trySendMsg(msg: String): Boolean {
        return true
    }


    open fun tryStopBleScan(): Boolean {
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
        mHandler.removeCallbacks(delayStopTask)
        logScanResults()
        bluetoothLeScanner?.stopScan(mScanCallback)
    }

    override fun onDestroy() {
        doRelease()
        super.onDestroy()
    }

    open fun doRelease() {

    }

    fun logScanResults() {
        log(BleTools.buildDevicesShowMsg(mDeviceSet, "logScanResults", true))
    }

    fun log(msg: String) {
        BleConfig.bleLog(msg)
    }
}