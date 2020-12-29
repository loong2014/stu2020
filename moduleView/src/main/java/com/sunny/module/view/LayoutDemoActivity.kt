package com.sunny.module.view

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.sunny.lib.common.base.BaseActivity
import com.sunny.lib.common.router.RouterConstant
import kotlinx.android.synthetic.main.view_activity_demo.*

/**
 * Created by zhangxin17 on 2020/12/29
 */
@Route(path = RouterConstant.View.PageDemo)
class LayoutDemoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_activity_demo)

        initView()
    }

    private fun initView() {
        top_bar.setMiddleName("模块：布局")
        top_bar.setOnBackBtnClickListener(View.OnClickListener {
            doExitActivity()
        })
    }
}