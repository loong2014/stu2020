package com.sunny.module.account

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.sunny.lib.common.base.BaseActivity
import com.sunny.lib.common.router.RouterConstant
import com.sunny.lib.common.router.RouterJump
import kotlinx.android.synthetic.main.login_activity_demo.*

/**
 * Created by zhangxin17 on 2020/12/29
 */
@Route(path = RouterConstant.Account.PageDemo)
class AccountDemoActivity : BaseActivity() {

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

        btnLogin.setOnClickListener {
            RouterJump.navigation(RouterConstant.Account.PageLogin)
        }
    }
}
