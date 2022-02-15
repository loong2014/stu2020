package com.sunny.module.web.ble.client

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.sunny.lib.base.log.SunLog

class BleClientService : Service() {

    var isOpen = false
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        openDiscover()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun openDiscover() {
        log("openDiscover $isOpen")
        if (isOpen) return
        isOpen = true
    }

    override fun onBind(intent: Intent?): IBinder? {
        return mClientBleBinder
    }

    private val mClientBleBinder = object : IBleClientInterface.Stub() {
        override fun sendMsg(msg: String?): Boolean {
            log("sendMsg :$msg")
            if (msg != null) {
                return doSeenMsg(msg)
            }
            return false
        }

        override fun readMsg(): String {
            return buildResultMsg()
        }

        override fun sendOpt(opt: Int): Boolean {
            return true
        }
    }

    private fun doSeenMsg(msg: String): Boolean {

        return true
    }

    private fun buildResultMsg(): String {
        val sb = StringBuilder()
        return sb.toString()
    }

    private fun log(msg: String) {
        SunLog.i("PaxBleService-Client", msg)
    }
}