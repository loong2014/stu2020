package com.sunny.module.ble.master

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanFilter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import com.sunny.lib.base.log.SunLog
import com.sunny.module.ble.PaxBleConfig

/**
 * 主设备 启动后开始扫描，发现目标获取广播信息
 *
 * 车机上的PaxLauncher 不停扫描 FF Ctrl 设备
 */
abstract class PaxBleMasterService : Service() {

    lateinit var mWorkHandler: Handler
    lateinit var mMainHandler: Handler

    // 是否支持BLE
    var isSupportBle = false

    lateinit var fpdConnectThread: PaxBleFpdConnectThread
    lateinit var rsdConnectThread: PaxBleRsdConnectThread

    abstract fun doInit()

    abstract fun doRelease()

    /**
     * 开始扫描
     */
    abstract fun doStartScan()

    /**
     * 停止扫描
     */
    abstract fun doStopScan()


    override fun onCreate() {
        super.onCreate()

        val ht = HandlerThread("pax_ble")
        ht.start()
        mWorkHandler = Handler(ht.looper)

        mMainHandler = Handler(Looper.getMainLooper())

        fpdConnectThread = PaxBleFpdConnectThread(this)
        rsdConnectThread = PaxBleRsdConnectThread(this)

        doInit()
    }

    override fun onDestroy() {
        doRelease()
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        tryStartScan()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        tryStartScan()
        return null
    }

    /**
     * 尝试开始ble扫描
     */
    private fun tryStartScan() {
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
        if (bluetoothAdapter.bluetoothLeScanner == null) {
            showLog("not support scanner")
            return
        }
        isSupportBle = true
        showLog("initBle succeed, name(${bluetoothAdapter.name})")
        doStartScan()
    }

    /**
     * 构建过滤条件
     */
    fun buildScanFilters(): List<ScanFilter> {
        val list = mutableListOf<ScanFilter>()
        ScanFilter.Builder()
            .setServiceUuid(ParcelUuid(PaxBleConfig.getServiceUUID()))
            .build().also {
                list.add(it)
            }
//        fpdConnectThread.buildScanFilter()?.also {
//            list.add(it)
//        }
        rsdConnectThread.buildScanFilter()?.also {
            list.add(it)
        }
        return list
    }

    /**
     * 显示日志
     */
    fun showLog(log: String) {
        SunLog.i("BleLog-", log)
//        Timber.i("PaxBle-$log")
    }

    /**
     * 显示提示
     */
    fun showTip(tip: String) {
        SunLog.i("BleLog-", tip)
//        Timber.i("PaxBle-$tip")
    }
}

