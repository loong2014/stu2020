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

    // 是否正在扫描
    private var mmScanning = false

    override fun doStartScan() {
        showLog("doStartScan($mmScanning)")
        // 先停止正在进行的扫描
        if (mmScanning) {
            doStopScan()
        }

        val filter = buildScanFilters()
        if (filter.isNullOrEmpty()) {
            showLog("No UUID For Filter")
            return
        }

        mmBluetoothLeScanner = BluetoothAdapter.getDefaultAdapter()?.bluetoothLeScanner?.also {
            it.startScan(filter, buildScanSettings(), mmScanCallback)
            showLog("startScan")
            mmScanning = true
        }
    }

    override fun doStopScan() {
        showLog("doStopScan($mmScanning)")
        mmBluetoothLeScanner?.stopScan(mmScanCallback)
        mmBluetoothLeScanner = null
        mmScanning = false
    }

    /**
     * 扫描结果回调
     */
    private val mmScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            if (result == null) return
            mWorkHandler.post {
                dealScanResult(result)
            }
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
        }

        override fun onScanFailed(errorCode: Int) {
            showLog("onScanFailed :$errorCode")

            // 扫描失败，5s后再次开始扫描
            mMainHandler.postDelayed({
                showLog("doStartScan by delay")
                doStartScan()
            }, 5_000)
        }
    }

    /**
     * 处理扫描到的设备
     */
    private fun dealScanResult(result: ScanResult?) {
        if (result == null) return

//        showLog("${result.device?.name} , ${result.device} , rssi(${result.rssi}) , isConnectable(${result.isConnectable})")

        if (inConnecting()) {
            showLog("Wait BLE Connected")
            return
        }

        // 尝试后排连接
        if (rsdConnectThread.dealConnect(result)) {
            return
        }

        // 尝试前排连接
        if (fpdConnectThread.dealConnect(result)) {
            return
        }
    }

    /**
     * 是否有BLE设备正在连接，同一时刻，只能有一个设备处于正在连接状态
     */
    private fun inConnecting(): Boolean {
        return rsdConnectThread.inConnecting() || fpdConnectThread.inConnecting()
    }
}
