package com.sunny.lib.common.connect

import com.ff.iai.lib.core.connect.PaxConnectConfig
import java.net.DatagramPacket

class PaxConnectResultInfo {
    var succeed: Boolean = false
    var errorInfo: String? = null

    var displayType: Byte = -1
    var pid: Int = -1
    var msg: String? = null

    var resultPacket: DatagramPacket? = null

    fun onEmptyMsg() {
        succeed = false
        errorInfo = "msg is empty"
    }

    fun onError(e: String) {
        succeed = false
        errorInfo = e
    }

    fun onInvalid() {
        succeed = false
        errorInfo = "result not valid"
    }

    fun onSucceed(displayType: Byte, pid: Int, msg: String) {
        succeed = true
        this.displayType = displayType
        this.pid = pid
        this.msg = msg
    }

    fun isSendSucceed(): Boolean {
        return succeed && PaxConnectConfig.RESULT_SUCCEED == msg
    }

    override fun toString(): String {
        return "succeed=$succeed , errorInfo=$errorInfo , ($displayType, $pid, $msg)"
    }
}