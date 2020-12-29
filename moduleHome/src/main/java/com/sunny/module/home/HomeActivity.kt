package com.sunny.module.home

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.sunny.lib.common.base.BaseActivity
import com.sunny.lib.common.router.RouterConstant
import com.sunny.lib.common.service.IAccountService
import com.sunny.lib.common.service.ServiceFactory
import kotlinx.android.synthetic.main.home_activity_home.*

/**
 * Created by zhangxin17 on 2020/12/29
 */
@Route(path = RouterConstant.Home.PageHome)
class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity_home)

        module_name.text = "模块：Home"

        initView()
    }

    private fun initView() {

        btn_get_user_info.setOnClickListener {
            val accountService: IAccountService = ServiceFactory.getInstance().accountService
            val id = accountService.getUserId()
            val name = accountService.getUserName()

            updateTip("$name : $id")

        }

        btn_auto_login.setOnClickListener {
            val accountService: IAccountService = ServiceFactory.getInstance().accountService

            accountService.doAutoLogin()
        }
    }

    private fun updateTip(tip: String) {
        tv_tip.text = tip
    }
}