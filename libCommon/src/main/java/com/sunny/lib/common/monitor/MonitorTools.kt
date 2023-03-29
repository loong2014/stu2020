package com.sunny.lib.common.monitor;

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.os.SystemClock
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.sunny.lib.common.R
import com.sunny.lib.common.base.LibCoreConfig
import com.sunny.lib.common.utils.setSafeClickListener
import timber.log.Timber
import java.util.*

@SuppressLint("StaticFieldLeak")
object MonitorTools {

    var isMonitorOpen = false
    var monitorView: View? = null
    var amazonTipView: TextView? = null
    var flowTipView: TextView? = null
    var paxTipView: TextView? = null
    var httpTipView: TextView? = null

    var monitorRunning = false
    var monitorTask: PaxMonitorTask? = null

    private val httpTimeLruCache = LinkedList<String>()

    fun updateHttpTime(useTime: Long, size: Long = 0, path: String = "") {
        val info = "${useTime}ms >>> $path($size-byte)"
        Timber.i("PaxHttp >>> $info")

        httpTimeLruCache.addFirst(info)
        if (httpTimeLruCache.size > 10) {
            httpTimeLruCache.removeLast()
        }

        if (isMonitorOpen && monitorView != null) {
            val sb = StringBuilder()
            for (time in httpTimeLruCache) {
                sb.append("\n$time")
            }

            LibCoreConfig.mainHandler.post {
                httpTipView?.text = sb.toString()
            }
        }
    }

    fun showMonitorDialog() {
        if (monitorView != null) {
            return
        }
        isMonitorOpen = true
        val context = LibCoreConfig.appContext
        val view = View.inflate(context, R.layout.dialog_http_monitor_tip, null)

        val windowManager = getWindowManager()
        val layoutParams = WindowManager.LayoutParams(
            LibCoreConfig.resources.getDimensionPixelSize(R.dimen.common_400),
            LibCoreConfig.resources.getDimensionPixelSize(R.dimen.common_510),
            0, 0, PixelFormat.TRANSPARENT
        )
        layoutParams.flags =
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        layoutParams.gravity = Gravity.RIGHT

        windowManager.addView(view, layoutParams)

        monitorView = view
        flowTipView = view.findViewById(R.id.tv_tip_flow)
        amazonTipView = view.findViewById(R.id.tv_tip_amazon)
        paxTipView = view.findViewById(R.id.tv_tip_pax)
        httpTipView = view.findViewById(R.id.tv_tip_http)

        startMonitor()

        view.findViewById<View>(R.id.btn_close).setSafeClickListener {
            hideMonitorDialog()
        }
    }

    fun hideMonitorDialog() {
        isMonitorOpen = false
        stopMonitor()
        flowTipView = null
        amazonTipView = null
        paxTipView = null
        httpTipView = null
        if (monitorView != null) {
            val windowManager = getWindowManager()
            windowManager.removeViewImmediate(monitorView)
            monitorView = null
        }
    }

    private fun startMonitor() {
        monitorTask = PaxMonitorTask()
        monitorTask?.start()
        updateHttpTime(-1)
        FlowRateMonitor.startMonitor()
    }

    private fun stopMonitor() {
        monitorRunning = false
        monitorTask?.interrupt()
        FlowRateMonitor.stopMonitor()
    }

    class PaxMonitorTask : Thread() {
        override fun run() {
            monitorRunning = true
            while (monitorRunning) {
                val startTime = SystemClock.elapsedRealtime()
                val amazonMs = PingUtil.getRTT("https://www.amazon.com")
                val paxMs = PingUtil.getRTT("https://www.baidu.com")
                Timber.i("ping amazonMs=$amazonMs , paxMs=$paxMs")
                LibCoreConfig.mainHandler.post {
                    flowTipView?.text = FlowRateMonitor.flowRateInfo()
                    amazonTipView?.text = "$amazonMs ms : www.amazon.com"
                    paxTipView?.text = "$paxMs ms : www.baidu.com"
                }
                val sleepTime = startTime + 2_000 - SystemClock.elapsedRealtime()
                if (sleepTime > 0) {
                    try {
                        sleep(sleepTime)
                    } catch (e: Exception) {
                    }
                }
            }
        }
    }

    private fun getWindowManager(): WindowManager {
        return LibCoreConfig.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }
}