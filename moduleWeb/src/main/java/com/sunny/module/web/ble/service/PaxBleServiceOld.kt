package com.sunny.module.web.ble.service

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.sunny.module.web.ble.BleTools
import com.sunny.module.web.ble.PaxBleServiceBase

/**
 * 传统蓝牙
 * https://developer.android.com/guide/topics/connectivity/bluetooth?hl=zh-cn#FindDevices
 */
class PaxBleServiceOld : PaxBleServiceBase() {

    private var mScanning: Boolean = false

    override fun doInit() {
        // Register for broadcasts when a device is discovered.
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)
    }

    override fun doRelease() {
        super.doRelease()
        unregisterReceiver(receiver)
    }

    override fun doStartScan() {
        log("doStartScan($mScanning)")
        if (mScanning) {
            mHandler.removeCallbacks(delayStopTask)
        }
        mHandler.postDelayed(delayStopTask, SCAN_PERIOD)
        mScanning = true

        mDeviceAllSet.clear()
        mDeviceSet.clear()
        val isOk = bluetoothAdapter?.startDiscovery()
        showTipInfo("startDiscovery($isOk)")
    }

    override fun doStopScan() {
        log("doStopScan($mScanning)")
        mScanning = false
        mHandler.removeCallbacks(delayStopTask)
        val isOk = bluetoothAdapter?.cancelDiscovery()
        showTipInfo("cancelDiscovery($isOk)")

        logScanResults()
    }

    private val delayStopTask = Runnable {
        doStopScan()
    }

    private fun dealFoundOneDevice(device: BluetoothDevice) {
        if (mDeviceAllSet.add(device)) {
            BleTools.buildOneDeviceShowMsg(
                device, "FoundNewDevice allCount(${mDeviceAllSet.size})"
            )
        }
        device.takeIf { it.name != null }?.let {
            if (mDeviceSet.add(it)) {
                logScanResults()
            }
        }
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
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
}