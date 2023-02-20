package com.sunny.stu.module.cs.client

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sunny.stu.module.cs.PaxConnectModel
import com.sunny.stu.module.cs.PaxReceiveCallback
import com.sunny.stu.module.cs.PaxSendCallback
import com.sunny.stu.module.cs.R
import kotlinx.android.synthetic.main.activity_client.*
import kotlinx.android.synthetic.main.activity_service.tv_rcv_info
import kotlinx.android.synthetic.main.activity_service.tv_send_info


class ClientActivity : AppCompatActivity(), PaxReceiveCallback {

    val paxConnectModel = PaxConnectModel(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client)
    }

    fun clickOpenRev(view: View?) {
        paxConnectModel.openReceive()
    }

    fun clickCloseRcv(view: View?) {
        paxConnectModel.closeReceive()
    }

    var needGet = false
    fun clickSend(view: View?) {
        needGet = !needGet
        val msg = if (needGet) {
            "getCenterInfo"
        } else {
            "hello center"
        }
        showStatusInfo(msg)
        paxConnectModel.doSendMsgToCenter(msg, object : PaxSendCallback {

            override fun onError(e: Throwable) {
                showStatusInfo("error :$e")
            }

            override fun onResult(succeed: Boolean, result: String?) {
                showRcvInfo("succeed=$succeed , result=$result")
            }
        })
    }

    override fun onReceiveMsg(displayId: Int, msg: String) {
        showRcvInfo("displayId=$displayId , msg=$msg")
    }

    override fun paxLog(tag: String, msg: String) {
        when (tag) {
            "status" -> showStatusInfo(msg)
            "rcv" -> showRcvInfo(msg)
            "send" -> showSendInfo(msg)
            else -> log(msg)
        }
    }
//    var rcvSocket: DatagramSocket? = null
//
//    // UDP接收
//    private fun funOpenUDPReceive() {
//        log("try open rcv :$rcvSocket")
//        if (rcvSocket != null) return
//
//        val byteArray = ByteArray(4096)
//        val rcvPacket = DatagramPacket(byteArray, byteArray.size)
//        try {
//            val datagramSocket = DatagramSocket(PORT)
//            rcvSocket = datagramSocket
//            showStatusInfo("open rcv :$rcvSocket")
//            while (true) {
//                datagramSocket.receive(rcvPacket)
//                parseRcvPacket(rcvPacket)
//            }
//        } catch (e: Exception) {
//            showStatusInfo("open rcv error :$e")
//            rcvSocket?.close()
//        }
//    }
//
//    //关闭UDP接收
//    private fun funCloseUDPReceive() {
//        try {
//            val isConnected = rcvSocket?.isConnected
//            log("close rcv isConnected=$isConnected")
//            if (rcvSocket?.isConnected == true) {
//                rcvSocket?.close()
//                rcvSocket = null
//                showStatusInfo("close rcv")
//            }
//        } catch (e: Exception) {
//            showStatusInfo("close rcv error :$e")
//        }
//    }
//
//    inner class SendTask(private val sendMsg: String, private val callback: SendCallback? = null) :
//        Runnable {
//
//        override fun run() {
//            val sendPacket = DatagramPacket(ByteArray(0), 0, InetAddress.getByName(HOST_MPC), PORT)
//            val socket = DatagramSocket(PORT)
//            socket.soTimeout = 5_000
//            try {
//                // send
//                val sendDisplayId = getDisplayId()
//                val sendSeq = getSeqNum()
//                log("send :displayId=$sendDisplayId , seq=$sendSeq , msg=$sendMsg")
//                sendPacket.data = buildSendDataByteArray(sendDisplayId, sendSeq, sendMsg)
//                socket.send(sendPacket)
//                showSendInfo("displayId=$sendDisplayId , seq=$sendSeq , msg=$sendMsg")
//
//                // receive
//                val rcvByteArray = ByteArray(4096)
//                val rcvPacket = DatagramPacket(rcvByteArray, rcvByteArray.size)
//                socket.receive(rcvPacket)
//
//                parseRcvPacket(rcvPacket, callback)
//
//            } catch (e: Exception) {
//                showStatusInfo("SendTask error :$e")
//                callback?.onError()
//            } finally {
//                socket.close()
//            }
//        }
//    }
//
//    private fun parseRcvPacket(rcvPacket: DatagramPacket, callback: SendCallback? = null) {
//        val rcvIpInfo = rcvPacket.socketAddress.toString().substring(1)
//        var rcvInfo: String? = null
//
//        if (isValidRcvPacket(rcvPacket)) {
//            val rcvByteArray = rcvPacket.data
//            val displayId = readDisplayId(rcvByteArray)
//            val seq = readSeqNum(rcvByteArray)
//            val msg = readMsg(rcvByteArray, rcvPacket.length)
//            rcvInfo = "displayId=$displayId , seq=$seq , msg=$msg"
//            callback?.onResult(msg)
//        } else {
//            callback?.onResult(null)
//        }
//        showRcvInfo("ipInfo=$rcvIpInfo , rcvInfo=$rcvInfo")
//    }

    private fun showStatusInfo(msg: String?) {
        runOnUiThread {
            val info = "${System.currentTimeMillis()} status :$msg"
            log(info)
            tv_status_info.text = info
        }
    }

    private fun showSendInfo(msg: String?) {
        runOnUiThread {
            val info = "${System.currentTimeMillis()} send :$msg"
            log(info)
            tv_send_info.text = info
        }
    }

    private fun showRcvInfo(msg: String?) {
        runOnUiThread {
            val info = "${System.currentTimeMillis()} rcv :$msg"
            log(info)
            tv_rcv_info.text = info
        }
    }

    private fun log(msg: String?) {
        Log.i("Debug-Client", "$msg")
    }

}