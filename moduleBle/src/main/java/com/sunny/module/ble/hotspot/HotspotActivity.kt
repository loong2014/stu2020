package com.sunny.module.ble.hotspot

import android.Manifest
import android.app.Activity
import android.content.Context
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Bundle
import com.sunny.lib.common.base.BaseActivity
import com.sunny.module.ble.R
import com.sunny.module.ble.hotspot.arcode.QrCodeMode
import kotlinx.android.synthetic.main.ble_activity_hotspot.*
import timber.log.Timber

/**
 * https://developer.android.com/reference/android/net/wifi/WifiManager#startLocalOnlyHotspot(android.net.wifi.WifiManager.LocalOnlyHotspotCallback,%20android.os.Handler)
 *
 * https://grd-gerrit-01.faradayfuture.com:8443/source/xref/DF91_Q_MPC_BRINGUP/vendor/ff/packages/apps/FFVehicle/src/com/ff/vehicle/settings/connectivity/hotspot/HotspotController.java
 */
class HotspotActivity : BaseActivity() {

    private val mQrModel: QrCodeMode by lazy {
        QrCodeMode()
    }

    private var mWifiConfiguration: WifiConfiguration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ble_activity_hotspot)
        initView()
    }

    private fun initView() {
        top_bar.setMiddleName("Hotspot")

        top_bar.setOnBackBtnClickListener {
            doExitActivity()
        }

        btn_check.setOnClickListener {
            requestPermission(mmActivity, 100)
        }

        btn_hotspot.setOnClickListener {
            doGetHotspotInfo()
        }
    }

    private fun showHotspotTip(tip: String) {
        hotspot_info.text = tip
        Timber.i(tip)
    }

    private fun showHotspotInfo() {
        val config = mWifiConfiguration
        if (config == null) {
            showHotspotTip("WifiConfiguration is null")
            return
        }


        val name = config.SSID
        val password = config.preSharedKey
        val qrString = buildQrString(name, password)

        val sb = StringBuilder()
        sb.append("Name :$name")
        sb.append("\n")
        sb.append("Password :$password")
        sb.append("\n")
        sb.append("QrString :$qrString")

        showHotspotTip(sb.toString())

        val bmp = mQrModel.createQrBitmap(qrString, 500, 500)
        hotspot_qr.setImageBitmap(bmp)
    }

    private fun buildQrString(name: String, password: String): String {
        val sb = StringBuilder()
        sb.append("WIFI:")
        sb.append("T:WPA;")
        sb.append("S:")
        sb.append(name)
        sb.append(";P:")
        sb.append(password)
        sb.append(";")
        return sb.toString()
    }

    /**
     * 判断是否有访问位置的权限，没有权限，直接申请位置权限
     */
    private fun requestPermission(activity: Activity, requestCode: Int): Boolean {

        activity.requestPermissions(
            arrayOf(
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE
            ), requestCode
        )
        return true
    }

    private val localOnlyHotspotCallback = object : WifiManager.LocalOnlyHotspotCallback() {
        override fun onStarted(reservation: WifiManager.LocalOnlyHotspotReservation?) {
            super.onStarted(reservation)

            mWifiConfiguration = reservation?.wifiConfiguration
            showHotspotInfo()

            // 使用完后要调用关闭，不然无法再次 startLocalOnlyHotspot
            reservation?.close()
        }

        override fun onStopped() {
            super.onStopped()
            showHotspotTip("onStopped")
        }

        override fun onFailed(reason: Int) {
            super.onFailed(reason)
            val tip = when (reason) {
                ERROR_NO_CHANNEL -> "没有频道"
                ERROR_GENERIC -> "一般错误"
                ERROR_INCOMPATIBLE_MODE -> "模式不兼容"
                ERROR_TETHERING_DISALLOWED -> "错误网络共享不允许"

                else -> "unknown :$reason"
            }
            showHotspotTip(tip)
        }
    }

    private fun doGetHotspotInfo() {
        val manager = getSystemService(Context.WIFI_SERVICE) as WifiManager
        manager.startLocalOnlyHotspot(localOnlyHotspotCallback, null)
    }

}