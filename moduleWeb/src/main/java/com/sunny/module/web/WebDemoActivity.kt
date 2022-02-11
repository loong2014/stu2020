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

        btn_ble_service.setOnClickListener {
            startBleService()
        }

        btn_ble_client.setOnClickListener {
            startBleClient()
        }
        btn_ble_msg_send.setOnClickListener {
            val msg = if (isClient) {
                "data from client(${System.currentTimeMillis()})"
            } else {
                "data from service(${System.currentTimeMillis()})"
            }
            val isOk = bleControl?.sendMsg(msg)
            tv_ble_content.text = "send msg result :$isOk"
        }

        btn_ble_msg_read.setOnClickListener {
            val msg = bleControl?.readMsg() ?: "None"
            tv_ble_content.text = msg
        }
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

    private fun startBleService() {
        if (isBind) return
        if (BleTools.checkBlePermission(this, 200)) {
            isBind = true
            isClient = false
            bindService(
                Intent(this, PaxBleService::class.java),
                serviceConnection,
                Context.BIND_AUTO_CREATE
            )
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
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }
}