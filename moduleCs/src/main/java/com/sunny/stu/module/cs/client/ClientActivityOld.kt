//package com.sunny.stu.module.cs.client
//
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import androidx.appcompat.app.AppCompatActivity
//import com.sunny.stu.module.cs.*
//import kotlinx.android.synthetic.main.activity_service.*
//import java.io.*
//import java.net.Socket
//import java.util.concurrent.Executors
//
//class ClientActivityOld : AppCompatActivity() {
//
//    val mExecutorService = Executors.newCachedThreadPool()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_client)
//
//        initObserve()
//    }
//
//    fun clickOpen(view: View?) {
//        if (mSocket != null) {
//            showClientInfo("already open :$mSocket")
//            return
//        }
//        mExecutorService.execute(connectTask)
//    }
//
//    fun clickClose(view: View?) {
//        if (mSocket?.isClosed == true) {
//            showClientInfo("already close :$mSocket")
//            return
//        }
//        mExecutorService.execute(disconnectTask)
//    }
//
//    fun clickSend(view: View?) {
//        dealSendMsg("hello server")
//    }
//
//    var mSocket: Socket? = null
//    var mWriter: PrintWriter? = null
//    var mReader: BufferedReader? = null
//
//    private val connectTask = Runnable {
//        try {
//            val socket = Socket(HOST_MPC, PORT)
//            socket.soTimeout = 60_000
//            mSocket = socket
//            showClientInfo("connected :$socket")
//
//            mWriter = PrintWriter(
//                BufferedWriter(OutputStreamWriter(socket.getOutputStream(), "UTF-8")),
//                true
//            )
//            mReader = BufferedReader(InputStreamReader(socket.getInputStream(), "UTF-8"))
//
//            dealRcvMsg()
//        } catch (e: Exception) {
//            showClientInfo("connectTask error :$e")
//        }
//    }
//    private val disconnectTask = Runnable {
//        if (mSocket?.isClosed == false) {
//            dealSendMsg("close")
////            doClose()
//        }
//    }
//
//    private fun doClose() {
//        mWriter?.close()
//        mReader?.close()
//        mSocket?.close()
//        mSocket = null
//    }
//
//    private fun dealRcvMsg() {
//        try {
//            while (true) {
//                val msg = mReader?.readLine()
//                showRcvMsg(msg)
//                if (msg?.contains("close") == true) {
//                    mExecutorService.execute {
//                        doClose()
//                    }
//                    break
//                }
//            }
//        } catch (e: IOException) {
//            showClientInfo("readLine error :$e")
//        }
//    }
//
//    private fun dealSendMsg(msg: String) {
//        showLog("dealSendMsg :$msg")
//        mExecutorService.execute {
//            mWriter?.println("(${System.currentTimeMillis()})$msg")
//        }
//    }
//
//    private fun showRcvMsg(msg: String?) {
//        runOnUiThread {
//            val info = "${System.currentTimeMillis()}: $msg"
//            showLog(info)
//            tv_rcv_info.text = info
//        }
//    }
//
//    private fun showClientInfo(msg: String?) {
//        runOnUiThread {
//            val info = "$msg"
//            showLog(info)
//            tv_client_info.text = info
//        }
//    }
//
//    private fun showLog(msg: String?) {
//        Log.i("Debug", "c->$msg")
//    }
//
//    private fun initObserve() {
//        clientStatusInfoLiveData.observe(this) {
//            val info = "${System.currentTimeMillis()} status :$it"
//            tv_server_info.text = info
//        }
//
//        clientClientInfoLiveData.observe(this) {
//            tv_client_info.text = it
//        }
//
//        clientSendMsgLiveData.observe(this) {
//            val info = "${System.currentTimeMillis()} send :$it"
//            tv_send_info.text = info
//        }
//
//        clientRcvMsgLiveData.observe(this) {
//            val info = "${System.currentTimeMillis()} rcv :$it"
//            tv_rcv_info.text = info
//        }
//    }
//}