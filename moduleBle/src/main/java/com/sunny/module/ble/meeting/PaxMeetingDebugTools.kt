package com.sunny.module.ble.meeting

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sunny.lib.base.utils.ContextProvider
import com.sunny.module.ble.IosMeetingInfo
import com.sunny.module.ble.master.PaxZoomInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

internal object PaxMeetingDebugTools {

    fun sendMeetingInfo(context: Context, userId: Int) {
        MainScope().launch(Dispatchers.IO) {
            val meetingInfo = buildMeetingInfo(context)

//            val data = Bundle()
//            data.putByteArray(PaxUserConfig.EXT_MEETING_INFO, meetingInfo)
//
//            val intent = Intent(PaxUserConfig.ACTION_MEETING)
//            intent.putExtras(data)
//
//            val userHandle = PaxUserConfig.getUserHandle(userId)
//            LibCoreConfig.sendBroadcastAsUser(intent, userHandle)
        }
    }

    fun buildMeetingInfo(context: Context? = null): ByteArray {
        val bContext = context ?: ContextProvider.appContext
        val json = getMeetingInfoFromAssets(bContext, "meeting_info.json")
        return json.toByteArray()
    }


    private fun buildZoomInfoByMeetingInfo(info: IosMeetingInfo): PaxZoomInfo? {
        if (info.os != "ios") return null
        return PaxZoomInfo(info.title, info.url, info.notes, info.startDate, info.endDate)
    }

    private fun getIosMeetingInfo(context: Context): List<IosMeetingInfo>? {
        val json = getMeetingInfoFromAssets(context, "meeting_list.json")
        return Gson().fromJson(json, object : TypeToken<ArrayList<IosMeetingInfo>>() {}.type)
    }

    private fun getMeetingInfoFromAssets(context: Context, name: String): String {
        val input = context.assets.open(name)
        BufferedReader(InputStreamReader(input)).use {
            val sb = StringBuilder()
            var line: String
            while (true) {
                line = it.readLine() ?: break
                sb.append(line)
            }
            return sb.toString()
        }
    }
}