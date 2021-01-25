package com.sunny.module.account.work

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.sunny.lib.base.log.SunLog

/**
 * Created by zhangxin17 on 2021/1/25
 * 周期性定时任务
 */
class AccountWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    companion object {
        const val TAG = "AccountWorker"
    }

    /**
     * 实现具体的后台业务逻辑代码
     */
    override fun doWork(): Result {
        SunLog.i(TAG, "doWork")
        return Result.success()
    }

}