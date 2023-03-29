package com.sunny.lib.common.monitor;

import android.net.TrafficStats
import android.os.SystemClock
import com.sunny.lib.common.base.LibCoreConfig
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.NumberFormat

object FlowRateMonitor {

    // 上次上下行流量
    var mLastUp: Long = 0L
    var mLastDown: Long = 0L

    // 当前的上下行速率
    var mCurDifUp: Long = 0L
    var mCurDifDown: Long = 0L

    //
    var tipUp = ""
    var tipDown = ""

    var isRunning = false
    var DURATION_TIME = 1000

    fun flowRateInfo(): String {
        return if (isRunning) {
            "Rate :up($tipUp)  down($tipDown)"
        } else {
            "Rate :None"
        }
    }

    fun stopMonitor() {
        Timber.i("stopMonitor")
        isRunning = false
        stopSignalStrengthsListener()
    }

    fun startMonitor() {
        Timber.i("startMonitor :isRunning=$isRunning")
        isRunning = true
        LibCoreConfig.ioApplicationScope.launch {
            while (isRunning) {
                val beginTime = SystemClock.elapsedRealtime()
                doFlowLogic()
                val delayTime = DURATION_TIME - (SystemClock.elapsedRealtime() - beginTime)
                if (delayTime > 0) {
                    delay(delayTime)
                }
            }
        }
        startSignalStrengthsListener()
    }

    suspend fun doFlowLogic() {
        val curUp = TrafficStats.getTotalTxBytes()
        val curDown = TrafficStats.getTotalRxBytes()
        mCurDifUp = curUp - mLastUp
        mCurDifDown = curDown - mLastDown
        mLastUp = curUp
        mLastDown = curDown

        // 上传速率
        tipUp = convert2Tip(mCurDifUp)
        tipDown = convert2Tip(mCurDifDown)

        Timber.i(flowRateInfo())
    }

//    private var telephonySignalStrengthsListener: TelephonySignalStrengthsListener? = null
//
//    class TelephonySignalStrengthsListener : TelephonyCallback(),
//        TelephonyCallback.SignalStrengthsListener {
//        override fun onSignalStrengthsChanged(signalStrength: SignalStrength) {
//            Timber.i("onSignalStrengthsChanged-Q :${signalStrength.level}")
//        }
//    }
//
//    private val executor by lazy {
//        ThreadPoolExecutor(
//            1, 3,
//            0L, TimeUnit.MILLISECONDS,
//            LinkedBlockingQueue()
//        )
//    }

    private fun startSignalStrengthsListener() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            val tm = LibCoreConfig.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//            val listener = TelephonySignalStrengthsListener()
//            telephonySignalStrengthsListener = listener
//            try {
//                tm.registerTelephonyCallback(executor, listener)
//            } catch (e: Exception) {
//            }
//        } else {
//            val tm = LibCoreConfig.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//            tm.listen(object : PhoneStateListener() {
//                override fun onSignalStrengthsChanged(signalStrength: SignalStrength?) {
//                    Timber.i("onSignalStrengthsChanged :${signalStrength?.level}")
//                }
//            }, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS)
//        }
    }

    private fun stopSignalStrengthsListener() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            val tm = LibCoreConfig.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//            telephonySignalStrengthsListener?.let { listener ->
//                try {
//                    tm.unregisterTelephonyCallback(listener)
//                } catch (e: Exception) {
//                }
//            }
//        }
    }

    private fun convert2Tip(rate: Long): String {
        return if (rate > 1000_000) {
            val format = NumberFormat.getInstance()
            format.maximumFractionDigits = 1
            format.format(rate.toFloat() / 1_000_000) + " MB"
        } else if (rate >= 1000) {
            val format = NumberFormat.getInstance()
            format.maximumFractionDigits = 1
            format.format(rate.toFloat() / 1000) + " KB"
        } else {
            "$rate B"
        }
    }
}