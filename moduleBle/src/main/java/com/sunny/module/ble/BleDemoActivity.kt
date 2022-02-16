package com.sunny.module.ble

import android.bluetooth.BluetoothAdapter
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.TextView
import com.sunny.lib.common.base.BaseActivity
import com.sunny.module.ble.client.BleClientClassic
import com.sunny.module.ble.server.BleServerBle
import com.sunny.module.ble.server.BleServerClassic
import kotlinx.android.synthetic.main.ble_activity_demo.*

class BleDemoActivity : BaseActivity() {

    private var isSupportBle = false
    private var isBind = false
    private var isScanning = false
    private var isConnected = false
    private var curBleControl: IBleClientInterface? = null
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            curBleControl = IBleClientInterface.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ble_activity_demo)

        initView()

        doDeviceCheck()
    }

    private fun doDeviceCheck() {
        isSupportBle = false
        if (BleTools.checkBlePermission(this, 300)) {
            BleTools.openDiscoverable(this)
            isSupportBle = true
        } else {
            showTip("权限检测未通过")
        }
    }

    private fun showTip(msg: String) {
        tv_ble_content.text = msg
        BleConfig.bleLog(msg)
    }

    private fun resetService() {
        if (isBind) {
            unbindService(serviceConnection)

            tv_ble_service_tip.text = "服务已关闭"
            isBind = false

            tv_ble_scan_tip.text = "扫描已停止"
            isScanning = false

            tv_ble_connect_tip.text = "未连接"
            isConnected = false
        }
        BleConfig.resetCurTargetDevice()
        tv_ble_target.text = "None"

        showTip("resetService")
    }

    private fun initView() {
        top_bar.setMiddleName("蓝牙Demo")
        top_bar.setOnBackBtnClickListener {
            doExitActivity()
        }

        initConfigView()
        initServerView()
        initScanView()
        initConnectView()
        initMsgView()
    }

    private fun initConfigView() {

        tv_ble_cur_info.text = BluetoothAdapter.getDefaultAdapter()?.run {
            "$name , $address"
        } ?: "None"

        btn_hide_show_config.setOnClickListener {
            if (ble_config_layout.visibility == View.VISIBLE) {
                ble_config_layout.visibility = View.GONE
                (it as TextView).text = "配置界面(隐藏)"
            } else {
                ble_config_layout.visibility = View.VISIBLE
                (it as TextView).text = "配置界面(显示)"
            }
        }

        btn_ble_demo_type.setOnClickListener {
            BleConfig.updateBleDemoType()

            if (BleConfig.curBleDemoIsClient) {
                tv_ble_demo_type.text = "客户端"
                et_msg.setText("msg from client")
                ble_client_config_layout.visibility = View.VISIBLE
                ble_client_opt_layout.visibility = View.VISIBLE

            } else {
                tv_ble_demo_type.text = "服务端"
                et_msg.setText("msg from server")
                ble_client_config_layout.visibility = View.GONE
                ble_client_opt_layout.visibility = View.GONE
            }

            resetService()
        }
        btn_ble_demo_type.callOnClick()

        btn_ble_server_type.setOnClickListener {
            BleConfig.updateBluetoothType()
            tv_ble_server_type.text = if (BleConfig.curBluetoothTypeIsClassic) {
                "传统蓝牙"
            } else {
                "低功耗蓝牙"
            }

            resetService()
        }
        btn_ble_server_type.callOnClick()

        btn_ble_paired_show.setOnClickListener {
            val msg = BleTools.getBondedDevices()?.takeIf {
                it.isNotEmpty()
            }?.let {
                BleTools.buildDevicesShowMsg(it, "showPairedDevices")
            } ?: "No Paired Devices"

            showTip(msg)
        }
        btn_ble_apply_permission.setOnClickListener {
            BleTools.checkBlePermission(this, 300)
        }
        btn_ble_open_discovery.setOnClickListener {
            BleTools.openDiscoverable(this)
        }
    }

    private fun initServerView() {
        btn_ble_service_start.setOnClickListener {
            val msg = if (isSupportBle) {
                startBleService()
            } else {
                "UnSupportBle"
            }
            showTip(msg)
            tv_ble_service_tip.text = msg
        }
        btn_ble_service_stop.setOnClickListener {
            val msg = if (isSupportBle) {
                stopBleService()
            } else {
                "UnSupportBle"
            }
            showTip(msg)
            tv_ble_service_tip.text = msg
        }
    }

    private fun initScanView() {
        btn_ble_scan_start.setOnClickListener {
            val msg = getBleControl()?.run {
                if (startScan()) {
                    isScanning = true
                    "开始扫描成功"
                } else {
                    "开始扫描失败"
                }
            } ?: "服务未开启"
            showTip(msg)
            tv_ble_scan_tip.text = msg
        }

        btn_ble_scan_stop.setOnClickListener {
            val msg = if (isScanning) {
                getBleControl()?.run {
                    if (stopScan()) {
                        isScanning = false
                        "停止扫描成功"
                    } else {
                        "停止扫描失败"
                    }
                } ?: "服务未开启"
            } else {
                "扫描未开始"
            }
            showTip(msg)
            tv_ble_scan_tip.text = msg
        }

        btn_ble_scan_show.setOnClickListener {
            val msg = BleConfig.curDiscoveryDevices.takeIf {
                it.isNotEmpty()
            }?.let {
                BleTools.buildDevicesShowMsg(it, "showPairedDevices")
            } ?: "No Devices Found"

            showTip(msg)
        }
    }

    private fun initConnectView() {
        btn_ble_target.setOnClickListener {
            tv_ble_target.text = BleConfig.updateTargetDevice()?.run {
                "${name}\n${address}"
            } ?: "None"
        }

        btn_ble_connect_start.setOnClickListener {
            val msg = BleConfig.curTargetDevice?.let { dev ->
                getBleControl()?.run {
                    if (startConnect(dev.address)) {
                        isConnected = true
                        "设备连接成功"
                    } else {
                        "设备连接失败"
                    }
                } ?: "服务未开启"
            } ?: "未选择连接设备"

            showTip(msg)
            tv_ble_connect_tip.text = msg
        }
        btn_ble_connect_stop.setOnClickListener {
            val msg = if (isConnected) {
                getBleControl()?.run {
                    if (stopConnect()) {
                        isConnected = false
                        "断开连接成功"
                    } else {
                        "断开连接失败"
                    }
                } ?: "服务未开启"
            } else {
                "设备未连接"
            }
            showTip(msg)
            tv_ble_connect_tip.text = msg
        }
    }

    private fun initMsgView() {

        btn_ble_msg_send.setOnClickListener {
            val tip = if (isConnected) {
                getBleControl()?.run {
                    val msg = "${System.currentTimeMillis()} :${et_msg.text?.toString()}"
                    if (sendMsg(msg)) {
                        "消息发送成功"
                    } else {
                        "消息发送失败"
                    }
                } ?: "服务未开启"
            } else {
                "设备未连接"
            }
            showTip(tip)
        }

        btn_ble_recent_tip.setOnClickListener {
            val msg = getBleControl()?.readDeviceInfo(BleConfig.MsgTypeRecentTip) ?: "None"
            showTip(msg)
        }
    }

    private fun getBleControl(): IBleClientInterface? {
        return if (isSupportBle && isBind) {
            curBleControl
        } else {
            null
        }
    }

    private fun startBleService(): String {
        if (isBind) {
            return "服务已开启"
        }

        return if (BleConfig.curBleDemoIsClient) {
            if (startClientService()) {
                isBind = true
                "服务开启成功"
            } else {
                "服务开启失败"
            }
        } else {
            if (startServiceService()) {
                isBind = true
                "服务开启成功"
            } else {
                "服务开启失败"
            }
        }
    }

    private fun startClientService(): Boolean {
        return if (BleConfig.curBluetoothTypeIsClassic) {
            bindService(
                Intent(this, BleClientClassic::class.java),
                serviceConnection,
                Context.BIND_AUTO_CREATE
            )
        } else {
            bindService(
                Intent(this, BleServerBle::class.java),
                serviceConnection,
                Context.BIND_AUTO_CREATE
            )
        }
    }

    private fun startServiceService(): Boolean {
        return if (BleConfig.curBluetoothTypeIsClassic) {
            bindService(
                Intent(this, BleServerClassic::class.java),
                serviceConnection,
                Context.BIND_AUTO_CREATE
            )
        } else {
            bindService(
                Intent(this, BleServerBle::class.java),
                serviceConnection,
                Context.BIND_AUTO_CREATE
            )
        }
    }

    private fun stopBleService(): String {
        if (!isBind) {
            return "服务未开启"
        }

        unbindService(serviceConnection)
        isBind = false
        return "服务关闭成功"
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }
}