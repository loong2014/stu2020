package com.sunny.module.ble.slave

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.sunny.lib.base.binding.CommonBindingListener
import com.sunny.module.ble.BleBaseActivity
import com.sunny.module.ble.R
import com.sunny.module.ble.databinding.BleActivityMasterBinding

class BleSlaveActivity : BleBaseActivity() {
    private lateinit var mActivityBinding: BleActivityMasterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mActivityBinding = DataBindingUtil.setContentView(this, R.layout.ble_activity_slave)

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

                    "startService" -> {
                        doBindService(PaxBleSlaveService::class.java)
                    }

                    "stopService" -> {
                        doUnbindService()
                    }

                    "userLogin" -> {

                    }
                    "userLogout" -> {
                    }
                }
            }
        }

    }

    override fun showTip(msg: String) {
    }

    private fun updateTopTip(tip: String) {
        mActivityBinding.tvTopTip.text = tip
    }

    private fun updateMsgTip(msg: String) {
        mActivityBinding.tvMsgInfo.text = msg
    }
}