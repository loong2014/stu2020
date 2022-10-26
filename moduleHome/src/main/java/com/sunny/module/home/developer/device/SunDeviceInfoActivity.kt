package com.sunny.module.home.developer.device

import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.widget.ListView
import com.alibaba.android.arouter.facade.annotation.Route
import com.sunny.lib.common.adapter.CommonKeyValueInfo
import com.sunny.lib.common.adapter.CommonKeyValueLVAdapter
import com.sunny.lib.common.base.BaseActivity
import com.sunny.lib.common.router.RouterConstant
import com.sunny.module.home.R

/**
 * Created by zhangxin17 on 2020/8/18
 * 设备信息，mac，sn，model等
 */
@Route(path = RouterConstant.Tool.PageDeviceInfo)
class SunDeviceInfoActivity : BaseActivity() {

    private lateinit var mListView: ListView
    private lateinit var mAdapter: CommonKeyValueLVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_act_device_info)

        findViewById<View>(R.id.btn_refresh).setOnClickListener {
            updateData()
        }

        mListView = findViewById(R.id.list_view)
        mAdapter = CommonKeyValueLVAdapter(mmActivity)
        mListView.adapter = mAdapter
    }

    private fun updateData() {
        val list = mutableListOf<CommonKeyValueInfo>()

        addSystemPropInfo(list)

        addDisplayInfo(list)

        addStatusBarInfo(list)

        mAdapter.updateData(list)
    }

    private fun addSystemPropInfo(list: MutableList<CommonKeyValueInfo>) {
        list.apply {
            add(CommonKeyValueInfo("系统属性", ""))
            add(CommonKeyValueInfo("ro.build.id", Build.ID))
            add(CommonKeyValueInfo("ro.build.display.id", Build.DISPLAY))
            add(CommonKeyValueInfo("ro.product.name", Build.PRODUCT))
            add(CommonKeyValueInfo("ro.product.device", Build.DEVICE))
            add(CommonKeyValueInfo("ro.product.board", Build.BOARD))
            add(CommonKeyValueInfo("ro.product.brand", Build.BRAND))
        }
    }

    private fun addDisplayInfo(list: MutableList<CommonKeyValueInfo>) {
        list.add(CommonKeyValueInfo("尺寸信息", ""))
        //
        var infoValue = "---"
        val point = Point()
        windowManager.defaultDisplay?.getSize(point)
        infoValue = "pointXY(${point.x} x ${point.y})"
        list.add(CommonKeyValueInfo("windowManager.defaultDisplay", infoValue))

        //
        var dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        infoValue = "whPixels(${dm.widthPixels} x ${dm.heightPixels})" +
                "\n density(${dm.density})" +
                "\n scaledDensity(${dm.scaledDensity})" +
                "\n densityDpi(${dm.densityDpi})" +
                "\n xyDip(${dm.xdpi} , ${dm.ydpi})"
        list.add(CommonKeyValueInfo("windowManager.defaultDisplay.getMetrics", infoValue))

        //
        dm = resources.displayMetrics
        infoValue = "whPixels(${dm.widthPixels} x ${dm.heightPixels})" +
                "\n density(${dm.density})" +
                "\n scaledDensity(${dm.scaledDensity})" +
                "\n densityDpi(${dm.densityDpi})" +
                "\n xyDip(${dm.xdpi} , ${dm.ydpi})"
        list.add(CommonKeyValueInfo("resources.displayMetrics", infoValue))
    }

    private fun addStatusBarInfo(list: MutableList<CommonKeyValueInfo>) {
        list.add(CommonKeyValueInfo("状态栏", ""))

        //
        val decorView = window.findViewById<View>(Window.ID_ANDROID_CONTENT)
        var infoValue = decorView?.run {
            "wh($width , $height)" +
                    "\n($left , $top , $right , $bottom)"
        } ?: "---"
        list.add(CommonKeyValueInfo("decorView", infoValue))

        //
        val frame = Rect()
        decorView.getWindowVisibleDisplayFrame(frame)
        infoValue = frame.run {
            "($left , $top , $right , $bottom)"
        }
        list.add(CommonKeyValueInfo("decorDisplayFrame", infoValue))

        //
        infoValue = "${frame.left - decorView.left}" +
                " , ${frame.top - decorView.top}" +
                " , ${(frame.right - frame.left) - decorView.right}" +
                " , ${(frame.bottom - frame.top) - decorView.bottom}"
        list.add(CommonKeyValueInfo("statusBarInfo", infoValue))

        //
        val resId = resources.getIdentifier("status_bar_height", "dimen", "android")
        infoValue = if (resId > 0) {
            resources.getDimensionPixelSize(resId).toString()
        } else {
            "---"
        }
        list.add(CommonKeyValueInfo("status_bar_height", infoValue))


//        private int getStatusBarHeight(Context context) {
//
//            int result = 0;
//
//            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
//
//            if (resourceId > 0) {
//
//                result = context.getResources().getDimensionPixelSize(resourceId);
//
//            }
//
//            return result;
//
//        }

    }
//
//
//    private fun onGetDeviceInfo() {
//        tvDeviceName.text = deviceInfoViewModel.deviceName
//        tvSdkVersion.text = deviceInfoViewModel.sdkVersion
////        tvSnKey.text = mDeviceInfoModel.snInfo
//        tvMac.text = deviceInfoViewModel.mac
//        tvSystemUiVersion.text = deviceInfoViewModel.systemUiVersion
//        tvSystemSoftVersion.text = deviceInfoViewModel.systemSoftVersion
//        tvInternetAddress.text = deviceInfoViewModel.internetAddress
//
//        val point = Point()
//        windowManager.defaultDisplay?.getSize(point)
//        tvDisplayInfo.text = "(${point.x} , ${point.y})"
////        tvCpuInfo.text = mDeviceInfoModel.cpuInfo
//
////        mDeviceInfoModel.networkModel.isNetworkAvailable()
//    }

}