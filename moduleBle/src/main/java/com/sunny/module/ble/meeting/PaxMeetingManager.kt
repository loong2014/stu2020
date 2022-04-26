package com.ff.iai.paxlauncher.meeting

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.sunny.lib.base.utils.ContextProvider
import com.sunny.lib.base.utils.SystemUtils
import com.sunny.module.ble.meeting.PaxCalendarInfo
import timber.log.Timber

/**
 * Meeting 管理
 */
object PaxMeetingManager {

    private const val ZOOM_PKG = "us.zoom.videomeetings"

    private val _meetingInfo = MutableLiveData<PaxCalendarInfo?>()

    /**
     * Meeting 桌面可监听此值的变化
     */
    val meetingInfo: LiveData<PaxCalendarInfo?> = _meetingInfo

    /**
     * 收到来自BLE的会议信息
     */
    fun onGetBleMeetingInfo(byteArray: ByteArray?) {
        Timber.i("onGetBleMeetingInfo size :${byteArray?.size}")

        if (byteArray == null) {
            // 没有日历信息
            _meetingInfo.postValue(null)
            return
        }

        val calendarInfo =
            try {
                Gson().fromJson(String(byteArray), PaxCalendarInfo::class.java)
            } catch (e: Exception) {
                null
            }

        if (calendarInfo == null) {
            // 没有日历信息
            _meetingInfo.postValue(null)
            return
        }

        calendarInfo.events?.forEachIndexed { index, event ->
            Timber.i("onGetBleMeetingInfo($index) :$event")
        }
        _meetingInfo.postValue(calendarInfo)
    }


    /**
     * 启动zoom
     */
    fun startZoomMeeting(uri: Uri): Boolean {
        val pkgNameZoom = ZOOM_PKG
        val zoomJoinActivity = "com.zipow.videobox.JoinByURLActivity"

        val context = ContextProvider.appContext

        var hasError = true
        if (SystemUtils.isAppInstalled(context, pkgNameZoom)) {
            // 如果 zoom 已安装，设置zoom的指定页面
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = uri
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.component = ComponentName(pkgNameZoom, zoomJoinActivity)
            hasError = try {
                context.startActivity(intent)
                false
            } catch (e: Exception) {
                true
            }
        } else {
            Timber.e("App(${pkgNameZoom}) not install")
        }

        // zoom 未安装，或者打开zoom失败，使用默认方式打开
        if (hasError) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = uri
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            hasError = try {
                context.startActivity(intent)
                false
            } catch (e: Exception) {
                true
            }
        }

        return !hasError
    }
}