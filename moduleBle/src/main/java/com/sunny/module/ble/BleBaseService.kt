package com.sunny.module.ble

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.BluetoothLeScanner
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder

/**
 * 蓝牙服务的基类
 */
abstract class BleBaseService : Service() {

    lateinit var mHandler: Handler
    lateinit var mMainHandler: Handler

    val SCAN_PERIOD: Long = 200_000

    var bluetoothAdapter: BluetoothAdapter? = null

    var bluetoothLeScanner: BluetoothLeScanner? = null

    var isSupportBle = false

    val mDeviceSet = mutableSetOf<BluetoothDevice>()
    val mDeviceAllSet = mutableSetOf<BluetoothDevice>()

    var tipInfo: String = ""

    var bleCallback: IBleCallbackInterface? = null

    open fun log(msg: String) {
        BleConfig.bleLog("$this >>> $msg")
    }

    fun showTipInfo(msg: String) {
        tipInfo = msg
        log(msg)
    }

    fun logScanResults() {
        log(BleTools.buildDevicesShowMsg(mDeviceSet, "logScanResults", true))
    }

    abstract fun doRelease()

    abstract fun doInit()

    abstract fun doSendMsg(msg: String): Boolean

    abstract fun doReadMsg(): String

    abstract fun doStartScan()

    abstract fun doStopScan()

    abstract fun doStartConnect(address: String): Boolean

    abstract fun doStopConnect()

    override fun onCreate() {
        super.onCreate()
        log("onCreate")

        val ht = HandlerThread("pax_ble")
        ht.start()
        mHandler = Handler(ht.looper)

        mMainHandler = Handler()

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

    override fun onDestroy() {
        log("onDestroy")
        doRelease()
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        log("onBind :$mClientBleBinder")
        return mClientBleBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        log("onUnbind :$mClientBleBinder")
        return super.onUnbind(intent)
    }

    private val mClientBleBinder = object : IBleClientInterface.Stub() {
        override fun registerCallBack(callback: IBleCallbackInterface?) {
            bleCallback = callback
        }

        override fun unRegisterCallBack(callback: IBleCallbackInterface?) {
            bleCallback = callback
        }

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

                BleConfig.MsgTypeScanDevices -> BleTools.buildDevicesShowMsg(
                    mDeviceSet,
                    "showScannedDevices"
                )

                else -> "UnSupportOpt :$opt"
            }
        }

        override fun startScan(): Boolean {
            log("startScan")
            doStartScan()
            return true
        }

        override fun stopScan(): Boolean {
            log("stopScan")
            doStopScan()
            return true
        }

        override fun startConnect(address: String?): Boolean {
            log("startConnect :$address")
            if (address.isNullOrBlank()) {
                return false
            }
            return doStartConnect(address)
        }

        override fun stopConnect(): Boolean {
            log("stopConnect")
            doStopConnect()
            return true
        }

        override fun sendOpt(opt: Int): Boolean {
            return true
        }
    }
}