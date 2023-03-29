package com.sunny.lib.common.connect

import android.os.Process
import com.sunny.lib.common.connect.PaxConnectResultInfo
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.nio.ByteBuffer
import kotlin.experimental.xor

object PaxConnectConfig {
    const val HOST_MPC = "172.26.200.2"
    const val HOST_HPC = "172.26.200.3"


    const val FROM_DISPLAY_TYPE_CID: Byte = 1
    const val FROM_DISPLAY_TYPE_FPD: Byte = 2
    const val FROM_DISPLAY_TYPE_RSD: Byte = 3
    const val FROM_DISPLAY_TYPE_MUSIC: Byte = 4


    const val SO_TIMEOUT = 5_000
    const val BUFFER_SIZE = 4096

    const val EXIT_PLAY_TIMEOUT = 5_000L
    const val UPDATE_PLAY_DURATION = 3_000L

    const val RESULT_SUCCEED = "succeed"
    const val OPTION_CAN_PLAY = "can_play"
    const val OPTION_UPDATE_PLAY = "update_play"
    const val OPTION_STOP_PLAY = "stop_play"

    fun buildSendCenterDatagramPacket(): DatagramPacket {
        return DatagramPacket(ByteArray(0), 0, InetAddress.getByName(HOST_HPC), 10086)
    }

    fun buildSendCenterSocket(): DatagramSocket {
        val port = 10402
        return DatagramSocket(port)
//        return DatagramSocket(sendCenterSocketPort)
    }

//    private val sendCenterSocketPort: Int by lazy {
//        when (LibCoreConfig.appContext.packageName) {
//            "com.ff.iai.paxlauncher" -> {
//                buildSocketPort(10100)
//            }
//            "com.ff.iai.paxweb" -> {
//                buildSocketPort(10200)
//            }
//            "com.ff.iai.liveapp" -> {
//                buildSocketPort(10300)
//            }
//            else -> -1
//        }
//    }
//
//    private fun buildSocketPort(defPort: Int): Int {
//        return if (isUser0) {
//            defPort + 1
//        } else if (isCID) {
//            defPort + 2
//        } else if (isRSD) {
//            defPort + 3
//        } else if (isFPD) {
//            defPort + 4
//        } else {
//            defPort + 5
//        }
//    }
//
//    private val displayTypeByte: Byte by lazy {
//        when {
//            isCID -> FROM_DISPLAY_TYPE_CID
//            isFPD -> FROM_DISPLAY_TYPE_FPD
//            isRSD -> FROM_DISPLAY_TYPE_RSD
//            else -> -1
//        }
//    }

    fun buildSendDataByteArray(msg: String): ByteArray {
        val msgByteArray = msg.toByteArray()
        val len = msgByteArray.size + 6

        val byteArray = ByteArray(len)

        // displayId
//        byteArray[0] = displayTypeByte
        byteArray[0] = FROM_DISPLAY_TYPE_MUSIC

        // pid
        val seqByteArray = intToByteArray4(Process.myPid())
        System.arraycopy(seqByteArray, 0, byteArray, 1, seqByteArray.size)

        // msg
        System.arraycopy(msgByteArray, 0, byteArray, 5, msgByteArray.size)

        // checkNum
        byteArray[len - 1] = buildCheckNum(byteArray, len)

        return byteArray
    }

    fun parseResultPacket(packet: DatagramPacket, resultInfo: PaxConnectResultInfo) {
        val byteArray = packet.data
        val len = packet.length

        if (byteArray == null || len < 6) {
            resultInfo.onInvalid()
            return
        }

        if (byteArray[len - 1] != buildCheckNum(byteArray, len)) {
            resultInfo.onInvalid()
            return
        }

        val type = byteArray[0]
        val pid = ByteBuffer.wrap(byteArray, 1, 4).int
        val msg = String(byteArray, 5, len - 6, Charsets.UTF_8)
        resultInfo.onSucceed(type, pid, msg)
    }

    private fun intToByteArray4(num: Int): ByteArray {
        val byteArray = ByteArray(4)
        byteArray[0] = ((num shr 24) and 0xff).toByte()
        byteArray[1] = ((num shr 16) and 0xff).toByte()
        byteArray[2] = ((num shr 8) and 0xff).toByte()
        byteArray[3] = (num and 0xff).toByte()
        return byteArray
    }

    private fun buildCheckNum(byteArray: ByteArray, len: Int): Byte {
        var checknum: Byte = 0x02
        byteArray.forEachIndexed { index, byte ->
            if (index in 0 until len - 1) {
                checknum = checknum xor byte
            }
        }
        checknum = checknum xor 0x03
        return checknum
    }
}
