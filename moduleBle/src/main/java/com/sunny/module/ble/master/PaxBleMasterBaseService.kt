package com.sunny.module.ble.master

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanSettings
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import com.sunny.module.ble.PaxBleConfig
import com.sunny.module.ble.SEAT_FPD_NAME
import com.sunny.module.ble.SEAT_RSD_NAME
import com.sunny.module.ble.PaxBleCommonService

/**
 * 主设备 启动后开始扫描，发现目标获取广播信息
 *
 * 车机上的PaxLauncher 不停扫描 FF Ctrl 设备
 */
internal abstract class PaxBleMasterBaseService : PaxBleCommonService() {

//    lateinit var mWorkHandler: Handler
//    lateinit var mMainHandler: Handler

    // 是否支持BLE
    var isSupportBle = false

    lateinit var fpdConnectThread: PaxBleConnectThread
    lateinit var rsdConnectThread: PaxBleConnectThread

    override fun onCreate() {
        super.onCreate()

        val ht = HandlerThread("pax_ble")
        ht.start()
        mWorkHandler = Handler(ht.looper)

        mMainHandler = Handler(Looper.getMainLooper())

        fpdConnectThread = PaxBleConnectThread(this, mWorkHandler, mMainHandler, SEAT_FPD_NAME)
        rsdConnectThread = PaxBleConnectThread(this, mWorkHandler, mMainHandler, SEAT_RSD_NAME)

        val filter = IntentFilter()
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        registerReceiver(mBleReceiver, filter)
    }

    /**
     * 1、服务被启动
     * 2、用户发生变化
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.run {
            val display = getStringExtra(PaxBleConfig.EXT_USER_DISPLAY)
            val uid = getIntExtra(PaxBleConfig.EXT_USER_ID, -1)
            val ffid = getStringExtra(PaxBleConfig.EXT_USER_FFID)
            showLog("onStartCommand  :$display , $uid , $ffid")
            if (SEAT_FPD_NAME == display) {
                fpdConnectThread.updateUserInfo(uid, ffid)
            } else if (SEAT_RSD_NAME == display) {
                rsdConnectThread.updateUserInfo(uid, ffid)
            }else{
                rsdConnectThread.updateUserInfo(PaxBleConfig.BleDebugUserID, PaxBleConfig.BleDebugFFID)
            }
        }

        tryStartScan()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        unregisterReceiver(mBleReceiver)

        tryStopScanAndConnect()
        super.onDestroy()
    }

    private val mBleReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val bleState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0)

            showTip("onReceive bleState :$bleState")
            when (bleState) {
                BluetoothAdapter.STATE_TURNING_ON -> {
                }
                BluetoothAdapter.STATE_ON -> {
                    showTip("tryStartScan by ble on")
                    tryStartScan()
                }
                BluetoothAdapter.STATE_TURNING_OFF -> {
                }
                BluetoothAdapter.STATE_OFF -> {
                    showTip("tryStopScan by ble off")
                    tryStopScanAndConnect()
                }
            }
        }
    }

    /**
     * 尝试开始BLE扫描
     * 1、服务被启动
     * 2、蓝牙被打开
     * 3、用户发生变化
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
     * 尝试停止BLE扫描以及连接
     * 1、服务被关闭
     * 2、蓝牙被关闭
     */
    private fun tryStopScanAndConnect() {
        doStopScan()

        showLog("doDisconnect by stop scan")
        fpdConnectThread.doDisconnect()
        rsdConnectThread.doDisconnect()
    }

    /**
     * 构建过滤条件
     */
    fun buildScanFilters(): List<ScanFilter> {
        val list = mutableListOf<ScanFilter>()
        fpdConnectThread.buildScanFilter()?.also {
            list.add(it)
        }
        rsdConnectThread.buildScanFilter()?.also {
            list.add(it)
        }
        return list
    }

    /**
     * 构建过滤设置
     */
    fun buildScanSettings(): ScanSettings {
        return ScanSettings.Builder()
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
    }

}

