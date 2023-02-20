//package com.sunny.stu.module.cs.service
//
//import android.app.Service
//import android.content.Intent
//import android.os.Handler
//import android.os.HandlerThread
//import android.os.IBinder
//import android.util.Log
//import com.sunny.lib.common.mvvm.safeSet
//import com.sunny.stu.module.cs.*
//import java.io.*
//import java.net.ServerSocket
//import java.net.Socket
//import java.util.concurrent.Executors
//
//class PaxCenterServiceTCP : Service() {
//
//    private val mExecutorService = Executors.newCachedThreadPool()
//
//    private var mWorkHandler: Handler? = null
//
//    private var mServerSocket: ServerSocket? = null
//    private var isServerRunning = false
//
//    private val mClientList = mutableListOf<ClientService>()
//
//    private fun dealStartServer(delay: Boolean = false) {
//        showStatusInfo("dealStartServer(delay=$delay) isServerRunning=$isServerRunning  :$mServerSocket")
//        if (isServerRunning) return
//        isServerRunning = true
//
//        mWorkHandler?.removeCallbacks(startServerTask)
//        if (delay) {
//            mWorkHandler?.postDelayed(startServerTask, 5_000)
//        } else {
//            mWorkHandler?.post(startServerTask)
//        }
//    }
//
//    private fun startHeartbeat() {
//        log("startHeartbeat")
//        mWorkHandler?.removeCallbacks(heartbeatTask)
//        mWorkHandler?.postDelayed(heartbeatTask, 5_000)
//    }
//
//    private fun stopHeartbeat() {
//        log("stopHeartbeat")
//        mWorkHandler?.removeCallbacks(heartbeatTask)
//    }
//
//    private val heartbeatTask = Runnable {
//        log("send heartbeat(client count :${mClientList.size})")
//        mClientList.forEach {
//            it.sendMsg("heartbeat from center")
//        }
//    }
//
//    private val startServerTask = Runnable {
//        try {
//            mServerSocket = ServerSocket(PORT)
//            showStatusInfo("startServer($isServerRunning) mServerSocket=$mServerSocket")
//            while (isServerRunning) {
//                val client = mServerSocket?.accept()
//                dealClientConnected(client)
//                startHeartbeat()
//            }
//        } catch (e: Exception) {
//            showStatusInfo("startServer($isServerRunning) error :$e")
//            if (isServerRunning) {
//                isServerRunning = false
//                dealStartServer(true)
//            }
//        } finally {
//            doStopServer()
//
//        }
//    }
//
//    private fun dealStopServer() {
//        mClientList.forEach {
//            it.closeConnected()
//        }
//        mClientList.clear()
//        showClientInfo()
//
//        isServerRunning = false
//        doStopServer()
//    }
//
//    private fun doStopServer() {
//        stopHeartbeat()
//        try {
//            mServerSocket?.close()
//            mServerSocket = null
//        } catch (e: Exception) {
//            log("close ServerSocket error :$e")
//        }
//    }
//
//    private fun dealClientConnected(socket: Socket?) {
//        if (socket == null) return
//        showStatusInfo("dealClientConnected socket=$socket")
//        mExecutorService.execute(ClientService(socket))
//    }
//
//    inner class ClientService(private val socket: Socket) : Runnable {
//        var mWriter: PrintWriter? = null
//        var mReader: BufferedReader? = null
//
//        var running = false
//
//        init {
//            try {
//                mWriter = PrintWriter(
//                    BufferedWriter(OutputStreamWriter(socket.getOutputStream(), "UTF-8")),
//                    true
//                )
//                mReader = BufferedReader(InputStreamReader(socket.getInputStream(), "UTF-8"))
//
//                doSendMsg("Welcome :$socket")
//            } catch (e: Exception) {
//                showStatusInfo("ClientService init error :$e")
//            }
//        }
//
//        fun sendMsg(msg: String) {
//            log("sendMsg :$msg")
//            mWorkHandler?.post {
//                log("doSendMsg :$msg")
//                doSendMsg(msg)
//            }
//        }
//
//        private fun doSendMsg(msg: String) {
//            showSendMsg(msg)
//            try {
//                mWriter?.println(msg)
//            } catch (e: Exception) {
//                log("sendMsg error :$e")
//            }
//        }
//
//        private fun dealReadMsg(msg: String?): Boolean {
//            if (msg == null) return false
//            showRcvMsg(msg)
//            if (msg.contains("close")) {
//                doSendMsg("close succeed")
//                return true
//            }
//            return false
//        }
//
//        fun closeConnected() {
//            mWorkHandler?.post {
//                log("closeConnected :$socket")
//                doSendMsg("close by center")
//                running = false
//                try {
//                    mReader?.close()
//                    mWriter?.close()
//                    socket.close()
//                } catch (e: IOException) {
//                    log("doRelease error :$e")
//                }
//            }
//        }
//
//        override fun run() {
//            try {
//                mClientList.add(this)
//                showClientInfo()
//
//                running = true
//                log("run-start :$socket")
//                while (running) {
//                    val msg = mReader?.readLine()
//                    if (dealReadMsg(msg)) {
//                        break
//                    }
//                }
//                log("run-end :$socket")
//            } catch (e: IOException) {
//                log("readLine error :$e")
//            } finally {
//                mClientList.remove(this)
//                showClientInfo()
//
//                try {
//                    mReader?.close()
//                    mWriter?.close()
//                    socket.close()
//                } catch (e: IOException) {
//                    log("doRelease error :$e")
//                }
//            }
//        }
//    }
//
//    override fun onCreate() {
//        log("onCreate")
//        log("dealStartServer by onCreate")
//        val ht = HandlerThread("PaxCenterWork")
//        ht.start()
//        mWorkHandler = Handler(ht.looper)
//        dealStartServer()
//    }
//
//    override fun onBind(intent: Intent?): IBinder? {
//        log("onBind")
//        return null
//    }
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        log("onStartCommand")
//        log("dealStartServer by onStartCommand")
//        dealStartServer()
//        return super.onStartCommand(intent, flags, startId)
//    }
//
//    override fun onDestroy() {
//        log("onDestroy")
//        dealStopServer()
//        super.onDestroy()
//    }
//
//    private fun showClientInfo() {
//        val sb = StringBuilder()
//        sb.append("Client Count(${mClientList.size})")
//        mClientList.forEach {
//            sb.append("\n$it")
//        }
//        val msg = sb.toString()
//        log(msg)
//        serviceClientInfoLiveData.safeSet(msg)
//    }
//
//    private fun showStatusInfo(msg: String?) {
//        log(msg)
//        serviceStatusInfoLiveData.safeSet(msg)
//    }
//
//    private fun showSendMsg(msg: String?) {
//        log(msg)
//        serviceSendMsgLiveData.safeSet(msg)
//    }
//
//    private fun showRcvMsg(msg: String?) {
//        log(msg)
//        serviceRcvMsgLiveData.safeSet(msg)
//    }
//
//    private fun log(msg: String?) {
//        Log.i("Debug-Center", "$msg")
//    }
//}