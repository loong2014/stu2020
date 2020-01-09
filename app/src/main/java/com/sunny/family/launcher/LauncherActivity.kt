package com.sunny.family.launcher

import android.os.Bundle

import com.sunny.family.R
import com.sunny.lib.base.BaseActivity
import com.sunny.lib.jump.PageJumpUtils
import com.sunny.lib.utils.HandlerUtils

class LauncherActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_launcher)

        jumpHomeAct()
    }

    private fun jumpHomeAct() {
        HandlerUtils.getUiHandler().postDelayed({
            PageJumpUtils.jumpHomePage(null, null)
        }, 1500)
    }

}
