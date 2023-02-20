package com.sunny.stu.module.cs

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

interface PaxReceiveCallback {
    fun onReceiveMsg(displayId: Int, msg: String)
    fun paxLog(tag: String, msg: String)
}

interface PaxSendCallback {
    fun onError(e: Throwable)
    fun onResult(succeed: Boolean, result: String?)
}

class PaxConnectModel(private val paxCallback: PaxReceiveCallback) {

    var globalRcvSocket: DatagramSocket? = null

    fun openReceive() {
        paxCallback.paxLog("status", "openReceive :$globalRcvSocket")
        if (globalRcvSocket != null) return


        GlobalScope.launch(Dispatchers.IO) {

            val byteArray = ByteArray(bufferSize)
            val rcvPacket = DatagramPacket(byteArray, byteArray.size)
            try {
                val datagramSocket = DatagramSocket(clientPort)
                globalRcvSocket = datagramSocket
                paxCallback.paxLog("status", "openReceive socket=$globalRcvSocket")
                while (true) {
                    datagramSocket.receive(rcvPacket)
                    parseRcvPacket(rcvPacket)
                }
            } catch (e: Exception) {
                paxCallback.paxLog("status", "openReceive error :$e")
                globalRcvSocket?.close()
            }
        }
    }

    fun closeReceive() {
        val isConnected = globalRcvSocket?.isConnected
        paxCallback.paxLog("status", "closeReceive(isConnected=$isConnected) :$globalRcvSocket")
        GlobalScope.launch(Dispatchers.IO) {

            try {
                if (globalRcvSocket?.isConnected == true) {
                    globalRcvSocket?.close()
                    globalRcvSocket = null
                    paxCallback.paxLog("status", "closeReceive succeed")
                }
            } catch (e: Exception) {
                paxCallback.paxLog("status", "closeReceive error :$e")
            }
        }
    }

    fun doSendMsgToCenter(sendMsg: String, callback: PaxSendCallback? = null) {
        sendMsg(centerHost, centerPort, centerPort, sendMsg, callback)
    }

    private fun sendMsg(
        host: String,
        port: Int,
        clientPort: Int,
        sendMsg: String,
        callback: PaxSendCallback? = null
    ) {

        paxCallback.paxLog("status", "sendMsg($host:$port) msg=$sendMsg")
        GlobalScope.launch(Dispatchers.IO) {

            val socket = DatagramSocket(clientPort)
            socket.soTimeout = soTimeout

            paxCallback.paxLog("status", "build socket=$socket")

            try {
                // send
                val sendDisplayId = displayId
                val sendSeq = buildSeqNum()
                val sendPacket = DatagramPacket(
                    ByteArray(0), 0,
                    InetAddress.getByName(host), port
                ).apply {
                    data = buildSendDataByteArray(sendDisplayId, sendSeq, sendMsg)
                }
                socket.send(sendPacket)
                paxCallback.paxLog("send", "displayId=$sendDisplayId , seq=$sendSeq , msg=$sendMsg")

                // receive
                val rcvByteArray = ByteArray(bufferSize)
                val rcvPacket = DatagramPacket(rcvByteArray, rcvByteArray.size)
                socket.receive(rcvPacket)

                parseRcvPacket(rcvPacket, callback)
            } catch (e: Throwable) {
                callback?.onError(e)
            } finally {
                socket.close()
            }
        }
    }

    private fun parseRcvPacket(
        rcvPacket: DatagramPacket,
        callback: PaxSendCallback? = null
    ) {
        val rcvIpInfo = rcvPacket.socketAddress.toString().substring(1)
        var rcvInfo: String? = null

        if (isValidRcvPacket(rcvPacket)) {
            val rcvByteArray = rcvPacket.data
            val displayId = readDisplayId(rcvByteArray)
            val seq = readSeqNum(rcvByteArray)
            val msg = readMsg(rcvByteArray, rcvPacket.length)
            rcvInfo = "displayId=$displayId , seq=$seq , msg=$msg"
            callback?.onResult(true, msg)
            paxCallback.onReceiveMsg(displayId, msg)
        } else {
            callback?.onResult(false, "invalid format")
        }
        paxCallback.paxLog("rcv", "ipInfo=$rcvIpInfo , rcvInfo=$rcvInfo")
    }

    private val centerHost: String by lazy {
        "172.26.200.2" // MPC
//        "172.26.200.3" // HPC
    }

    private val soTimeout = 5_000
    private val bufferSize = 4096

    private val centerPort: Int by lazy {
        10086
    }

    private val clientPort:Int by lazy {
        10087
    }

    private val displayId: Int by lazy {
        1
    }

    private var curSeqNum: Int = 0
    private fun buildSeqNum(): Int {
        var seq = curSeqNum + 1
        if (seq >= Int.MAX_VALUE) {
            seq = 0
        }
        curSeqNum = seq
        return seq
    }
}