package com.sunny.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.sunny.lib.common.router.RouterConstant
import com.sunny.lib.common.router.RouterJump
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        btn_jump_stu.setOnClickListener {
            ARouter.getInstance().build(RouterConstant.Stu.PageDemo).navigation()
        }

        btn_jump_home.setOnClickListener {
            ARouter.getInstance().build(RouterConstant.Home.PageDemo).navigation()
        }

        btn_jump_login.setOnClickListener {
            ARouter.getInstance().build(RouterConstant.Account.PageDemo).navigation()
        }

        btn_jump_layout.setOnClickListener {
            ARouter.getInstance().build(RouterConstant.View.PageDemo).navigation()
        }

        btn_jump_web.setOnClickListener {
            ARouter.getInstance().build(RouterConstant.Web.PageDemo).navigation()
        }

        btn_jump_weather.setOnClickListener {
            RouterJump.navigation(RouterConstant.Weather.PageDemo)
        }
    }
}