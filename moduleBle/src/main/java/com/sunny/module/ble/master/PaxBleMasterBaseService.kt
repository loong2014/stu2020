package com.sunny.module.ble.master

import android.bluetooth.BluetoothAdapter
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
import com.sunny.module.ble.PaxBleCommonService
import com.sunny.module.ble.PaxBleConfig
import com.sunny.module.ble.master.helper.PaxBleConnectHelper

/**
 * 主设备 启动后开始扫描，发现目标获取广播信息
 *
 * 车机上的PaxLauncher 不停扫描 FF Ctrl 设备
 */
internal abstract class PaxBleMasterBaseService : PaxBleCommonService() {

    // 是否支持BLE
    var isSupportBle = false

    lateinit var bleConnectHelper: PaxBleConnectHelper

    override fun onCreate() {
        super.onCreate()

        val ht = HandlerThread("pax_ble")
        ht.start()
        mWorkHandler = Handler(ht.looper)

        mMainHandler = Handler(Looper.getMainLooper())

        bleConnectHelper = PaxBleConnectHelper(this, mWorkHandler, mMainHandler)

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
            bleConnectHelper.updateUserInfo(display, uid, ffid)
        }

        mMainHandler.post {
            showLog("tryStartScan by onStartCommand")
            tryStartScan()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        mMainHandler.post {
            showLog("tryStartScan by onBind")
            tryStartScan()
        }
        return super.onBind(intent)
    }

    override fun onDestroy() {
        unregisterReceiver(mBleReceiver)

        showLog("tryStopScanAndConnect by onDestroy")
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
                    mMainHandler.post {
                        showTip("tryStartScan by ble ON")
                        tryStartScan()
                    }
                    mMainHandler.postDelayed({
                        showTip("tryStartScan delay by ble ON")
                        tryStartScan()
                    }, 500)
                }
                BluetoothAdapter.STATE_TURNING_OFF -> {
                }
                BluetoothAdapter.STATE_OFF -> {
                    mMainHandler.post {
                        showLog("tryStopScanAndConnect by ble OFF")
                        /*
                        因为发现是蓝牙适配器的重量级过程，所以在尝试使用 BluetoothSocket.connect() 连接到远程设备之前，应始终调用此方法。
                         Discovery 不是由 Activity 管理的，而是作为系统服务运行的，因此应用程序应该始终调用取消发现，即使它没有直接请求发现，只是为了确定。
                         */
                        tryStopScanAndConnect()
                    }
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

        bleConnectHelper.dealStopConnect()
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

