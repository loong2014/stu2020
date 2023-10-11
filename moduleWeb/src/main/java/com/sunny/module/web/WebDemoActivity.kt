package com.sunny.module.web

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.sunny.lib.common.base.BaseActivity
import com.sunny.lib.common.router.RouterConstant
import com.sunny.module.web.ble.BleDemoActivity
import kotlinx.android.synthetic.main.web_activity_demo.*
import timber.log.Timber

@Route(path = RouterConstant.Web.PageDemo)
class WebDemoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.web_activity_demo)

        initView()
        initEvent()
    }

    private fun initView() {
        top_bar.setMiddleName("模块：Web")
        top_bar.setOnBackBtnClickListener(View.OnClickListener {
            doExitActivity()
        })

        btn_webview.setOnClickListener {
            ARouter.getInstance().build(RouterConstant.Web.PageWebView).navigation()
        }

        btn_http.setOnClickListener {
            ARouter.getInstance().build(RouterConstant.Web.PageHttp).navigation()
        }

        btn_cache.setOnClickListener {
            startActivity(Intent(this, HttpCacheActivity::class.java))
        }

        btn_ble.setOnClickListener {
            startActivity(Intent(this, BleDemoActivity::class.java))
        }
    }

    override fun dispatchGenericMotionEvent(ev: MotionEvent?): Boolean {
        Timber.i("Debug-dispatchGenericMotionEvent :$ev")
        return super.dispatchGenericMotionEvent(ev)
    }
    private fun initEvent(){

    }
}