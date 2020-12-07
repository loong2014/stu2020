package com.sunny.family.service

import android.app.IntentService
import android.content.Intent
import com.sunny.lib.utils.SunLog

/**
 * Created by zhangxin17 on 2020/12/10
 */
class SunIntentService : IntentService("sunIntentService") {
    companion object {
        private const val TAG = "SunIntentService"
    }

    /**
     * 处理耗时任务
     */
    override fun onHandleIntent(intent: Intent?) {
        SunLog.i(TAG, "onHandleIntent")

    }

    override fun onDestroy() {
        super.onDestroy()
        SunLog.i(TAG, "onDestroy")
    }
}