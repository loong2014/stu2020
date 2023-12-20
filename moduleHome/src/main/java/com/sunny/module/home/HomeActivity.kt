package com.sunny.module.home

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Bundle
import com.sunny.lib.common.base.BaseActivity
import com.sunny.lib.common.service.IAccountService
import com.sunny.lib.common.service.ServiceFactory
import kotlinx.android.synthetic.main.home_activity_home.btn_auto_login
import kotlinx.android.synthetic.main.home_activity_home.btn_get_user_info
import kotlinx.android.synthetic.main.home_activity_home.module_name
import kotlinx.android.synthetic.main.home_activity_home.tv_tip
import timber.log.Timber

/**
 * Created by zhangxin17 on 2020/12/29
 */
open class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity_home)

        module_name.text = "模块：Home"

        initView()
//        registerSoftApConnectionState(this)
    }

    private fun initView() {

        btn_get_user_info.setOnClickListener {
            val accountService: IAccountService = ServiceFactory.getInstance().accountService
            val id = accountService.getUserId()
            val name = accountService.getUserName()

            updateTip("$name : $id")

        }

        btn_auto_login.setOnClickListener {
            val accountService: IAccountService = ServiceFactory.getInstance().accountService

            accountService.doAutoLogin()
        }
    }

    protected fun updateTip(tip: String) {
        tv_tip.text = tip
    }

    private var mSoftApConnectionNum = 0 // 当前热点连接设备数量

    /**
     * 使用libs/framework.jar中的的WifiManager
     */
    private fun registerSoftApConnectionState(context: Context) {
        val wifiManager =
            context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val softApCallback = object : WifiManager.SoftApCallback {
            override fun onStateChanged(state: Int, failureReason: Int) {
                // 在这里处理状态改变事件
                Timber.i("PaxHostSpot onStateChanged state:$state, failureReason:$failureReason")
                if (state == 13) {
                    // WIFI_AP_STATE_DISABLING，热点开启，更改热点名称，会触发热点关闭+打开的操作
                }
            }

            override fun onNumClientsChanged(numClients: Int) {
                // 在这里处理连接的客户端数量改变事件
                Timber.i("PaxHostSpot onNumClientsChanged  $mSoftApConnectionNum >>> $numClients")
                if (numClients > mSoftApConnectionNum) {
//                    requestToShowDeviceConnected(numClients)
                }
                mSoftApConnectionNum = numClients
            }
        }
        wifiManager.registerSoftApCallback(softApCallback, null)
    }

}