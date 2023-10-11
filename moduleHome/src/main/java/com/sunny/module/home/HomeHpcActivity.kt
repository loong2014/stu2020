package com.sunny.module.home

import com.alibaba.android.arouter.facade.annotation.Route
import com.sunny.lib.common.router.RouterConstant

@Route(path = RouterConstant.Home.PageHome)
class HomeHpcActivity : HomeActivity() {

    override fun onResume() {
        super.onResume()
        updateTip("HomeHpcActivity")
    }
}