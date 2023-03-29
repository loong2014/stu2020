package com.sunny.lib.common.connect

import android.os.Process
import com.sunny.lib.common.connect.PaxConnectConfig.OPTION_CAN_PLAY
import com.sunny.lib.common.connect.PaxConnectConfig.OPTION_STOP_PLAY
import com.sunny.lib.common.connect.PaxConnectConfig.OPTION_UPDATE_PLAY
import timber.log.Timber
import java.net.DatagramPacket

object PaxConnectHelper {

    @Synchronized
    suspend fun canPlayVideo(): Boolean {
        val result = doSendMsgToCenterSync(OPTION_CAN_PLAY) ?: return true
        val curPid = Process.myPid()
        val playPid = result.msg?.toInt()
        Timber.i("canPlayVideo curPid=$curPid , playPid=$playPid")
        return curPid == playPid
    }

    @Synchronized
    suspend fun stopPlayVideo(): Boolean {
        val result = doSendMsgToCenterSync(OPTION_STOP_PLAY)
        return result?.isSendSucceed() ?: true
    }

    @Synchronized
    suspend fun sendMsgToCenter(msg: String): Boolean {
        val result = doSendMsgToCenterSync(msg)
        return result?.isSendSucceed() ?: true
    }

    @Synchronized
    fun updatePlayingPid(): Boolean {
        val result = doSendMsgToCenterSync(OPTION_UPDATE_PLAY)
        return result?.isSendSucceed() ?: true
    }

    private var closeConnectJudge = false

    @Synchronized
    private fun doSendMsgToCenterSync(msg: String?): PaxConnectResultInfo? {
        Timber.i("doSendMsgToCenterSync msg=$msg")
        if (closeConnectJudge) return null

        val resultInfo = PaxConnectResultInfo()

        if (msg.isNullOrBlank()) {
            resultInfo.onEmptyMsg()
            return resultInfo
        }
        val socket = PaxConnectConfig.buildSendCenterSocket()
        socket.soTimeout = PaxConnectConfig.SO_TIMEOUT

        try {
            // send
            val targetPacket = PaxConnectConfig.buildSendCenterDatagramPacket()
            targetPacket.data = PaxConnectConfig.buildSendDataByteArray(msg)
            socket.send(targetPacket)
            Timber.i("sendToCenter send succeed")

            // receive
            val rcvByteArray = ByteArray(PaxConnectConfig.BUFFER_SIZE)
            val rcvPacket = DatagramPacket(rcvByteArray, rcvByteArray.size)
            socket.receive(rcvPacket)
            Timber.i("sendToCenter receive succeed")

            // result
            PaxConnectConfig.parseResultPacket(rcvPacket, resultInfo)
        } catch (e: Throwable) {
            closeConnectJudge = true
            Timber.i("sendToCenter error :$e")
            resultInfo.onError(e.toString())
        } finally {
            socket.close()
        }
        return resultInfo
    }
}