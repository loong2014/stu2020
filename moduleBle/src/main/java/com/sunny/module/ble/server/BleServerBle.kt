package com.sunny.module.ble.server

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import com.sunny.module.ble.BleConfig

/**
 * 低功耗蓝牙
 * https://developer.android.com/guide/topics/connectivity/bluetooth-le?hl=zh-cn
 */
class BleServerBle : BleBaseServerService() {

    private var mScanning: Boolean = false

    override fun doRelease() {
        super.doRelease()
        doStopScan()
    }

    override fun doStartScan() {
        log("doStartScan($mScanning)")

        val filters = mutableListOf<ScanFilter>()
        filters.add(
            ScanFilter.Builder()
                .setServiceUuid(BleConfig.PAX_BLE_P_UUID)
                .build()
        )

        val setting = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
            .build()

        if (mScanning) {
            mHandler.removeCallbacks(delayStopTask)
        }
        mHandler.postDelayed(delayStopTask, SCAN_PERIOD)
        mScanning = true

        mDeviceAllSet.clear()
        mDeviceSet.clear()
        bluetoothLeScanner?.startScan(filters, setting, mScanCallback)
        showTipInfo("startScan")
    }

    override fun doStopScan() {
        log("doStopScan($mScanning)")
        mScanning = false
        mHandler.removeCallbacks(delayStopTask)
        bluetoothLeScanner?.stopScan(mScanCallback)
        showTipInfo("stopScan")

        logScanResults()
    }

    private val delayStopTask = Runnable {
        doStopScan()
    }


    fun dealFoundOneDevice(device: BluetoothDevice) {
        if (mDeviceAllSet.add(device)) {
            log(
                "FoundNewDevice allCount(${mDeviceAllSet.size}) -> " +
                        "$device(${device.type}) , ${device.name} , ${device.uuids}"
            )
        }
        device.takeIf { it.name != null }?.let {
            if (mDeviceSet.add(it)) {
                logScanResults()
                BleConfig.updateCurDiscoveryDevices(mDeviceSet)
            }
        }
    }

    private val mScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            result?.device?.let {
                dealFoundOneDevice(it)
            }
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
        }

        override fun onScanFailed(errorCode: Int) {
            showTipInfo("onScanFailed :$errorCode")
        }
    }

}