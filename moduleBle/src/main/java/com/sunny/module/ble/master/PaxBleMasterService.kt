package com.sunny.module.ble.master

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult

/**
 * 主设备 启动后开始扫描，发现目标获取广播信息
 *
 * 车机上的PaxLauncher 不停扫描 FF Ctrl 设备
 */
internal class PaxBleMasterService : PaxBleMasterBaseService() {

    private var mmBluetoothLeScanner: BluetoothLeScanner? = null

    override fun doStartScan() {
        showLog("dealStartScan")
        // 先停止正在进行的扫描
        doStopScan()

        // 是否用用户登录
        if (!bleConnectHelper.hasUserLogin()) {
            showLog("No user login")
            return
        }

        mmBluetoothLeScanner = BluetoothAdapter.getDefaultAdapter()?.bluetoothLeScanner?.also {
            it.startScan(null, buildScanSettings(), mmScanCallback)
            showLog("startScan")
        }
    }

    override fun doStopScan() {
        showLog("dealStopScan")
        try {
            mmBluetoothLeScanner?.stopScan(mmScanCallback)
            mmBluetoothLeScanner = null
        } catch (e: Exception) {
            showLog("stopScan error :$e")
        }
    }

    /**
     * 扫描结果回调
     */
    private val mmScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            mWorkHandler.post {
                bleConnectHelper.dealScanResult(result)
            }
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
        }

        override fun onScanFailed(errorCode: Int) {
            showLog("onScanFailed :$errorCode")

            // 扫描失败，5s后再次开始扫描
            mMainHandler.postDelayed({
                showLog("doStartScan dealy by onScanFailed($errorCode)")
                doStartScan()
            }, 5_000)
        }
    }
}
