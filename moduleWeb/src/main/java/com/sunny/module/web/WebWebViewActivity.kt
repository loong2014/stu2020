package com.sunny.module.web

import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import com.alibaba.android.arouter.facade.annotation.Route
import com.sunny.lib.base.log.SunLog
import com.sunny.lib.common.base.BaseActivity
import com.sunny.lib.common.router.RouterConstant
import com.sunny.lib.common.utils.SunTimeUtils
import kotlinx.android.synthetic.main.web_activity_webview.*

@Route(path = RouterConstant.Web.PageWebView)
class WebWebViewActivity : BaseActivity() {

    /**
     * https://blog.csdn.net/weixin_40438421/article/details/85700109
     */
    companion object {
        const val TAG = "WebWebViewActivity"
        const val URL_BASE = "https://www.sina.com.cn/"

        fun showLog(msg: String) {
            SunLog.i(TAG, SunTimeUtils.getCurTimeStr() + " $msg")
        }
    }

    var mWebView: WebView? = null
    var mWebSettings: WebSettings? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showLog("onCreate")
        setContentView(R.layout.web_activity_webview)

        initView()
    }

    override fun onStop() {
        super.onStop()
        mWebView?.onPause()
    }

    override fun onDestroy() {
        doExitWebView()
        super.onDestroy()
    }

    private fun initTopBar() {
        top_bar.setMiddleName("WebView")
        top_bar.setOnBackBtnClickListener(View.OnClickListener {
            if (!handlerBack()) {
                doExitActivity()
            }
        })
    }

    private fun initView() {

        //
        initTopBar()

        //
        mWebView = webview
        initWebView()
    }

    private fun initWebView() {
        showLog("initWebView")
        mWebView?.run {
            //
            mWebSettings = settings
            initWebSettings()

            //
            webViewClient = MyWebViewClient()

            //
            addJavascriptInterface(MyJavascriptInterface(), "injectedObject")
            //
            loadUrl(URL_BASE)
        }
    }

    private fun initWebSettings() {
        mWebSettings?.run {
            javaScriptEnabled = true
        }

    }

    private fun handlerBack(): Boolean {
        if (mWebView?.canGoBack() == true) {
            mWebView?.goBack()
            return true
        }
        return false
    }

    private fun doExitWebView() {
        mWebView?.run {
            loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
            clearHistory()
            (parent as ViewGroup).removeView(mWebView)
            destroy()
        }
        mWebView = null
    }

    class MyJavascriptInterface {

        @android.webkit.JavascriptInterface
        fun jumpPayPage() {

        }
    }

    class MyWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            showLog("shouldOverrideUrlLoading")
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            showLog("onPageStarted")
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            showLog("onPageFinished")
        }

        override fun onLoadResource(view: WebView?, url: String?) {
            super.onLoadResource(view, url)
            showLog("onLoadResource $url")
        }

        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
            super.onReceivedError(view, request, error)
            // 加载本地错误页面
            showLog("onReceivedError $error")
        }

        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
            super.onReceivedSslError(view, handler, error)
            //https证书响应
        }
    }


    class MyWebChromeClient : WebChromeClient() {

    }

}