package com.sunny.module.ble.master

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.sunny.lib.base.binding.CommonBindingListener
import com.sunny.module.ble.BleBaseActivity
import com.sunny.module.ble.PaxBleConfig
import com.sunny.module.ble.PaxBleConfig.EXT_USER_DISPLAY
import com.sunny.module.ble.PaxBleConfig.EXT_USER_FFID
import com.sunny.module.ble.PaxBleConfig.EXT_USER_ID
import com.sunny.module.ble.R
import com.sunny.module.ble.SEAT_RSD_NAME
import com.sunny.module.ble.databinding.BleActivityMasterBinding

class BleMasterActivity : BleBaseActivity() {
    private lateinit var mActivityBinding: BleActivityMasterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mActivityBinding = DataBindingUtil.setContentView(this, R.layout.ble_activity_master)

        initListener()
    }

    private fun initListener() {
        mActivityBinding.eventListener = object : CommonBindingListener {
            override fun commonOpt(v: View) {
                commonOpt(v, "")
            }

            override fun commonOpt(v: View, opt: String) {
                when (v.tag) {
                    "clearMsg" -> {
                        clearMsgTip()
                    }
                    "startService" -> {
                        doBindService(PaxBleMasterService::class.java)
                    }

                    "stopService" -> {
                        doUnbindService()
                    }

                    "userLogin" -> {
                        val bleIntent = Intent(mmActivity, PaxBleMasterService::class.java).apply {
                            putExtra(EXT_USER_DISPLAY, SEAT_RSD_NAME)
                            putExtra(EXT_USER_ID, PaxBleConfig.BleDebugUserID)
                            putExtra(EXT_USER_FFID, PaxBleConfig.BleDebugFFID)
                        }
                        startService(bleIntent)
                    }
                    "userLogout" -> {
                        val bleIntent = Intent(mmActivity, PaxBleMasterService::class.java).apply {
                            putExtra(EXT_USER_DISPLAY, SEAT_RSD_NAME)
                            putExtra(EXT_USER_ID, -1)
                            putExtra(EXT_USER_FFID, "")
                        }
                        startService(bleIntent)
                    }
                }
            }
        }
    }

    private fun updateTopTip(tip: String) {
        mActivityBinding.tvTopTip.text = tip
    }

    val msgTipSB = StringBuilder()
    override fun showTip(msg: String) {
        msgTipSB.append("$msg\n")
        updateMsgTip(msgTipSB.toString())
    }

    private fun clearMsgTip() {
        msgTipSB.clear()
        updateMsgTip("None")
    }

    private fun updateMsgTip(msg: String) {
        mActivityBinding.tvMsgInfo.text = msg
    }
}