package com.sunny.module.ble.pax

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.os.ParcelUuid
import com.sunny.module.ble.server.BleBaseServerService

/**
 * 从设备/外围设备，启动后开始扫描，发现广播后进行连接
 *
 * 手机上的 FF Ctrl 启动后，主动连接车机，建立 socket 通道
 */
class BleSlaveService : BleBaseServerService() {

    override fun doInit() {
    }

    override fun doStartScan() {
        dealRcvMsg("startScan")
        bluetoothLeScanner?.startScan(buildScanFilter(), buildScanSettings(), scanCallback)
    }

    override fun doRelease() {
        doStopScan()
    }

    override fun doStopScan() {
        dealRcvMsg("stopScan")
        bluetoothLeScanner?.stopScan(scanCallback)
    }

    private fun buildScanSettings(): ScanSettings {
        return ScanSettings.Builder()
            .build()
    }

    private fun buildScanFilter(): List<ScanFilter> {
        val filters = mutableListOf<ScanFilter>()
        filters.add(
            ScanFilter.Builder()
                .setServiceUuid(ParcelUuid(PaxBleConfig.PAX_SERVICE_UUID))
                .build()
        )
//        filters.add(
//            ScanFilter.Builder()
//                .setDeviceAddress("22:22:ED:16:C2:52") // FF 91 Driver
//                .build()
//        )
//        filters.add(
//            ScanFilter.Builder()
//                .setDeviceAddress("9C:5A:81:2F:19:39") // Redmi K40
//                .build()
//        )
//        filters.add(
//            ScanFilter.Builder()
//                .setDeviceAddress("20:47:DA:9B:A2:73") // MI 6
//                .build()
//        )
        return filters
    }

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            val sb = StringBuilder()
            sb.append("onScanResult($callbackType)(${result?.rssi}) :")
            result?.device?.let {
                sb.append("${it.address}(${it.type}) --- ${it.name} --- ${it.bondState}")
            } ?: sb.append("null")
            log(sb.toString())

            dealScanResult(result)
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {

        }

        override fun onScanFailed(errorCode: Int) {
            dealRcvMsg("onScanFailed :$errorCode")
        }
    }

    private fun dealScanResult(result: ScanResult?) {
        result?.device?.let {
            if (mDeviceSet.add(it)) {
                // 首次发现设备，进行连接
                dealRcvMsg("${it.address}(${it.type}) --- ${it.name}")
                doConnected(result.device)
            }
        }
    }

    private var slaveConnectThread: SlaveConnectThread? = null
    private fun doConnected(device: BluetoothDevice) {
        log("doConnected($device) :$slaveConnectThread")
        if (slaveConnectThread != null) {
            slaveConnectThread?.doCancel()
            slaveConnectThread = null
        }

        slaveConnectThread = buildSlaveConnectThread(this, device, bleConnectCallback)
        slaveConnectThread?.start()
    }

    private val bleConnectCallback = object : BleConnectCallback {
        override fun showRcvTip(msg: String) {
            dealRcvMsg(msg)
        }
    }
}