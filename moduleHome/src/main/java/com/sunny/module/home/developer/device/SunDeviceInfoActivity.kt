package com.sunny.module.home.developer.device

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.sunny.lib.common.base.BaseActivity
import com.sunny.lib.common.router.RouterConstant
import com.sunny.module.home.R
import kotlinx.android.synthetic.main.home_act_device_info.*

/**
 * Created by zhangxin17 on 2020/8/18
 * 设备信息，mac，sn，model等
 */
@Route(path = RouterConstant.Tool.PageDeviceInfo)
class SunDeviceInfoActivity : BaseActivity() {

    lateinit var deviceInfoViewModel: DeviceViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.home_act_device_info)

        deviceInfoViewModel = ViewModelProvider(this).get(DeviceViewModel::class.java)

        initData()

        btn_device_info.setOnClickListener {
            deviceInfoViewModel.doGetDeviceInfo()
        }

        deviceInfoViewModel.doGetDeviceInfo()
    }

    private fun initData() {
        deviceInfoViewModel.deviceInfo.observe(this, Observer {
            onGetDeviceInfo()
        })
    }

    private fun onGetDeviceInfo() {
        tvDeviceName.text = deviceInfoViewModel.deviceName
        tvSdkVersion.text = deviceInfoViewModel.sdkVersion
//        tvSnKey.text = mDeviceInfoModel.snInfo
        tvMac.text = deviceInfoViewModel.mac
        tvSystemUiVersion.text = deviceInfoViewModel.systemUiVersion
        tvSystemSoftVersion.text = deviceInfoViewModel.systemSoftVersion
        tvInternetAddress.text = deviceInfoViewModel.internetAddress

//        tvCpuInfo.text = mDeviceInfoModel.cpuInfo

//        mDeviceInfoModel.networkModel.isNetworkAvailable()
    }
}