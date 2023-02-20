package com.sunny.stu.module.cs.service

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sunny.stu.module.cs.*
import kotlinx.android.synthetic.main.activity_service.*

class ServiceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_service)

        initObserve()
    }

    fun clickOpen(view: View?) {
        startService(Intent(this, PaxCenterService::class.java))
    }

    fun clickClose(view: View?) {
        stopService(Intent(this, PaxCenterService::class.java))
    }

    private fun initObserve() {
        serviceStatusInfoLiveData.observe(this) {
            val info = "${System.currentTimeMillis()} status :$it"
            tv_server_info.text = info
        }

        serviceClientInfoLiveData.observe(this) {
            tv_client_info.text = it
        }

        serviceSendMsgLiveData.observe(this) {
            val info = "${System.currentTimeMillis()} send :$it"
            tv_send_info.text = info
        }

        serviceRcvMsgLiveData.observe(this) {
            val info = "${System.currentTimeMillis()} rcv :$it"
            tv_rcv_info.text = info
        }
    }
}