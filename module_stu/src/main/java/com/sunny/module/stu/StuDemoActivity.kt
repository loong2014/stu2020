package com.sunny.module.stu

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.sunny.lib.common.base.BaseActivity
import com.sunny.lib.common.router.RouterConstant

@Route(path = RouterConstant.Stu.PageDemo)
class StuDemoActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stu_activity_demo)
    }
}