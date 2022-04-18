package com.sunny.module.ble.slave

import com.sunny.lib.base.log.SunLog
import com.sunny.module.ble.meeting.PaxMeetingDebugTools
import com.sunny.module.ble.utils.PaxByteUtils

/**
 * 处理通过 send 日历信息
 */
internal class PaxBleCalendarDataHelper() {

    companion object {
        var curSessionId: Int = 0
    }

    private var curDataArray: ByteArray = byteArrayOf()
    private var curTotalSize: Int = -1

    private var curReadPos = 0
    private var curReadSize = 240

    private var hasReadAll = false

    fun updateReadSize(size: Int) {
        curReadSize = size
    }

    fun doInitData() {
        curSessionId += 1
        curDataArray = PaxMeetingDebugTools.buildMeetingInfo()
        curTotalSize = curDataArray.size
        hasReadAll = false

        showLog("doInitData :$curSessionId , $curTotalSize")
    }

    fun getAllData(): ByteArray {
        return curDataArray
    }

    fun getCloseByteArray(): ByteArray {
        return PaxByteUtils.intTo4Byte(0)
    }

    fun getAuthFailedByteArray(): ByteArray {
        return PaxByteUtils.intTo4Byte(-1)
    }

    fun buildResponseData(byteArray: ByteArray): ByteArray? {
        return null
    }

    fun getNextSendData(offset: Int): ByteArray? {
        if (hasReadAll) return null

        val from = offset
        val to = if (from + curReadSize > curTotalSize) {
            curTotalSize
        } else {
            from + curReadSize
        }
        return curDataArray.copyOfRange(from, to)
    }

    private fun showLog(log: String) {
        SunLog.i("PaxBle-$curSessionId", log)
    }
}