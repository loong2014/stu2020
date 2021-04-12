package com.sunny.module.stu

import android.content.Intent
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.sunny.lib.common.base.BaseActivity
import com.sunny.lib.common.router.RouterConstant
import com.sunny.lib.common.router.RouterJump
import kotlinx.android.synthetic.main.stu_activity_demo.*

@Route(path = RouterConstant.Stu.PageDemo)
class StuDemoActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stu_activity_demo)

        initView()
    }

    private fun initView() {

        btnJumpThreadStu.setOnClickListener {
            RouterJump.navigation(RouterConstant.Stu.PageThread)
        }
    }
}