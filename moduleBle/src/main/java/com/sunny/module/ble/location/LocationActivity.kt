package com.sunny.module.ble.location

import android.os.Bundle
import com.sunny.lib.common.base.BaseActivity
import com.sunny.module.ble.R
import kotlinx.android.synthetic.main.ble_activity_location.*

class LocationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ble_activity_location)
        initView()
    }

    private fun initView() {
        top_bar.setMiddleName("Location")

        top_bar.setOnBackBtnClickListener {
            doExitActivity()
        }

        btn_check.setOnClickListener {
            PaxLocationManager.doCheck(mmActivity, 100)
        }

        btn_update_gps.setOnClickListener {
            PaxLocationManager.doUpdateByGPS(mmActivity)
        }

        btn_update_network.setOnClickListener {
            PaxLocationManager.doUpdateByNetwork(mmActivity)
        }

        PaxLocationManager.doInit(mmActivity)

        PaxLocationManager.getZipCodeTip().observe(this) {
            top_tip.text = it
        }

        PaxLocationManager.providersLV.observe(this) {
            provider_tip.text = it?.toString() ?: "null"
        }
        PaxLocationManager.locationLV.observe(this) {
            location_tip.text = it?.toString() ?: "null"
        }

        PaxLocationManager.addressLV.observe(this) {
            address_tip.text = it?.toString() ?: "null"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        PaxLocationManager.doRelease(mmActivity)
    }
}