package com.sunny.module.home

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.sunny.lib.common.base.BaseActivity
import com.sunny.lib.common.router.RouterConstant
import com.sunny.lib.common.router.RouterJump
import kotlinx.android.synthetic.main.home_activity_demo.*

/**
 * Created by zhangxin17 on 2020/12/29
 */
@Route(path = RouterConstant.Home.PageDemo)
class HomeDemoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity_demo)

        initView()
    }

    private fun initView() {
        top_bar.setMiddleName("模块：首页")
        top_bar.setOnBackBtnClickListener(View.OnClickListener {
            doExitActivity()
        })

        btnJumpHome.setOnClickListener {
            RouterJump.navigation(RouterConstant.Home.PageHome)
        }
    }
}