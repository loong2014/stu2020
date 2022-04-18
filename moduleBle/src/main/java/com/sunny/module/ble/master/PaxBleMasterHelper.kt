package com.sunny.module.ble.master

import com.sunny.module.ble.showLog
import com.sunny.module.ble.utils.PaxByteUtils
import java.nio.ByteBuffer

/**
 * 处理通过通知获取的日历信息
 */
internal class PaxBleCalendarNotifyHelper(private val name: String) {
    private var curSessionId: Int = 0
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

        showLog(name, "onRcvData :$sessionId , $pageSize , $pageIndex")

        if (sessionId != curSessionId) {
            curSessionId = sessionId
            curPageSize = pageSize
            rcvData = byteArrayOf()
        }
        rcvData += byteArray.copyOfRange(8, len)

        // 是否接收完毕
        return curPageSize == (pageIndex + 1).toShort()
    }
}

/**
 * 处理通过read获取的日历信息
 */
internal class PaxBleCalendarReadHelper(val name: String) {

    var curSessionId: Int = -1
    var curTotalSize: Int = -1

    var curPos = 0
    var curReadSize = 240
    var hasReadAll = false

    var rcvData: ByteArray = byteArrayOf()

    fun updateReadSize(size: Int) {
        curReadSize = size
    }

    fun doInit(byteArray: ByteArray) {
        val sessionIdArray = byteArray.copyOf(4)
        val totalSizeArray = byteArray.copyOfRange(4, 8)

        curSessionId = ByteBuffer.wrap(sessionIdArray).int
        curTotalSize = ByteBuffer.wrap(totalSizeArray).int
        rcvData = byteArrayOf()
        showLog(name, "doInit :$curSessionId , $curTotalSize")
    }

    fun getNextReadData(): ByteArray? {
        if (hasReadAll) return null

        val size = if (curPos + curReadSize > curTotalSize) {
            curTotalSize - curPos
        } else {
            curReadSize
        }

        showLog(name, "getNextReadData $curSessionId , $curPos , $size")
        return PaxByteUtils.intTo4Byte(curSessionId) +
                PaxByteUtils.intTo4Byte(curPos) +
                PaxByteUtils.intTo2Byte(size)
    }

    fun saveReadData(byteArray: ByteArray): Boolean {
        val len = byteArray.size
        if (len <= 10) {
            showLog(name, "saveReadData error len = $len")
            return false
        }

        val validData = byteArray.copyOfRange(10, len)
        curPos += validData.size
        rcvData += validData

        showLog(name, "saveReadData $curTotalSize/$curPos")
        hasReadAll = curPos >= curTotalSize
        return true
    }
}