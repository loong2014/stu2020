package com.sunny.module.ble.debug

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.sunny.lib.base.binding.CommonBindingListener
import com.sunny.lib.common.base.BaseActivity
import com.sunny.module.ble.BleTools
import com.sunny.module.ble.R
import com.sunny.module.ble.databinding.BleActivityConfigBinding

class BleConfigActivity : BaseActivity() {
    private lateinit var mActivityBinding: BleActivityConfigBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mActivityBinding = DataBindingUtil.setContentView(this, R.layout.ble_activity_config)

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
                        updateMsgTip("None")
                    }
                    "deviceCheck" -> {
                        updateMsgTip(BleTools.deviceCheck())
                    }

                    "permissionCheck" -> {
                        updateMsgTip(BleTools.permissionCheck(mmActivity))
                    }
                    "pairedDevice" -> {
                        updateMsgTip(BleTools.getPairedDevice())

                    }
                    "requestPermission" -> {
                        updateMsgTip(BleTools.requestBlePermission(mmActivity, 100))
                    }

                    "enableBle" -> {
                        updateMsgTip(BleTools.requestEnableBle(mmActivity, 200))
                    }
                    "enableLocation" -> {
                        updateMsgTip(BleTools.requestEnableLocation(mmActivity, 300))
                    }
                }
            }
        }
    }

    private fun updateTopTip(tip: String) {
        mActivityBinding.tvTopTip.text = tip
    }

    private fun updateMsgTip(msg: String) {
        mActivityBinding.tvMsgInfo.text = msg
    }
}