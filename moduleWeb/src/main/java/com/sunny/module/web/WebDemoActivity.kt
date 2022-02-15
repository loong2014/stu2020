package com.sunny.module.web

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.sunny.lib.common.base.BaseActivity
import com.sunny.lib.common.router.RouterConstant
import com.sunny.module.web.ble.BleTools
import com.sunny.module.web.ble.client.BleClientService
import com.sunny.module.web.ble.client.IBleClientInterface
import com.sunny.module.web.ble.service.PaxBleService
import com.sunny.module.web.ble.service.PaxBleServiceOld
import kotlinx.android.synthetic.main.web_activity_demo.*

@Route(path = RouterConstant.Web.PageDemo)
class WebDemoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.web_activity_demo)

        initView()
    }

    private fun initView() {
        top_bar.setMiddleName("模块：WebView")
        top_bar.setOnBackBtnClickListener(View.OnClickListener {
            doExitActivity()
        })

        btn_webview.setOnClickListener {
            ARouter.getInstance().build(RouterConstant.Web.PageWebView).navigation()
        }

        btn_http.setOnClickListener {
            ARouter.getInstance().build(RouterConstant.Web.PageHttp).navigation()
        }

        btn_cache.setOnClickListener {
            startActivity(Intent(this, HttpCacheActivity::class.java))
        }

        btn_ble_target.setOnClickListener {
            changedTargetDevice()
        }
        btn_ble_old.setOnClickListener {
            openBleService(false)
        }
        btn_ble_new.setOnClickListener {
            openBleService(true)
        }
        btn_ble_scan_start.setOnClickListener {
            sendBleOpt(1)
            showTip("开始扫描")
        }
        btn_ble_scan_stop.setOnClickListener {
            sendBleOpt(2)
            showTip("停止扫描")
        }
        btn_ble_open_discoverable.setOnClickListener {
            BleTools.openDiscoverable(this)
        }
        btn_ble_msg_send.setOnClickListener {
            val msg = if (isClient) {
                "data from client(${System.currentTimeMillis()})"
            } else {
                "data from service(${System.currentTimeMillis()})"
            }
            val isOk = bleControl?.sendMsg(msg)
            showTip("send msg result :$isOk")
        }

        btn_ble_msg_read.setOnClickListener {
            val msg = bleControl?.readMsg() ?: "None"
            showTip(msg)
        }

        changedTargetDevice()
    }

    private fun changedTargetDevice() {
        val dev = BleTools.updateTarget()
        val msg = "${dev.first}\n${dev.second}"
        tv_ble_target.text = msg
    }

    private fun showTip(msg: String) {
        tv_ble_content.text = msg
    }

    var bleControl: IBleClientInterface? = null
    var isBind = false
    var isClient = false
    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            bleControl = IBleClientInterface.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }
    }

    private fun openBleService(isBle: Boolean) {
        if (isBind) {
            showTip("关闭蓝牙扫描服务")
            unbindService(serviceConnection)
            isBind = false
        } else {
            isBind = true
            if (isBle) {
                showTip("打开低功耗蓝牙扫描服务")
                bindService(
                    Intent(this, PaxBleService::class.java),
                    serviceConnection,
                    Context.BIND_AUTO_CREATE
                )
            } else {
                showTip("打开传统蓝牙扫描服务")
                bindService(
                    Intent(this, PaxBleServiceOld::class.java),
                    serviceConnection,
                    Context.BIND_AUTO_CREATE
                )
            }
        }
    }

    private fun sendBleOpt(opt: Int) {
        if (isBind && bleControl != null) {
            bleControl?.sendOpt(opt)
        }
    }

    private fun startBleClient() {
        if (isBind) return
        if (BleTools.checkBlePermission(this, 300)) {
            isBind = true
            isClient = true
            bindService(
                Intent(this, BleClientService::class.java),
                serviceConnection,
                Context.BIND_AUTO_CREATE
            )
        }
    }

    private fun startPaxGuardService() {

        val intent = Intent()
        intent.component =
            ComponentName("com.ff.iai.paxlauncher", "com.ff.iai.guardservice.PaxLauncherService")
        startForegroundService(intent)
//            131            Intent intent = new Intent();
//            132            intent.setComponent(new ComponentName("com.ff.ext.services",
//            133                    "com.ff.analytics.service.AnalyticsService"));
//            134            startServiceAsUser(intent, UserHandle.SYSTEM);

//        sendBroadcastAsUser(intent,null)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }
}