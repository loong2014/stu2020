package com.sunny.module.login

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.sunny.lib.common.base.BaseActivity
import com.sunny.lib.common.router.RouterConstant
import kotlinx.android.synthetic.main.login_activity_login.*

/**
 * Created by zhangxin17 on 2020/12/29
 */
@Route(path = RouterConstant.Login.PageLogin)
class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.login_activity_login)

        module_name.text = "模块：Login"
    }

}
