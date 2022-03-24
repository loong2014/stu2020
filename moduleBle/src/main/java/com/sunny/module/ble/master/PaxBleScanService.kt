package com.sunny.module.ble.master

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import com.sunny.module.ble.PaxBleConfig

/**
 * 主设备 启动后开始扫描，发现目标获取广播信息
 *
 * 车机上的PaxLauncher 不停扫描 FF Ctrl 设备
 */
class PaxBleScanService : PaxBleMasterService() {

    private var mmBluetoothLeScanner: BluetoothLeScanner? = null

    // 是否正在扫描
    private var mmScanning = false

    override fun doInit() {
    }

    override fun doRelease() {
        doStopScan()
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
    }

    override fun doStopScan() {
        showLog("doStopScan($mmScanning)")
        mmBluetoothLeScanner?.stopScan(mmScanCallback)
        mmBluetoothLeScanner = null
        mmScanning = false
    }

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

        val settings = PaxBleConfig.buildScanSettings()

        mmBluetoothLeScanner = BluetoothAdapter.getDefaultAdapter()?.bluetoothLeScanner?.also {
            it.startScan(filter, settings, mmScanCallback)
            showLog("startScan")
            mmScanning = true
        }
    }

    /**
     * 延时开始扫描
     */
    private fun delayStartScan() {
        showLog("delayStartScan($mmScanning)")
        mMainHandler.postDelayed({
            showLog("doStartScan by delay")
            doStartScan()
        }, 5_000)
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
            delayStartScan()
        }
    }

    var curConnectDevice: BluetoothDevice? = null

    /**
     * 处理扫描到的设备
     */
    private fun dealScanResult(result: ScanResult?) {
        if (result == null) return

        if (curConnectDevice != null) return

        if (result.device?.name != null) {
            showLog("scanResult :${result.device?.name} , ${result.device} , rssi(${result.rssi}) , isConnectable(${result.isConnectable})")
        }

        if (inConnecting()) {
            showLog("Wait BLE Connected")
            return
        }

        // 尝试后排连接
        if (rsdConnectThread.dealConnected(result)) {
            curConnectDevice = result.device
            return
        }

//        // 尝试前排连接
//        if (fpdConnectThread.dealConnected(result)) {
//            return
//        }
    }

    /**
     * 是否有BLE设备正在连接
     */
    private fun inConnecting(): Boolean {
        return rsdConnectThread.inConnecting() || fpdConnectThread.inConnecting()
    }
}
