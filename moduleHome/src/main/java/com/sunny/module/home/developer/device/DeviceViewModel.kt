package com.sunny.module.home.developer.device

import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sunny.lib.common.utils.SunDeviceUtils
import com.sunny.lib.common.utils.SunNetworkUtils

class DeviceViewModel : ViewModel() {

    // 网络信息
    var networkInfo = MutableLiveData<Int>()

    var sdkVersion: String = "xxx"

    var mac: String = "xxx"

    var deviceName: String = "xxx"

    var snCode: String = "xxx"

    var systemUiVersion: String = "xxx"
    var systemSoftVersion: String = "xxx"

    var internetAddress: String = "xxx"

    fun doGetDeviceInfo() {
        deviceName = Build.DEVICE

        sdkVersion = Build.VERSION.SDK_INT.toString()

        mac = SunDeviceUtils.mac

        snCode = Build.MODEL

        systemUiVersion = SunDeviceUtils.getSystemProperty("ro.letv.release.version")
        systemSoftVersion = SunDeviceUtils.getSystemProperty("ro.build.id")

        internetAddress = if (SunNetworkUtils.isNetworkAvailable()) {
            SunNetworkUtils.getIpAddress()
        } else {
            "0.0.0.0"
        }

        //
        networkInfo.value = 2
    }
}