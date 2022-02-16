package com.sunny.module.web.ble

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.TextView
import com.sunny.lib.common.base.BaseActivity
import com.sunny.module.web.R
import com.sunny.module.web.ble.client.BleClientService
import com.sunny.module.web.ble.client.BleClientServiceOld
import com.sunny.module.web.ble.client.IBleClientInterface
import com.sunny.module.web.ble.service.PaxBleService
import com.sunny.module.web.ble.service.PaxBleServiceOld
import kotlinx.android.synthetic.main.web_activity_ble.*

class BleDemoActivity : BaseActivity() {

    private var isSupportBle = false
    private var isBind = false
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
        setContentView(R.layout.web_activity_ble)

        initView()

        initConfig()
    }

    private fun updateConfig() {

    }

    private fun initConfig() {
//        tv_ble_cur_info.

        tv_ble_demo_type.text = if (BleConfig.curBleDemoIsClient) {
            "客户端"
        } else {
            "服务端"
        }

        tv_ble_server_type.text = if (BleConfig.curBleServerIsClassic) {
            "低功耗蓝牙"
        } else {
            "传统蓝牙"
        }

        tv_ble_target.text = BleConfig.curBleTarget.run {
            "${first}\n${second}"
        }

        doDeviceCheck()

        doOpenDiscoverable()
    }

    private fun doDeviceCheck() {
        isSupportBle = false
        if (BleTools.checkBlePermission(this, 300)) {
            BleTools.openDiscoverable(this)
            isSupportBle = true
        } else {
            showTip("权限检测未通过")
        }

        tv_ble_check.text = if (isSupportBle) {
            "支持"
        } else {
            "不支持"
        }
    }

    private fun doOpenDiscoverable() {
        BleTools.openDiscoverable(this)

        tv_ble_discovery.text = if (BleTools.isBleDiscoverableEnable()) {
            "可见"
        } else {
            "不可见"
        }
    }

    private fun showTip(msg: String) {
        tv_ble_content.text = msg
        BleConfig.bleLog(msg)
    }

    private fun initView() {
        top_bar.setMiddleName("蓝牙Demo")
        top_bar.setOnBackBtnClickListener {
            doExitActivity()
        }

        btn_hide_show_config.setOnClickListener {
            if (ble_config_layout.visibility == View.VISIBLE) {
                ble_config_layout.visibility = View.GONE
                (it as TextView).text = "配置界面(隐藏)"
            } else {
                ble_config_layout.visibility = View.VISIBLE
                (it as TextView).text = "配置界面(显示)"
            }
        }

        btn_hide_show_opt.setOnClickListener {
            if (ble_opt_layout.visibility == View.VISIBLE) {
                ble_opt_layout.visibility = View.GONE
                (it as TextView).text = "操作界面(隐藏)"
            } else {
                ble_opt_layout.visibility = View.VISIBLE
                (it as TextView).text = "操作界面(显示)"
            }
        }

        btn_ble_demo_type.setOnClickListener {
            tv_ble_demo_type.text = if (BleConfig.updateBleDemoType()) {
                "客户端"
            } else {
                "服务端"
            }

            resetService()
        }

        btn_ble_server_type.setOnClickListener {
            tv_ble_server_type.text = if (BleConfig.updateBleServerType()) {
                "低功耗蓝牙"
            } else {
                "传统蓝牙"
            }

            resetService()
        }

        btn_ble_check.setOnClickListener {
            doDeviceCheck()
        }

        btn_ble_discovery.setOnClickListener {
            doOpenDiscoverable()
        }


        btn_ble_target.setOnClickListener {
            BleConfig.updateBleTarget()
            updateConfig()
            resetService()
        }


        btn_ble_service_start.setOnClickListener {
            if (isSupportBle) {
                startBleService()
            }
        }
        btn_ble_service_stop.setOnClickListener {
            if (isSupportBle) {
                stopBleService()
            }
        }

        btn_ble_scan_start.setOnClickListener {
            if (sendBleOpt(BleConfig.OptTypeScanStart)) {
                showTip("开始扫描")
            }
        }
        btn_ble_scan_stop.setOnClickListener {
            if (sendBleOpt(BleConfig.OptTypeScanStop)) {
                showTip("停止扫描")
            }
        }

        btn_ble_paired_show.setOnClickListener {
            val msg = getBleControl()?.readDeviceInfo(BleConfig.MsgTypePairedDevices) ?: "None"
            showTip(msg)
        }

        btn_ble_scan_show.setOnClickListener {
            val msg = getBleControl()?.readDeviceInfo(BleConfig.MsgTypeScanDevices) ?: "None"
            showTip(msg)
        }

        btn_ble_recent_tip.setOnClickListener {
            val msg = getBleControl()?.readDeviceInfo(BleConfig.MsgTypeRecentTip) ?: "None"
            showTip(msg)
        }

        btn_ble_connect_start.setOnClickListener {
            if (sendBleOpt(BleConfig.OptTypeConnectStart)) {
                showTip("开始连接")
            }
        }
        btn_ble_connect_stop.setOnClickListener {
            if (sendBleOpt(BleConfig.OptTypeConnectStop)) {
                showTip("停止连接")
            }
        }

        btn_ble_msg_send.setOnClickListener {
            val msg = if (BleConfig.curBleDemoIsClient) {
                "data from client(${System.currentTimeMillis()})"
            } else {
                "data from service(${System.currentTimeMillis()})"
            }

            val isOk = getBleControl()?.run {
                sendMsg(msg)
            } ?: false
            showTip("send msg result :$isOk")
        }

        btn_ble_msg_read.setOnClickListener {
            val msg = getBleControl()?.readMsg() ?: "None"
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

    private fun sendBleOpt(opt: Int): Boolean {
        return getBleControl()?.run {
            sendOpt(opt)
        } ?: false
    }


    private fun resetService() {
        if (isBind) {
            unbindService(serviceConnection)
            showTip("服务已关闭")
        }
        isBind = false
    }

    private fun startBleService() {
        if (isBind) {
            showTip("服务已开启")
            return
        }

        if (BleConfig.curBleDemoIsClient) {
            if (startClientService()) {
                isBind = true
                showTip("服务开启成功")
            } else {
                showTip("服务开启失败")
            }
        } else {
            if (startServiceService()) {
                isBind = true
                showTip("服务开启成功")
            } else {
                showTip("服务开启失败")
            }
        }
    }

    private fun startClientService(): Boolean {
        return if (BleConfig.curBleServerIsClassic) {
            bindService(
                Intent(this, BleClientService::class.java),
                serviceConnection,
                Context.BIND_AUTO_CREATE
            )
        } else {
            bindService(
                Intent(this, BleClientServiceOld::class.java),
                serviceConnection,
                Context.BIND_AUTO_CREATE
            )
        }
    }

    private fun startServiceService(): Boolean {
        return if (BleConfig.curBleServerIsClassic) {
            bindService(
                Intent(this, PaxBleService::class.java),
                serviceConnection,
                Context.BIND_AUTO_CREATE
            )
        } else {
            bindService(
                Intent(this, PaxBleServiceOld::class.java),
                serviceConnection,
                Context.BIND_AUTO_CREATE
            )
        }
    }

    private fun stopBleService() {
        if (!isBind) {
            showTip("服务未开启")
            return
        }

        unbindService(serviceConnection)
        isBind = false
        showTip("服务关闭成功")
    }


    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }
}