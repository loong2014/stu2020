package com.sunny.module.account.work

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

/**
 * Created by zhangxin17 on 2021/1/25
 */
class WorkerHelper {

    // 单次后台任务
    val request = OneTimeWorkRequest.Builder(AccountWorker::class.java)
            .setInitialDelay(5, TimeUnit.MINUTES) // 延时5分钟后执行
            .addTag("account") // 给后台任务添加一个标签
            .build()

    // 周期性后台任务，时间必须大于15分钟
    val requestLoop = PeriodicWorkRequest.Builder(
            AccountWorker::class.java, 30, TimeUnit.MINUTES).build()

    /**
     * 执行任务
     */
    fun startAccountWorker(context: Context, lifecycleOwner: LifecycleOwner) {
        WorkManager.getInstance(context).enqueue(request)
        WorkManager.getInstance(context).enqueue(requestLoop)

        // 链式执行任务
        WorkManager.getInstance(context)
                .beginWith(request)
                .then(request)
                .then(request)
                .enqueue()


        // 监听任务执行结果
        WorkManager.getInstance(context)
                .getWorkInfosByTagLiveData("account")
                .observe(lifecycleOwner, Observer {
                    // 重复任务返回的是一个list
                })

        // 观察requestLoop的LiveData对象
        WorkManager.getInstance(context)
                .getWorkInfoByIdLiveData(requestLoop.id)
                .observe(lifecycleOwner, Observer { workInfo: WorkInfo ->
                    if (workInfo.state == WorkInfo.State.SUCCEEDED) {

                    } else {

                    }
                })
    }

    /**
     * 取消任务
     */
    fun cancelAccountWorker(context: Context) {
        // 通过标签取消任务，会将有相同标签的任务都取消
        WorkManager.getInstance(context).cancelAllWorkByTag("account")

        WorkManager.getInstance(context).cancelWorkById(requestLoop.id) // 通过id取消任务
    }
}