package com.sunny.module.ble.master.helper

import com.sunny.module.ble.utils.PaxByteUtils
import timber.log.Timber
import java.nio.ByteBuffer

/**
 * 处理通过read获取的日历信息
 */
internal class PaxBleDataReadHelper(private val logTag: String) {

    private var curSessionId: Int = -1
    private var curTotalSize: Int = -1

    private var curPos = 0
    private var curReadSize = 240
    private var hasReadAll = false

    var rcvData: ByteArray = byteArrayOf()

    fun updateReadSize(size: Int) {
        curReadSize = size
    }

    fun doClear() {
        curSessionId = -1
        curTotalSize = -1
        curPos = 0
        curReadSize = 240
        hasReadAll = false

        rcvData = byteArrayOf()
    }

    fun doInit(byteArray: ByteArray) {
        val sessionIdArray = byteArray.copyOf(4)
        val totalSizeArray = byteArray.copyOfRange(4, 8)

        curSessionId = ByteBuffer.wrap(sessionIdArray).int
        curTotalSize = ByteBuffer.wrap(totalSizeArray).int
        rcvData = byteArrayOf()
        showLog("doInit :$curSessionId , $curTotalSize")
    }

    fun getNextReadData(): ByteArray? {
        if (hasReadAll) return null

        val size = if (curPos + curReadSize > curTotalSize) {
            curTotalSize - curPos
        } else {
            curReadSize
        }

        showLog("getNextReadData $curSessionId , $curPos , $size")
        return PaxByteUtils.intTo4Byte(curSessionId) +
                PaxByteUtils.intTo4Byte(curPos) +
                PaxByteUtils.intTo2Byte(size)
    }

    fun saveReadData(byteArray: ByteArray): Boolean {
        val len = byteArray.size
        if (len <= 10) {
            showLog("saveReadData error len = $len")
            return false
        }

        val validData = byteArray.copyOfRange(10, len)
        curPos += validData.size
        rcvData += validData

        showLog("saveReadData $curTotalSize/$curPos")
        hasReadAll = curPos >= curTotalSize
        return true
    }


    private fun showLog(log: String) {
        Timber.i("$logTag-$log")
    }

}