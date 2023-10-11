package com.sunny.module.web.webview

import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.alibaba.android.arouter.facade.annotation.Route
import com.sunny.lib.base.log.SunLog
import com.sunny.lib.common.base.BaseActivity
import com.sunny.lib.common.router.RouterConstant
import com.sunny.lib.common.utils.SunTimeUtils
import com.sunny.module.web.R
import kotlinx.android.synthetic.main.web_activity_webview.btn_amazon
import kotlinx.android.synthetic.main.web_activity_webview.btn_sling
import kotlinx.android.synthetic.main.web_activity_webview.btn_youtube
import kotlinx.android.synthetic.main.web_activity_webview.top_bar
import kotlinx.android.synthetic.main.web_activity_webview.webview

@Route(path = RouterConstant.Web.PageWebView)
class WebWebViewActivity : BaseActivity() {


    /**
     * https://blog.csdn.net/weixin_40438421/article/details/85700109
     */
    companion object {
        const val TAG = "WebWebViewActivity"

        //        const val URL_BASE = "https://www.sina.com.cn/"
        const val URL_BASE = "https://www.amazon.com/"

        fun showLog(msg: String) {
            SunLog.i(TAG, SunTimeUtils.getCurTimeStr() + " $msg")
        }
    }

    private var curUrl: String? = null
    private var isDesktop: Boolean = false

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
        btn_sling.setOnClickListener {
            curUrl = FFConfig.URL_SLING
            isDesktop = true
            tryLoadUrl()
        }
        btn_amazon.setOnClickListener {
            curUrl = FFConfig.URL_AMAZON
            isDesktop = true
            tryLoadUrl()
        }
        btn_youtube.setOnClickListener {
            curUrl = FFConfig.URL_YOUTUBE_MUSIC
            isDesktop = true
            tryLoadUrl()
        }
    }

    private fun tryLoadUrl() {
        showLog("tryLoadUrl url=${curUrl}, isDesktop=$isDesktop")
        val url = curUrl ?: return

        initWebView(url)
    }

    private fun initWebView(url: String) {
        mWebView = webview
        showLog("initWebView :$mWebView")

        mWebView?.run {
            //
            mWebSettings = settings
            initWebSettings()

            //
            webViewClient = MyWebViewClient()

            //
            addJavascriptInterface(MyJavascriptInterface(), "injectedObject")
            //
            loadUrl(url)
        }
    }

    private fun initWebSettings() {
        mWebSettings?.run {
            mediaPlaybackRequiresUserGesture = false

            userAgentString = FFConfig.getUA(isDesktop)

            // 屏幕适配，使内容适配屏幕宽度
            layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN;
            useWideViewPort = true
            loadWithOverviewMode = true
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
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            showLog("onPageStarted :${url}")
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            showLog("onPageFinished :${url}")
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            val url = request?.url?.toString()
            showLog("shouldOverrideUrlLoading :${url}")

            if (url.isNullOrBlank()) return true

            if (url.startsWith("intent://")) return true

            if (url.startsWith("about:") || url.startsWith("chrome:")) return false

            return false
        }

        override fun onLoadResource(view: WebView?, url: String?) {
            super.onLoadResource(view, url)
            showLog("onLoadResource $url")
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            if (request?.isForMainFrame == true && error?.errorCode != -1) {
                // 加载本地错误页面
            }
            showLog("onReceivedError :${request?.url}  >>>  ${error?.errorCode} , ${error?.description}")
            super.onReceivedError(view, request, error)
        }

        override fun onReceivedHttpError(
            view: WebView?,
            request: WebResourceRequest?,
            errorResponse: WebResourceResponse?
        ) {
            showLog("onReceivedHttpError :${request?.url} , $errorResponse")
            super.onReceivedHttpError(view, request, errorResponse)
        }

        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            showLog("onReceivedSslError :$handler , $error")
            super.onReceivedSslError(view, handler, error)
        }
    }
}