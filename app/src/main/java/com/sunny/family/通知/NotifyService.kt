package com.sunny.family.通知

import com.alibaba.android.arouter.facade.template.IProvider

/**
 * Created by zhangxin17 on 2020/12/10
 */
interface NotifyService : IProvider {
    fun showNotify(title: String)
}