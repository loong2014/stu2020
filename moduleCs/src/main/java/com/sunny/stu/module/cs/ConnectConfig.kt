package com.sunny.stu.module.cs

import androidx.lifecycle.MutableLiveData
import java.net.DatagramPacket
import java.nio.ByteBuffer
import kotlin.experimental.xor

const val PORT = 10086
const val HOST_MPC = "172.26.200.2"
const val HOST_HPC = "172.26.200.3"

val serviceStatusInfoLiveData = MutableLiveData<String?>()
val serviceClientInfoLiveData = MutableLiveData<String>()
val serviceSendMsgLiveData = MutableLiveData<String?>()
val serviceRcvMsgLiveData = MutableLiveData<String?>()


interface SendCallback {
    fun onError()
    fun onResult(result: String?)
}
fun main() {
    val array = buildSendDataByteArray(1, 100, "hello")
    val sb = StringBuilder()
    array.forEach {
        sb.append("$it ")
    }
    println("aaaa >>> $sb")


    println("bbb1 >>> ${readDisplayId(array)}")
    println("bbb2 >>> ${readSeqNum(array)}")
    println("bbb3 >>> ${readMsg(array)}")

}

fun buildSendDataByteArray(displayId: Int, seq: Int, msg: String): ByteArray {
    val msgByteArray = msg.toByteArray()

    val byteArray = ByteArray(msgByteArray.size + 6)

    // displayId
    byteArray[0] = displayId.toByte()

    // seq
    val seqByteArray = intToByteArray4(seq)
    System.arraycopy(seqByteArray, 0, byteArray, 1, seqByteArray.size)

    // checkNum
    byteArray[5] = buildCheckNum(msgByteArray)

    // msg
    System.arraycopy(msgByteArray, 0, byteArray, 6, msgByteArray.size)

    return byteArray
}

fun isValidRcvPacket(packet: DatagramPacket): Boolean {
    val byteArray = packet.data ?: return false
    val len = packet.length
    if (len < 6) return false
    return byteArray[5] == buildCheckNum(byteArray, 6, len)
}

fun isValid(byteArray: ByteArray, len: Int): Boolean {
    if (byteArray.size < 6) return false
    return byteArray[5] == buildCheckNum(byteArray, 6, len)
}

fun readDisplayId(byteArray: ByteArray): Int {
    return byteArray[0].toInt()
}

fun readSeqNum(byteArray: ByteArray): Int {
    return ByteBuffer.wrap(byteArray, 1, 4).int
}

fun readMsg(byteArray: ByteArray, len: Int? = null): String {
    val end = len ?: byteArray.size
    return String(byteArray, 6, end - 6, Charsets.UTF_8)
}


fun getDisplayId(): Int {
    return 1
}

var num: Int = 0
fun getSeqNum(): Int {
    var seq = num + 1
    if (seq >= Int.MAX_VALUE) {
        seq = 0
    }
    num = seq
    return seq
}

private fun intToByteArray4(num: Int): ByteArray {
    val byteArray = ByteArray(4)
    byteArray[0] = ((num shr 24) and 0xff).toByte()
    byteArray[1] = ((num shr 16) and 0xff).toByte()
    byteArray[2] = ((num shr 8) and 0xff).toByte()
    byteArray[3] = (num and 0xff).toByte()
    return byteArray
}


private fun buildCheckNum(byteArray: ByteArray, offset: Int = 0, len: Int? = null): Byte {
    var checknum: Byte = 0x02
    val end = len ?: byteArray.size
    byteArray.forEachIndexed { index, byte ->
        if (index in offset..end) {
            checknum = checknum xor byte
        }
    }
    checknum = checknum xor 0x03
    return checknum
}