package com.sunny.stu.module.cs.service

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sunny.stu.module.cs.PORT
import com.sunny.stu.module.cs.R
import kotlinx.android.synthetic.main.activity_service.*
import java.io.*
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.Executors

class ServiceActivity : AppCompatActivity() {

    val mExecutorService = Executors.newCachedThreadPool()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)
    }

    fun clickOpen(view: View?) {
        if (mServerSocket != null) {
            showServerInfo("already open :$mServerSocket")
            return
        }
        mExecutorService.execute(openServiceTask)
    }

    fun clickClose(view: View?) {
        if (mServerSocket?.isClosed == true) {
            showServerInfo("already close :$mServerSocket")
            return
        }
        mExecutorService.execute(closeServiceTask)
    }

    fun clickSend(view: View?) {
        mExecutorService.execute {
            mClientList.forEach {
                it.sendMsg("hello client")
            }
        }
    }

    private val mClientList = mutableListOf<ClientService>()

    private var mServerSocket: ServerSocket? = null

    private val openServiceTask = Runnable {
        try {
            val ss = ServerSocket(PORT)
            mServerSocket = ss
            showServerInfo("server opened :$mServerSocket")
            while (true) {
                val client = ss.accept()
                showLog("accept client :$client")
                mExecutorService.execute(ClientService(client))
            }
        } catch (e: Exception) {
            showServerInfo("openService error :$e")
        }
    }

    private val closeServiceTask = Runnable {
        if (mServerSocket != null) {
            showServerInfo("close server :$mServerSocket")
            mClientList.forEach {
                it.closeConnected()
            }
            mClientList.clear()
            mServerSocket?.close()
            mServerSocket = null
        }
    }

    inner class ClientService(val socket: Socket) : Runnable {

        var mWriter: PrintWriter? = null
        var mReader: BufferedReader? = null

        var running = false

        init {
            try {
                mWriter = PrintWriter(
                    BufferedWriter(OutputStreamWriter(socket.getOutputStream(), "UTF-8")),
                    true
                )
                mReader = BufferedReader(InputStreamReader(socket.getInputStream(), "UTF-8"))

                sendMsg("Welcome :$socket")

                mClientList.add(this)
                showClientInfo()
            } catch (e: Exception) {
                showLog("ClientService e:$e")
            }
        }

        fun closeConnected() {
            sendMsg("close by server")
            running = false
        }

        fun sendMsg(msg: String) {
            showLog("sendMsg($msg) by $socket")
            try {
                mWriter?.println("(${System.currentTimeMillis()})$msg")
            } catch (e: Exception) {
                showServerInfo("sendMsg error :$e")
            }
        }

        override fun run() {
            running = true
            try {
                showLog("while-start :$socket")
                while (running) {
                    val msg = mReader?.readLine()
                    showRcvMsg(msg)
                    if (msg?.contains("close") == true) {
                        sendMsg("close succeed")
                        break
                    }
                }
                mClientList.remove(this)
                showLog("while-end :$socket")
                doRelease()
            } catch (e: IOException) {
                showServerInfo("readLine error :$e")
            }
        }

        private fun doRelease() {
            try {
                mReader?.close()
                mWriter?.close()
                socket.close()
            } catch (e: IOException) {
                showServerInfo("doRelease error :$e")
            }
        }
    }

    private fun showClientInfo() {
        runOnUiThread {
            val sb = StringBuilder()
            sb.append("count(${mClientList.size})")
            mClientList.forEach {
                sb.append("\n$it")
            }

            val info = sb.toString()
            showLog(info)
            tv_client_info.text = info
        }
    }

    private fun showRcvMsg(msg: String?) {
        runOnUiThread {
            val info = "${System.currentTimeMillis()}: $msg"
            showLog(info)
            tv_rcv_info.text = info
        }
    }

    private fun showServerInfo(msg: String?) {
        runOnUiThread {
            val info = "$msg"
            showLog(info)
            tv_server_info.text = info
        }
    }

    private fun showLog(msg: String?) {
        Log.i("Debug", "s->$msg")
    }
}