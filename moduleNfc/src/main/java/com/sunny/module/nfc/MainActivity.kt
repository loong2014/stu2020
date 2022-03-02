package com.sunny.module.nfc

import android.content.Intent
import android.os.Bundle
import com.sunny.lib.common.base.BaseActivity
import com.sunny.module.nfc.service.NfcServiceActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * https://developer.android.com/guide/topics/connectivity/nfc?hl=zh-cn
 */
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        btn_client.setOnClickListener {
            startActivity(Intent(mmActivity, NfcClientActivity::class.java))
        }

        btn_service.setOnClickListener {
            startActivity(Intent(mmActivity, NfcServiceActivity::class.java))
        }

        btn_all.setOnClickListener {
            startActivity(Intent(mmActivity, NfcTechActivity::class.java))
        }
    }
}