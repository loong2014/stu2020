package com.sunny.module.login

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.sunny.lib.common.base.BaseActivity
import com.sunny.lib.common.router.RouterConstant
import kotlinx.android.synthetic.main.login_activity_demo.*

/**
 * Created by zhangxin17 on 2020/12/29
 */
@Route(path = RouterConstant.Login.PageDemo)
class LoginDemoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.login_activity_demo)

        initView()
    }

    private fun initView() {
        top_bar.setMiddleName("模块：Login")
        top_bar.setOnBackBtnClickListener(View.OnClickListener {
            doExitActivity()
        })
    }
}
