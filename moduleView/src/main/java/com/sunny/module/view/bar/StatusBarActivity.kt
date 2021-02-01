package com.sunny.module.view.bar

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.sunny.lib.common.base.BaseActivity
import com.sunny.lib.common.router.RouterConstant
import com.sunny.module.view.R
import kotlinx.android.synthetic.main.view_act_status_bar.*

@Route(path = RouterConstant.View.PageStatusBar)
class StatusBarActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        SystemBarTintManager.fullscreen(this) // 全屏

        SystemBarTintManager.translucentStatus(this) // 透明状态栏

        SystemBarTintManager.setStatusContentColor(this, true) // 设置状态栏内容颜色

        setContentView(R.layout.view_act_status_bar)

        SystemBarTintManager.setViewPaddingTopStatusBar(this, titleBar)

    }


}