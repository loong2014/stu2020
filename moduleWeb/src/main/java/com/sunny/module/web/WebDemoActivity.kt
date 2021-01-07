package com.sunny.module.web

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import com.alibaba.android.arouter.facade.annotation.Route
import com.sunny.lib.common.base.BaseActivity
import com.sunny.lib.common.router.RouterConstant
import kotlinx.android.synthetic.main.web_activity_demo.*

@Route(path = RouterConstant.Web.PageDemo)
class WebDemoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.web_activity_demo)

        initView()
    }

    override fun onResume() {
        super.onResume()

        initWebView()
    }

    private fun initView() {
        top_bar.setMiddleName("模块：WebView")
        top_bar.setOnBackBtnClickListener(View.OnClickListener {
            doExitActivity()
        })

        //
//        initWebView()
    }

    private fun initWebView() {
        webview.settings.javaScriptEnabled = true
        webview.webViewClient = WebViewClient()

        webview.loadUrl("https://www.sina.com.cn/")

    }
}