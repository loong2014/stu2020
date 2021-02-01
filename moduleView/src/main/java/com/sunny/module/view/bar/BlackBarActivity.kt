package com.sunny.module.view.bar

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.sunny.lib.common.base.BaseActivity
import com.sunny.lib.common.router.RouterConstant
import com.sunny.module.view.R

@Route(path = RouterConstant.View.PageBarBlack)
class BlackBarActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarTintManager.translucentStatus(this) // 透明状态栏

        SystemBarTintManager.setStatusContentColor(this, false) // 设置状态栏内容颜色
//        // 更改状态栏颜色
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.statusBarColor = ResUtils.getColor(R.color.black)


//
//        val decor: View = activity.getWindow().getDecorView()
//        if (dark) {
//            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
//        } else {
//            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
//        }

        setContentView(R.layout.view_act_bar_black)
    }
}