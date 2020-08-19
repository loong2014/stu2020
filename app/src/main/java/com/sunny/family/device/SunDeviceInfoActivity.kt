package com.sunny.family.device

import android.os.Bundle
import com.sunny.family.R
import com.sunny.lib.base.BaseActivity
import kotlinx.android.synthetic.main.act_device_info.*

/**
 * Created by zhangxin17 on 2020/8/18
 * 设备信息，mac，sn，model等
 */
class SunDeviceInfoActivity : BaseActivity() {

    private val mDeviceInfoModel by lazy {
        DeviceInfoModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_device_info)

        btn_device_info.setOnClickListener {
            doGetDeviceInfo()
        }
    }

    private fun doGetDeviceInfo() {
        tv_android_sdk_value.text = mDeviceInfoModel.sdkCode.toString()
        tv_sn_value.text = mDeviceInfoModel.snInfo
        tv_mac_value.text = mDeviceInfoModel.macInfo
        tv_cpu_value.text = mDeviceInfoModel.cpuInfo

        mDeviceInfoModel.networkModel.isNetworkAvailable()
    }
}