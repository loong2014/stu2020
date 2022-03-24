package com.sunny.module.ble.client.ble

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import com.sunny.module.ble.BleConfig
import com.sunny.module.ble.client.BleBaseClientService
import com.sunny.module.ble.client.ClientConnectCallback
import com.sunny.module.ble.client.ClientConnectThread
import com.sunny.module.ble.client.buildConnectThreadBle

/**
 * 低功耗蓝牙
 * https://developer.android.com/guide/topics/connectivity/bluetooth-le?hl=zh-cn
 *
 * 外围
 */
class BleClientBle : BleBaseClientService() {


    private var mScanning: Boolean = false

    override fun doRelease() {
        doStopScan()
        doStopConnect()
    }

    override fun doInit() {
        doStartScan()
    }

    override fun doStartScan() {
        log("doStartScan($mScanning)")
        val filters = mutableListOf<ScanFilter>()
//        filters.add(
//            ScanFilter.Builder()
//                .setServiceUuid(BleConfig.PAX_BLE_P_UUID)
//                .build()
//        )

        val setting = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
            .build()
//
//        if (mScanning) {
//            mHandler.removeCallbacks(delayStopTask)
//        }
//        mHandler.postDelayed(delayStopTask, SCAN_PERIOD)
        mScanning = true

        mDeviceAllSet.clear()
        mDeviceSet.clear()
        bluetoothLeScanner?.startScan(filters, setting, mScanCallback)
        showTipInfo("startScan")
    }

    override fun doStopScan() {
        log("doStopScan($mScanning)")
        mScanning = false
        mWorkHandler.removeCallbacks(delayStopTask)
        bluetoothLeScanner?.stopScan(mScanCallback)
        showTipInfo("stopScan")

        logScanResults()
    }

    private val delayStopTask = Runnable {
        doStopScan()
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
            dealRcvMsg("onScanFailed :$errorCode")
        }
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
                dealRcvMsg("${device.name}(${device.type})(${device.bondState})\n>>>${device.address} , ${device.uuids}")
                device.uuids?.forEachIndexed { index, uuid ->
                    showLog("${device.name}($index) >>> $uuid")
                }
                tryAutoConnect(device)
            }
        }
    }

    override fun doStartConnect(address: String): Boolean {
        return bluetoothAdapter?.getRemoteDevice(address)?.let { dev ->
            tryAutoConnect(dev)
        } ?: false
    }

    // 正在连接的服务端
    var mDeviceConnectThread: ClientConnectThread? = null

    val connectMap: MutableMap<String, ClientConnectThread?> = mutableMapOf()

    private fun tryAutoConnect(device: BluetoothDevice): Boolean {
        log("tryAutoConnect : $device")
        if (buildClientConnect(device)) {
            bleCallback?.onConnectStateChanged(
                BleConfig.ConnectStateTypeConnected,
                "(${device.type})${device.name} --- ${device.address}"
            )
        }
        return true
    }

    private fun buildClientConnect(device: BluetoothDevice): Boolean {
        if (connectMap.containsKey(device.address)) {
            showLog("$device 正在连接服务端")
            return true
        }

        val sb = StringBuilder()
        connectMap.entries.iterator().takeIf { it.hasNext() }?.let {
            val entry = it.next()
            if (entry.value == null) {
                it.remove()
            } else {
                sb.append("\n${entry.key} --> ${entry.value}")
            }
        }

        if (connectMap.size >= 1) {
            showLog("无法连接更多设备")
            showLog("已连接设备 :${sb}")
            return false
        }

        connectMap.keys.forEach {
            if (connectMap[it] == null) {
                connectMap.remove(it)
            }
        }

        val connectThread =
            buildConnectThreadBle(this, device, object : ClientConnectCallback {

                override fun onStateChanged(oldState: String, newState: String) {
                }

                override fun onReadMsg(msg: String) {
                }

                override fun onWriteState(isOk: Boolean, seq: Int) {
                }

            })
        connectThread.start()
        mDeviceConnectThread = connectThread

        connectMap[device.address] = connectThread
        return true
    }

    override fun doStopConnect() {
        log("doStopConnect : ${connectMap.size}")
        connectMap.values.forEach {
            it?.doCancel()
        }
    }

    var msgSeq: Int = 0
    override fun doSendMsg(msg: String): Boolean {
        return true
//        return mDeviceConnectThread?.write(msg, (msgSeq++)) ?: false
    }


}