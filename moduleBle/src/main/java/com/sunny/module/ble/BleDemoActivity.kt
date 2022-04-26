package com.sunny.module.ble

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.sunny.lib.base.binding.CommonBindingListener
import com.sunny.lib.common.base.BaseActivity
import com.sunny.module.ble.databinding.BleActivityDemoBinding
import com.sunny.module.ble.debug.BleConfigActivity
import com.sunny.module.ble.master.BleMasterActivity
import com.sunny.module.ble.slave.BleSlaveActivity

class BleDemoActivity : BaseActivity() {
    private lateinit var mActivityBinding: BleActivityDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mActivityBinding = DataBindingUtil.setContentView(this, R.layout.ble_activity_demo)

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
                    "bleConfig" -> {
                        doStartActivity(BleConfigActivity::class.java)
                    }

                    "bleSlave" -> {
                        doStartActivity(BleSlaveActivity::class.java)
                    }
                    "bleMaster" -> {
                        doStartActivity(BleMasterActivity::class.java)
                    }
                }
            }
        }

    }

    private fun doStartActivity(cls: Class<*>) {
        startActivity(Intent(mmActivity, cls))
    }

    private fun updateTopTip(tip: String) {
        mActivityBinding.tvTopTip.text = tip
    }

    private fun updateMsgTip(msg: String) {
        mActivityBinding.tvMsgInfo.text = msg
    }

}