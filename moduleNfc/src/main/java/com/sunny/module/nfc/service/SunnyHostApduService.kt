package com.sunny.module.nfc.service

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import com.sunny.module.nfc.nfcLog

/**
 * 只要 NFC 读取器向您的服务发送应用协议数据单元 (APDU)，系统就会调用 processCommandApdu()。
 * APDU 也在 ISO/IEC 7816-4 规范中定义。
 * APDU 是在 NFC 读取器和您的 HCE 服务之间进行交换的应用级数据包。
 * 该应用级协议为半双工：NFC 读取器会向您发送命令 APDU，反之它会等待您发送响应 APDU。
 */
class SunnyHostApduService : HostApduService() {
    companion object {
        private val _hostApduService: HostApduService by lazy {
            SunnyHostApduService()
        }

        fun get(): HostApduService {
            return _hostApduService
        }
    }

    override fun processCommandApdu(commandApdu: ByteArray, extras: Bundle?): ByteArray {
        nfcLog("processCommandApdu  commandApdu=${String(commandApdu)}")

        val cla = commandApdu[0]
        val ins = commandApdu[1]
        val optTip = when (ins) {
            "0xA4".toByte() -> "select"
            "0xCA".toByte() -> "getMessage"
            "0xB0".toByte() -> "readBinary"
            "0xA4".toByte() -> "select"
            "0x5c".toByte() -> "getBalance"
            "0x50".toByte() -> "purchaseInit"
            "0xDC".toByte() -> "updateRecord"
            "0x54".toByte() -> "purchase"
            else -> "unknown"
        }
        nfcLog("processCommandApdu :$optTip")
        return "apduService".toByteArray()
    }

    override fun onDeactivated(reason: Int) {
        val tip = when (reason) {
            DEACTIVATION_LINK_LOSS -> "LINK_LOSS"
            DEACTIVATION_DESELECTED -> "DESELECTED"
            else -> "None"
        }
        nfcLog("onDeactivated($tip)")
    }
}

/**
 * 发送响应数据
 */
fun sendResponseApdu(byteArray: ByteArray) {
    SunnyHostApduService.get().sendResponseApdu(byteArray)
}