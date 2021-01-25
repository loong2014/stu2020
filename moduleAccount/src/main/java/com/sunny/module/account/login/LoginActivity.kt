package com.sunny.module.account.login

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.sunny.lib.common.base.BaseActivity
import com.sunny.lib.common.router.RouterConstant
import com.sunny.lib.common.utils.SunSharedPreferencesUtils
import com.sunny.module.account.R
import kotlinx.android.synthetic.main.login_activity_login.*

/**
 * Created by zhangxin17 on 2020/12/29
 */
@Route(path = RouterConstant.Account.PageLogin)
class LoginActivity : BaseActivity() {

    lateinit var loginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.login_activity_login)

        // 1. 必须通过ViewModelProvider的方式获取ViewModel的实例
//        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        // 2. 通过LoginViewModelFactory传递参数到ViewModel
        val count = SunSharedPreferencesUtils.getInt("login_count", 0)
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(count)).get(LoginViewModel::class.java)

        // 3. 将通过 lifecycle 使 LoginObserver 获取当前 activity 的生命周期变化，
        // 通过传递 lifecycle 使 LoginObserver 可以获取当前生命周期的状态
        lifecycle.addObserver(LoginObserver(lifecycle))

        btnLogin.setOnClickListener {
            loginViewModel.doLogin()
        }

        btnReset.setOnClickListener {
            loginViewModel.doReset()
        }

        // 4. 监听 ViewModel 中 loginCount 数据的变化
        loginViewModel.loginCount.observe(this, Observer { count ->
            tvLoginTip.text = "欢迎用户: ${loginViewModel.loginName} 进行登录，登录次数: $count"
        })

        // 5
        loginViewModel.user.observe(this, Observer { userModel->

        })

//        { count ->
//            tvLoginTip.text = "欢迎用户: ${loginViewModel.loginName} 进行登录，登录次数: $count"
//        }
    }

    override fun onPause() {
        super.onPause()

        SunSharedPreferencesUtils.putInt("login_count", loginViewModel.loginCount.value ?: 0)
    }

    private fun refreshLoginCount() {
        tvLoginTip.text = "欢迎用户: ${loginViewModel.loginName} 进行登录，登录次数: ${loginViewModel.loginCount ?: 0}"
    }

}
