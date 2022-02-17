package com.sunny.module.ble.client

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.sunny.module.ble.BleConfig

/**
 * 传统蓝牙
 * https://developer.android.com/guide/topics/connectivity/bluetooth?hl=zh-cn#FindDevices
 */
class BleClientBle : BleBaseClientService() {

    private var mScanning: Boolean = false

    override fun doInit() {
        // Register for broadcasts when a device is discovered.
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)

        //
        doStartScan()
    }

    override fun doRelease() {
        super.doRelease()
        unregisterReceiver(receiver)
    }

    private fun dealRcvMsg(msg: String) {
        showLog("dealRcvMsg msg :$msg")
        bleCallback?.onRcvClientMsg(msg)
    }

    fun sendServerState(tip: String) {
        bleCallback?.onConnectStateChanged(1, tip)
        showLog(tip)
    }

    private fun showLog(msg: String) {
        BleConfig.bleLog("dddddd :$msg")
    }

    override fun doStartScan() {
        showLog("doStartScan($mScanning)")
        if (mScanning) {
            mHandler.removeCallbacks(delayStopTask)
        }
        mHandler.postDelayed(delayStopTask, SCAN_PERIOD)
        mScanning = true

        mDeviceAllSet.clear()
        mDeviceSet.clear()
        val isOk = bluetoothAdapter?.startDiscovery()
        sendServerState("开始扫描附近设备($isOk)")
    }

    override fun doStopScan() {
        showLog("doStopScan($mScanning)")
        mScanning = false
        mHandler.removeCallbacks(delayStopTask)
        val isOk = bluetoothAdapter?.cancelDiscovery()
        sendServerState("停止扫描附近设备($isOk)")

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
                dealRcvMsg("${device.name}(${device.type})(${device.bondState})\n>>>${device.address} , ${device.uuids}")
                device.uuids?.forEachIndexed { index, uuid ->
                    showLog("${device.name}($index) >>> $uuid")
                    mHandler.post {
                        tryAutoConnect(device)
                    }
                }
            }
        }
    }


    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // 发现设备
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    if (device != null) {
                        dealFoundOneDevice(device)
                    }
                }

                BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    when (device?.bondState) {
                        BluetoothDevice.BOND_BONDING -> {
                            showTipInfo("正在与 ${device.name} 进行配对")
                        }
                        BluetoothDevice.BOND_BONDED -> {
                            showTipInfo("与 ${device.name} 的配对完成")
                        }

                        BluetoothDevice.BOND_NONE -> {
                            showTipInfo("取消与 ${device.name} 的配对")
                        }
                    }
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    showTipInfo("扫描完成")
                    doStopScan()
                }
            }
        }
    }

    // 正在连接的服务端
    var mDeviceConnectThread: ClientConnectThread? = null

    val connectMap: MutableMap<String, ClientConnectThread?> = mutableMapOf()

    private fun tryAutoConnect(device: BluetoothDevice) {
        if (device.name == "MI 6") {
            if (buildClientConnect(device)) {
                bleCallback?.onConnectStateChanged(
                    BleConfig.ConnectStateTypeConnected,
                    "设备连接成功:${device.name}"
                )
            }
        }
    }

    override fun doStartConnect(address: String): Boolean {
        return bluetoothAdapter?.getRemoteDevice(address)?.let { dev ->
            buildClientConnect(dev)
        } ?: false
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

        if (connectMap.size >= 2) {
            showLog("无法连接更多设备")
            showLog("已连接设备 :${sb}")
            return false
        }

        connectMap.keys.forEach {
            if (connectMap[it] == null) {
                connectMap.remove(it)
            }
        }


        val connectThread: ClientConnectThread =
            buildConnectThread(device, object : ClientConnectCallback {

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
        mDeviceConnectThread?.cancel()
    }

    var msgSeq: Int = 0
    override fun doSendMsg(msg: String): Boolean {
        return mDeviceConnectThread?.write(msg, (msgSeq++)) ?: false
    }


}