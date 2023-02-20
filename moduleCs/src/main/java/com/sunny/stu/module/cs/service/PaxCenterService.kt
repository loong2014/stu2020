package com.sunny.stu.module.cs.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.sunny.lib.common.mvvm.safeSet
import com.sunny.stu.module.cs.*
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketException
import java.util.concurrent.Executors

class PaxCenterService : Service() {
    val mExecutorService = Executors.newCachedThreadPool()

    private var isRcvRunning = false
    private var rcvThread: RcvThread? = null
    private var rcvSocket: DatagramSocket? = null

    private fun dealStartServer() {
        log("dealStartServer :$rcvSocket")
        if (rcvSocket != null) return

        try {
            rcvSocket = DatagramSocket(PORT)
            showStatusInfo("open rcvSocket :$rcvSocket")
        } catch (e: SocketException) {
            rcvSocket = null
            showStatusInfo("open rcvSocket error :$e")
        }

        rcvThread = RcvThread()
        rcvThread?.start()
    }

    inner class RcvThread : Thread() {
        override fun run() {
            isRcvRunning = true
            while (true) {
                if (isRcvRunning) {
                    val byteArray = ByteArray(4096)
                    val rcvPacket = DatagramPacket(byteArray, byteArray.size)
                    try {
                        rcvSocket?.receive(rcvPacket)
                        val msg = parseRcvDateByteArray(byteArray, rcvPacket.length)
                        val info = rcvPacket.socketAddress.toString().substring(1) + ":" + msg
                        showRcvMsg(info)
                        dealRcvMsg(msg)
                    } catch (e: Exception) {
                        showStatusInfo("open rcv error :$e")
                    }
                }
            }
        }
    }

    private fun parseRcvDateByteArray(byteArray: ByteArray, len: Int): String? {
        if (isValid(byteArray, len)) {
            val displayId = readDisplayId(byteArray)

            val seq = readSeqNum(byteArray)

            val msg = readMsg(byteArray, len)
            showStatusInfo("displayId=$displayId , seq=$seq , msg=$msg")
            return msg
        }
        return null
    }

    private fun dealRcvMsg(msg: String?) {
        if (msg == null) {
            showRcvMsg("error format")
            return
        }

        val rcvMsg = if (msg == "getCenterInfo") {
            "hello client, my name is Sunny"
        } else {
            "ok"
        }
        val msgPacket = DatagramPacket(ByteArray(0), 0, InetAddress.getByName(HOST_HPC), PORT)
        val socket = DatagramSocket()
        try {
            val seq = getSeqNum()
            msgPacket.data = buildSendDataByteArray(getDisplayId(), seq, rcvMsg)
            socket.send(msgPacket)
        } catch (e: Exception) {
            showStatusInfo("SendTask error :$e")
        } finally {
            socket.close()
        }

//            mExecutorService.execute(SendTask("hello client, my name is Sunny"))
    }

    inner class SendTask(private val msg: String) : Runnable {
        override fun run() {
            showSendMsg(msg)
            val msgPacket = DatagramPacket(ByteArray(0), 0, InetAddress.getByName(HOST_HPC), PORT)
            val socket = DatagramSocket()
            try {
                val seq = getSeqNum()
                msgPacket.data = buildSendDataByteArray(getDisplayId(), seq, msg)
                socket.send(msgPacket)
            } catch (e: Exception) {
                showStatusInfo("SendTask error :$e")
            } finally {
                socket.close()
            }
        }
    }

    private fun dealStopServer() {
        isRcvRunning = false
        rcvSocket?.close()
        rcvThread?.interrupt()
    }

    override fun onCreate() {
        log("onCreate")
        dealStartServer()
    }

    override fun onBind(intent: Intent?): IBinder? {
        log("onBind")
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand")
        dealStartServer()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        log("onDestroy")
        dealStopServer()
        super.onDestroy()
    }

    private fun showStatusInfo(msg: String?) {
        log(msg)
        serviceStatusInfoLiveData.safeSet(msg)
    }

    private fun showSendMsg(msg: String?) {
        log(msg)
        serviceSendMsgLiveData.safeSet(msg)
    }

    private fun showRcvMsg(msg: String?) {
        log(msg)
        serviceRcvMsgLiveData.safeSet(msg)
    }

    private fun log(msg: String?) {
        Log.i("Debug-Center", "$msg")
    }
}