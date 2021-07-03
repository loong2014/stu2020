package com.sunny.module.stu.book

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.sunny.lib.common.base.BaseActivity
import com.sunny.lib.common.router.RouterConstant
import com.sunny.module.stu.R

@Route(path = RouterConstant.Stu.PageBook01)
class StuBook01Activity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stu_act_book_01)
    }
}