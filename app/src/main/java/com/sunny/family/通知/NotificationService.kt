package com.sunny.family.通知

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.sunny.lib.router.RouterConstant
import com.sunny.lib.utils.SunLog

/**
 * Created by zhangxin17 on 2020/12/9
 */

@Route(path = RouterConstant.ServiceNotification)
class NotificationService : NotifyService {

    companion object {
        private const val TAG = "NotifyService"
    }

    override fun showNotify(title: String) {
        SunLog.i(TAG, "showNotify $title")
    }

    override fun init(context: Context?) {
        SunLog.i(TAG, "init")
    }

}