//package com.sunny.module.ble.pax
//
//import android.bluetooth.BluetoothDevice
//import android.bluetooth.le.ScanCallback
//import android.bluetooth.le.ScanFilter
//import android.bluetooth.le.ScanResult
//import android.bluetooth.le.ScanSettings
//import android.os.ParcelUuid
//import com.sunny.module.ble.client.BleBaseClientService
//
///**
// * 从设备/外围设备，启动后开始扫描，发现广播后进行连接
// *
// * 手机上的 FF Ctrl 启动后，主动连接车机，建立 socket 通道
// */
//class BleSlaveServiceOld : BleBaseClientService() {
//
//    override fun doInit() {
//    }
//
//    override fun doStartScan() {
//        dealRcvMsg("startScan")
//        bluetoothLeScanner?.startScan(buildScanFilter(), buildScanSettings(), scanCallback)
//    }
//
//    override fun doRelease() {
//        doStopScan()
//    }
//
//    override fun doStopScan() {
//        dealRcvMsg("stopScan")
//        bluetoothLeScanner?.stopScan(scanCallback)
//    }
//
//    private fun buildScanSettings(): ScanSettings {
//        return ScanSettings.Builder()
//            .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
//            .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
//            .setMatchMode(ScanSettings.MATCH_MODE_STICKY)
//            .build()
//    }
//
//    private fun buildScanFilter(): List<ScanFilter> {
//        val filters = mutableListOf<ScanFilter>()
//        filters.add(
//            ScanFilter.Builder()
//                .setServiceUuid(ParcelUuid(PaxBleConfig.PAX_SERVICE_UUID))
//                .build()
//        )
//        return filters
//    }
//
//    private val scanCallback = object : ScanCallback() {
//        override fun onScanResult(callbackType: Int, result: ScanResult?) {
//            if (result == null) return
//            mMainHandler.post {
//                dealScanResult(result)
//            }
//        }
//
//        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
//        }
//
//        override fun onScanFailed(errorCode: Int) {
//            dealRcvMsg("onScanFailed :$errorCode")
//        }
//    }
//
//    private fun dealScanResult(result: ScanResult?) {
//        if (result == null) return
//
//        showLog("${result.device} , rssi(${result.rssi}) , bondState(${result.device.bondState}) , isConnectable(${result.isConnectable})")
//        if (!mDeviceSet.add(result.device)) {
//            return
//        }
//
//        //解析蓝牙广播数据报文
//        var isFFDevice = false
//        var devFfid = ""
//        result.scanRecord?.serviceData?.forEach { (parcelUuid, value) ->
//            if (PaxBleConfig.PAX_SERVICE_UUID_FFID == parcelUuid.uuid) {
//                isFFDevice = true
//                devFfid = String(value)
//                return@forEach
//            }
//        }
//
//        dealRcvMsg(
//            "${result.device}(${result.device.type})" +
//                    " --- ${result.device.name}" +
//                    " --- isFF91Device($isFFDevice)" +
//                    " --- ffid($devFfid)"
//        )
//
//        // FF 设备
//        if (isFFDevice) {
//            doConnected(result.device)
//        }
//    }
//
//    private var slaveConnectThread: SlaveConnectThread? = null
//    private fun doConnected(device: BluetoothDevice) {
//        log("doConnected($device) :$slaveConnectThread")
//        if (slaveConnectThread != null) {
//            slaveConnectThread?.doCancel()
//            slaveConnectThread = null
//        }
//
//        slaveConnectThread = buildSlaveConnectThread(this, device, bleConnectCallback)
//        slaveConnectThread?.start()
//    }
//
//    private val bleConnectCallback = object : BleConnectCallback {
//        override fun showRcvTip(msg: String) {
//            dealRcvMsg(msg)
//        }
//    }
//
//    var doWriteOpt = true
//    override fun doSendMsg(msg: String): Boolean {
//        if (doWriteOpt) {
//            doWriteOpt = false
//            dealRcvMsg("write:$msg")
//            slaveConnectThread?.doWriteData(msg)
//        } else {
//            doWriteOpt = true
//            dealRcvMsg("read:${slaveConnectThread?.doReadData()}")
//        }
//
//        return true
//    }
//}