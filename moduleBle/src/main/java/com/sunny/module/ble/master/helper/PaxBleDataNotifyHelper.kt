package com.sunny.module.ble.master.helper

import timber.log.Timber
import java.nio.ByteBuffer

/**
 * 处理通过通知获取的日历信息
 */
internal class PaxBleDataNotifyHelper(private val logTag: String) {
    private var curSessionId: Int = -1
    private var curPageSize: Short = -1
    var rcvData: ByteArray = byteArrayOf()

    fun onRcvData(byteArray: ByteArray): Boolean {
        val len = byteArray.size
        if (len <= 8) {
            return false
        }
        val sessionId = ByteBuffer.wrap(byteArray.copyOf(4)).int
        val pageSize = ByteBuffer.wrap(byteArray.copyOfRange(4, 6)).short
        val pageIndex = ByteBuffer.wrap(byteArray.copyOfRange(6, 8)).short

        showLog("onRcvData :$sessionId , $pageSize , $pageIndex")

        if (sessionId != curSessionId) {
            curSessionId = sessionId
            curPageSize = pageSize
            rcvData = byteArrayOf()
        }
        rcvData += byteArray.copyOfRange(8, len)

        // 是否接收完毕
        return curPageSize == (pageIndex + 1).toShort()
    }

    fun doClear() {
        curSessionId = -1
        curPageSize = -1
        rcvData = byteArrayOf()
    }

    private fun showLog(log: String) {
        Timber.i("$logTag-$log")
    }
}