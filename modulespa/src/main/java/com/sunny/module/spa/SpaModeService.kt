package com.sunny.module.spa

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.hardware.display.DisplayManager
import android.os.IBinder
import android.view.Display
import androidx.core.app.NotificationCompat
import timber.log.Timber

class SpaModeService : Service() {

    companion object {
        const val INNER_ON = "android.intent.action.SUNNY_SPA_ON"
        const val INNER_OFF = "android.intent.action.SUNNY_SPA_OFF"
    }

    private var mPresentation: SunnyPresentation? = null

    private fun initPresentation() {
        if (mPresentation == null) {
            val display = getDisplay()
            mPresentation = SunnyPresentation(this, display)
            mPresentation?.setCallBack(object : PresentationCallBack {
                override fun onShown() {
                    Timber.i("onShown")
                }

                override fun onDismiss() {
                    Timber.i("onDismiss")
                }

                override fun onComplete() {
                    Timber.i("onComplete")
                    mPresentation?.dealStop()
                }
            })
        }
    }

    private fun getDisplay(): Display {
        val dm = this.getSystemService(DISPLAY_SERVICE) as DisplayManager

//        val count = dm.displays?.size ?: 0
//        Timber.i("displayCount : $count")
//        dm.displays?.forEachIndexed { index, display ->
//            Timber.i("$index >>> $display")
//        }

        val displayId = Display.DEFAULT_DISPLAY // for RSD
        val display = dm.getDisplay(displayId)
        Timber.i("getDisplay : $displayId  >>>  $display")
        return display
    }

    override fun onCreate() {
        super.onCreate()
        Timber.i("onCreate")
        startNotification()
    }

    private fun startNotification() {
        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channelId = 1
        val channel = NotificationChannel(
            channelId.toString(), "SunnySpa1",
            NotificationManager.IMPORTANCE_NONE
        )
        nm.createNotificationChannel(channel)
        val notification = NotificationCompat.Builder(this, channelId.toString())
            .build()

        Timber.i("startForeground($channelId)")
        startForeground(channelId, notification)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Timber.i("onStartCommand")
        dealStartForeground()
        initReceiver()
        initPresentation()
        return START_STICKY
    }

    private fun dealStartForeground() {
        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channelId = 2
        val channel = NotificationChannel(
            channelId.toString(), "SunnySpa2",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            enableLights(true)
            lightColor = Color.RED
            setShowBadge(true)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }
        nm.createNotificationChannel(channel)
        val notification = NotificationCompat.Builder(this, channelId.toString())
            .build()

        Timber.i("startForeground($channelId)")
        startForeground(2, notification)
    }

    private fun initReceiver() {
        val innerFilter = IntentFilter()
        innerFilter.addAction(INNER_ON)
        innerFilter.addAction(INNER_OFF)
        registerReceiver(innerReceiver, innerFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy")
        unregisterReceiver(innerReceiver)
        mPresentation?.dealStop()
        mPresentation = null
    }

    private val innerReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            Timber.i("onReceive : $action")
            mPresentation?.run {
                if (INNER_ON == action) {
                    dealStart()
                } else if (INNER_OFF == action) {
                    dealStop()
                }
            }
        }
    }
}