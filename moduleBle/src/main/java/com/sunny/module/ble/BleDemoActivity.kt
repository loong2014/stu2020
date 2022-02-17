package com.sunny.module.ble

import kotlinx.android.synthetic.main.ble_activity_demo.*

class BleDemoActivity : BleDemoBaseActivity() {

    private var isScanning = false
    private var isConnected = false

    override fun resetService() {
        isScanning = false
        tv_ble_scan_tip.text = "None"

        isConnected = false
        tv_ble_connect_tip.text = "None"

        super.resetService()
    }

    override fun initView() {
        super.initView()

        btn_ble_scan_start.setOnClickListener {
            val msg = getBleControl()?.run {
                if (startScan()) {
                    isScanning = true
                    "开始扫描成功"
                } else {
                    "开始扫描失败"
                }
            } ?: "服务未开启"
            tv_ble_scan_tip.text = msg
            showConnectTip(msg)
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
            tv_ble_scan_tip.text = msg
            showConnectTip(msg)
        }

        btn_ble_scan_show.setOnClickListener {
            val msg = BleConfig.curDiscoveryDevices.takeIf {
                it.isNotEmpty()
            }?.let {
                BleTools.buildDevicesShowMsg(it, "showPairedDevices")
            } ?: "No Devices Found"

            showTip(msg)
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

            tv_ble_connect_tip.text = msg
            showConnectTip(msg)
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
            tv_ble_connect_tip.text = msg
            showConnectTip(msg)
        }
    }
}