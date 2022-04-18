package com.sunny.module.ble

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.TextView
import com.sunny.lib.common.base.BaseActivity
import com.sunny.module.ble.master.PaxBleMasterService
import com.sunny.module.ble.slave.PaxBleSlaveService
import com.sunny.module.ble.utils.StringLrcCircle
import kotlinx.android.synthetic.main.ble_activity_demo.*
import java.text.SimpleDateFormat
import java.util.*

open class BleDemoBaseActivity : BaseActivity() {

    private val tipList = StringLrcCircle(10)

    private var isClientSlaveDemo = true // 是否是客户端/Slave外设
    var isClassic = true // 是否是经典蓝牙

    var isSupportBle = false
    var isBind = false
    private var curBleControl: IBleClientInterface? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            curBleControl = IBleClientInterface.Stub.asInterface(service)
            curBleControl?.registerCallBack(bleCallback)
            curBleControl?.startScan()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
        }
    }

    private val bleCallback = object : IBleCallbackInterface.Stub() {
        override fun onRcvClientMsg(msg: String?) {
            showRcvMsg(msg ?: "Null")
        }

        override fun onRcvServiceMsg(msg: String?) {
            showRcvMsg(msg ?: "Null")
        }

        override fun onConnectStateChanged(type: Int, tip: String?) {
            when (type) {
                BleConfig.ConnectStateTypeConnected -> {
                    showDeviceTip(tip ?: "Null")
                }
            }
        }
    }

    fun getBleControl(): IBleClientInterface? {
        return if (isSupportBle && isBind) {
            curBleControl
        } else {
            null
        }
    }

    fun showTip(tip: String) {
        runOnUiThread {
            ble_tip.text = tip
        }
    }

    private fun showTipList() {
        runOnUiThread {
            val sb = StringBuilder()
            tipList.toList().forEach {
                sb.append("\n$it")
            }
            showTip(sb.toString())
        }
    }

    private fun clearTip() {
        tipList.clear()
        showTip("None")
    }

    val format = SimpleDateFormat("mm:ss SSS")
    fun showRcvMsg(msg: String) {
        val tip = "${format.format(Date())} >>> $msg"
        tipList.add(tip)
        showTipList()
    }

    fun showCheckMsg(msg: String) {
        val tip = "Check >>> $msg"
        BleConfig.bleLog(msg)
        tipList.add(tip)
        showTipList()
    }

    private fun showServerTip(tip: String) {
        val msg = "Server >>> $tip"
        ble_server_tip.text = msg
        BleConfig.bleLog(msg)
    }

    fun showConnectTip(tip: String) {
        val msg = "Connect >>> $tip"
        ble_connect_tip.text = msg
        BleConfig.bleLog(msg)
    }

    fun showDeviceTip(tip: String) {
        val msg = "ConnectedDevice >>> $tip"
        tv_target_device_info.text = msg
        BleConfig.bleLog(msg)
    }

    private fun showTargetDevice() {
        val msg = BleConfig.curTargetDevice?.run {
            "目标设备(${type}) >>> $name --- $address"
        } ?: "目标设备:Null"
        tv_target_device_info.text = msg
        BleConfig.bleLog(msg)
    }

    fun showDeviceStateTip(tip: String) {
        val msg = "State >>> $tip"
        tv_target_device_info.text = msg
        BleConfig.bleLog(msg)
    }

    fun doSendMsg(info: String) {
//        BleConfig.doParseMeetingJson(this)

        getBleControl()?.sendMsg(info)

//        val msg = "Send >>> $info"
//        BleConfig.bleLog(msg)
//        tipList.add(msg)
//        showTipList()
    }

    fun doReadMsg() {
        val msg = getBleControl()?.readMsg() ?: ""
        tipList.add(msg)
        showTipList()
    }

    open fun resetService() {
        tipList.clear()
        ble_config_layout.visibility = View.GONE

        doUnbindService()

        BleConfig.resetCurTargetDevice()

        showTargetDevice()

        showTip("resetService")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ble_activity_demo)

        initView()

        //
        updateDemoType()

        //
        updateServiceType()

        //
        doDeviceCheck()
    }

    private fun doDeviceCheck() {
        isSupportBle = false
        showCheckMsg("start")

        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

        val adapter = BluetoothAdapter.getDefaultAdapter()
        if (adapter == null) {
            showCheckMsg("不支持蓝牙")
            return
        }

        if (!adapter.isEnabled) {
            showCheckMsg("蓝牙未开启")
            return
        }
        showCheckMsg("蓝牙已开启")

        // 蓝牙3.0规范之前的版本称为传统蓝牙，蓝牙4.0规范之后的版本称为低功耗蓝牙
        // Android 从4.3版本(API Level 18)开始支持BLE通信。


        showCheckMsg("isLeExtendedAdvertisingSupported :${adapter.isLeExtendedAdvertisingSupported}")
        showCheckMsg("isMultipleAdvertisementSupported :${adapter.isMultipleAdvertisementSupported}")

        // 通告
        // Android 8.0 Oreo（API LEVEL 26）开始在BLE Manager里增加了5.0BLE相关方法，可以使用下面三个方法来鉴定你的手机对5.0BLE支持到什么程度。
        // PHY，物理层，需要硬件支持
        // 调制速率1Mbit/s和2Mbps
        showCheckMsg("PHY是否支持2Mbps:${adapter.isLe2MPhySupported}")
        /*
        调制方式
        1Mbps支持两种调制方式：
            LE Uncode PHY：信息数据不编码，传输速率就为 1 Mb/s
            LE Coded PHY：信息数据编码，传输速率为 125 kb/s或者500 kb/s
        2Mbps支持单一方式
            LE 2MPHY：信息数据不编码，传输速率就为 2 Mb/s
         */
        showCheckMsg("1Mbps下是否支持编解码 :${adapter.isLeCodedPhySupported}")

        /*
        广播
        单向广播
            未建立连接时 the advertising procedure
        周期广播
        advertising or scanning device

         */
        showCheckMsg("是否支持周期广播 :${adapter.isLePeriodicAdvertisingSupported}")
        showCheckMsg("isLePeriodicAdvertisingSupported :${adapter.isLePeriodicAdvertisingSupported}")
        showCheckMsg("isOffloadedFilteringSupported :${adapter.isOffloadedFilteringSupported}")
        showCheckMsg("isOffloadedScanBatchingSupported :${adapter.isOffloadedScanBatchingSupported}")

        val msg = "当前设备:${adapter.name} , ${adapter.address} "
        tv_own_device_info.text = msg

        if (!BleTools.checkBlePermission(this, 100)) {
            showCheckMsg("permission error")
            return
        }

        if (!BleTools.isSupportBLE(this)) {
            showCheckMsg("不支持低功耗")
        }

        if (adapter.isDiscovering) {
            showCheckMsg("设备正在扫描")
        }

        if (adapter.cancelDiscovery()) {
            showCheckMsg("关闭发现")
        }

        BleTools.openDiscoverable(this)

        isSupportBle = true
        showCheckMsg("可以正常使用")
    }

    private fun updateDemoType() {
        isClientSlaveDemo = !isClientSlaveDemo

        if (isClientSlaveDemo) {
            top_bar.setMiddleName("蓝牙-手机/Slave")
            et_msg.setText("client msg")
            ble_client_config_layout.visibility = View.VISIBLE
        } else {
            top_bar.setMiddleName("蓝牙-车机/Master")
            et_msg.setText("service msg")
            ble_client_config_layout.visibility = View.GONE
        }
    }

    /**
     * [BluetoothDevice.getType]
     * 1==经典
     * 2==低能耗
     * 3==双模式
     */
    private fun updateServiceType() {
        isClassic = !isClassic
        btn_ble_service_type.text = if (isClassic) {
            "服务类型(BT)"
        } else {
            "服务类型(BLE)"
        }
    }

    open fun initView() {
        top_bar.setMidNameClickListener {
            updateDemoType()

            resetService()
        }

        top_bar.setOnBackBtnClickListener {
            doExitActivity()
        }

        //
        ble_config_layout.visibility = View.GONE
        btn_hide_show_config.text = "配置界面(隐藏)"
        btn_hide_show_config.setOnClickListener {
            (it as TextView).text = if (ble_config_layout.visibility == View.VISIBLE) {
                ble_config_layout.visibility = View.GONE
                "配置界面(隐藏)"
            } else {
                ble_config_layout.visibility = View.VISIBLE
                "配置界面(显示)"
            }
        }

        // https://www.zhihu.com/question/48722431
        btn_ble_service_type.setOnClickListener {
            updateServiceType()

            resetService()
        }

        //
        btn_ble_paired_show.setOnClickListener {
            val msg = BleTools.getBondedDevices()?.takeIf {
                it.isNotEmpty()
            }?.let {
                BleTools.buildDevicesShowMsg(it, "showPairedDevices")
            } ?: "No Paired Devices"

            showTip(msg)
        }

        BleConfig.resetCurTargetDevice()
        btn_ble_target.setOnClickListener {
            BleConfig.updateTargetDevice()
            showTargetDevice()
        }

        //
        btn_ble_device_check.setOnClickListener {
            doDeviceCheck()
        }

        //
        btn_ble_service_start.setOnClickListener {
            showServerTip(doBindService())
        }

        //
        btn_ble_service_stop.setOnClickListener {
            showServerTip(doUnbindService())
        }

        //
        btn_ble_msg_clear.setOnClickListener {
            clearTip()
        }
        //
        btn_ble_msg_send.setOnClickListener {
            val msg: String = et_msg.text?.toString() ?: "None"
//            val tip = "${System.currentTimeMillis()}$msg"
            doSendMsg(msg)
        }
        //
        btn_ble_msg_read.setOnClickListener {
            doReadMsg()
        }
    }

    private fun doBindService(): String {
        if (isClientSlaveDemo) {
            isBind = bindService(
                Intent(this, PaxBleSlaveService::class.java),
                serviceConnection,
                Context.BIND_AUTO_CREATE
            )
        } else {
            isBind = bindService(
//                Intent(this, PaxBleScanService::class.java),
                Intent(this, PaxBleMasterService::class.java),
                serviceConnection,
                Context.BIND_AUTO_CREATE
            )
        }
        return "服务已开启"
    }

    private fun doUnbindService(): String {
        if (isBind) {
            isBind = false
            unbindService(serviceConnection)
        }
        return "服务关闭成功"
//
//        if (!isBind) {
//            return "服务未开启"
//        }
//
//        unbindService(serviceConnection)
//        isBind = false
//        curBleControl = null
//        return "服务关闭成功"
    }

    override fun onDestroy() {
        doUnbindService()
        super.onDestroy()
    }
}