package com.sunny.module.ble.client

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.sunny.module.ble.BleBaseService
import com.sunny.module.ble.BleConfig.PAX_BLE_UUID
import java.io.OutputStream

/**
 * 传统蓝牙
 * https://developer.android.com/guide/topics/connectivity/bluetooth?hl=zh-cn#FindDevices
 */
class BleClientClassic : BleBaseService() {

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

    /*
    connect
     */
    private var mBluetoothDevice: BluetoothDevice? = null
    private var mBluetoothSocket: BluetoothSocket? = null
    private var mOutputStream: OutputStream? = null

    override fun doStartConnect(address: String): Boolean {

        doStopConnect()

        return try {
            mBluetoothDevice = bluetoothAdapter?.getRemoteDevice(address)
            log("getRemoteDevice :$mBluetoothDevice")

//        mBluetoothSocket = mBluetoothDevice?.createInsecureRfcommSocketToServiceRecord(PAX_BLE_UUID)
            mBluetoothSocket = mBluetoothDevice?.createRfcommSocketToServiceRecord(PAX_BLE_UUID)
            mBluetoothSocket?.connect()
            mOutputStream = mBluetoothSocket?.outputStream
            true
        } catch (e: Exception) {
            e.printStackTrace()
            log("doStartConnect($address) error :$e")
            false
        }
    }

    override fun doStopConnect() {
        try {
            mOutputStream?.close()
            mOutputStream = null
        } catch (e: Exception) {
            e.printStackTrace()
            log("mOutputStream close error :$e")
        }

        try {
            mBluetoothSocket?.takeIf { it.isConnected }?.close()
            mBluetoothSocket = null
        } catch (e: Exception) {
            e.printStackTrace()
            log("mBluetoothSocket close error :$e")
        }
    }

    override fun doSendMsg(msg: String): Boolean {
        return if (mOutputStream != null) {
            try {
                mOutputStream?.write(msg.toByteArray())
                showTipInfo("trySendMsg succeed")
                true
            } catch (e: Exception) {
                e.printStackTrace()
                showTipInfo("trySendMsg error :$e")
                false
            }
        } else {
            showTipInfo("mOutputStream is null")
            false
        }
    }

}