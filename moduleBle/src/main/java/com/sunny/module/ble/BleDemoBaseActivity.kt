package com.sunny.module.ble

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.TextView
import com.sunny.lib.common.base.BaseActivity
import com.sunny.module.ble.client.ble.BleClientBle
import com.sunny.module.ble.client.classic.BleClientClassic
import com.sunny.module.ble.server.ble.BleServerBle
import com.sunny.module.ble.server.classic.BleServerClassic
import com.sunny.module.ble.utils.StringLrcCircle
import kotlinx.android.synthetic.main.ble_activity_demo.*

open class BleDemoBaseActivity : BaseActivity() {

    private val tipList = StringLrcCircle(10)

    var isClientDemo = false // 是否是客户端
    var isClassic = true // 是否是经典蓝牙

    var isSupportBle = false
    var isBind = false
    private var curBleControl: IBleClientInterface? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            curBleControl = IBleClientInterface.Stub.asInterface(service)
            curBleControl?.registerCallBack(bleCallback)
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
        ble_tip.text = tip
    }

    private fun showTipList() {
        val sb = StringBuilder()
        tipList.toList().forEach {
            sb.append("\n$it")
        }
        showTip(sb.toString())
    }

    fun showRcvMsg(msg: String) {
        val tip = "Rcv >>> $msg"
        BleConfig.bleLog(msg)
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

    var sendSeq: Int = 0
    fun doSendMsg(info: String) {
        val seq = sendSeq++
        val msg = "($seq)$info"
        BleConfig.bleLog("send msg :$msg")
        val tip = getBleControl()?.run {
            if (sendMsg(msg)) {
                "($seq)成功"
            } else {
                "($seq)失败"
            }
        } ?: "服务未开启"

        BleConfig.bleLog(tip)
        tipList.add(tip)
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

        val adapter = BluetoothAdapter.getDefaultAdapter()
        if (adapter == null) {
            showCheckMsg("不支持蓝牙")
            return
        }

        if (!adapter.isEnabled) {
            showCheckMsg("蓝牙未开启")
            return
        }

        showCheckMsg("isLeExtendedAdvertisingSupported :${adapter.isLeExtendedAdvertisingSupported}")
        showCheckMsg("isMultipleAdvertisementSupported :${adapter.isMultipleAdvertisementSupported}")

        showCheckMsg("通告isLe2MPhySupported :${adapter.isLe2MPhySupported}")
        showCheckMsg("isLeCodedPhySupported :${adapter.isLeCodedPhySupported}")
        showCheckMsg("isLePeriodicAdvertisingSupported :${adapter.isLePeriodicAdvertisingSupported}")
        showCheckMsg("isOffloadedFilteringSupported :${adapter.isOffloadedFilteringSupported}")
        showCheckMsg("isOffloadedScanBatchingSupported :${adapter.isOffloadedScanBatchingSupported}")

        val msg = "当前设备:${adapter.name} , ${adapter.address} "
        tv_own_device_info.text = msg

        showCheckMsg("start check permission")
        if (!BleTools.checkBlePermission(this, 300)) {
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
        isClientDemo = !isClientDemo

        if (isClientDemo) {
            top_bar.setMiddleName("蓝牙-客户端")
            et_msg.setText("client msg")
            ble_client_config_layout.visibility = View.VISIBLE
        } else {
            top_bar.setMiddleName("蓝牙-服务端")
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
        btn_ble_msg_send.setOnClickListener {
            val msg = et_msg.text?.toString() ?: "None"
            doSendMsg(msg)
        }
    }

    private fun doBindService(): String {
        if (!isSupportBle) return "蓝牙初始化失败"

        if (isBind) return "服务已开启"

        val cls: Class<*> = if (isClientDemo) {
            if (isClassic) {
                BleClientClassic::class.java
            } else {
                BleClientBle::class.java
            }
        } else {
            if (isClassic) {
                BleServerClassic::class.java
            } else {
                BleServerBle::class.java
            }
        }

        isBind = bindService(
            Intent(this, cls),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )

        return if (isBind) {
            "服务开启成功"
        } else {
            "服务开启失败"
        }
    }

    private fun doUnbindService(): String {
        if (!isBind) {
            return "服务未开启"
        }

        unbindService(serviceConnection)
        isBind = false
        curBleControl = null
        return "服务关闭成功"
    }

    override fun onDestroy() {
        doUnbindService()
        super.onDestroy()
    }
}