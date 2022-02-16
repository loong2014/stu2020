package com.sunny.module.web.ble.service

import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import com.sunny.module.web.ble.BleConfig
import com.sunny.module.web.ble.PaxBleServiceBase

/**
 * 低功耗蓝牙
 * https://developer.android.com/guide/topics/connectivity/bluetooth-le?hl=zh-cn
 *
 * macbook : ac:bc:32:97:bc:38
 * Redmi K40 : 9C:5A:81:2F:19:39
 * FF 91 Driver : 22:22:ed:16:c2:52
 */
class PaxBleService : PaxBleServiceBase() {
    private var mScanning: Boolean = false

    override fun tryStartBleScan(restart: Boolean): Boolean {
        log("tryStartBleScan  isSupportBle($isSupportBle)")
        if (isSupportBle) {
            startLeScan()
            return true
        }
        return false
    }

    override fun tryStopBleScan(): Boolean {
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

    /**
     * 开始查找 BLE 设备
     * 注意：您仅能扫描蓝牙 LE 设备或传统蓝牙设备，正如蓝牙概览中所述。您无法同时扫描蓝牙 LE 设备和传统蓝牙设备。
     */
    private fun startLeScan() {
        log("startLeScan($mScanning)")

        val filters = mutableListOf<ScanFilter>()
        BleConfig.curBleTarget.let {
            filters.add(
                ScanFilter.Builder()
                    .setDeviceName(it.first)
                    .build()
            )
        }

        val setting = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
            .build()


        if (mScanning) {
            mHandler.removeCallbacks(delayStopTask)
        }
        mHandler.postDelayed(delayStopTask, SCAN_PERIOD)

        mScanning = true
        mDeviceSet.clear()
        bluetoothLeScanner?.startScan(filters, setting, mScanCallback)
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
        mScanning = false
        mHandler.removeCallbacks(delayStopTask)
        logScanResults()
        bluetoothLeScanner?.stopScan(mScanCallback)
    }
}