package com.sunny.lib.common.base

import android.os.Bundle
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import com.sunny.lib.common.font.PaxFontHelper
import com.sunny.lib.common.mvvm.isMainThread
import com.sunny.lib.common.utils.SunToast
import timber.log.Timber

open class BaseActivity : AppCompatActivity() {

    //回收标志位
    private var hasReleaseResource = false

    // 解决onSaveInstanceState引起的问题
    private var hasSaveInstanceState = false


    protected val mHandler by lazy {
        ActivityHandler(this)
    }

    protected val mmActivity: BaseActivity by lazy {
        this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        PaxFontHelper.byFactory(this)
        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        hasSaveInstanceState = true
    }

    override fun onResume() {
        super.onResume()
        hasSaveInstanceState = false
    }

    override fun onPause() {
        if (isFinishing) {
            readyToResource()
        }
        super.onPause()
    }

    override fun onStop() {
        if (isFinishing) {
            readyToResource()
        }
        super.onStop()
    }

    override fun onBackPressed() {
        if (!hasSaveInstanceState) {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        readyToResource()
        super.onDestroy()
    }

    protected fun executeInMainThread(block: () -> Unit) {
        if (isMainThread()) {
            block()
        } else {
            mHandler.post { block() }
        }
    }

    private fun readyToResource() {
        mHandler.removeCallbacksAndMessages(null)
        if (!hasReleaseResource) {
            hasReleaseResource = true
            Timber.i("releaseResource")
            releaseResource()
        }
    }

    /**
     * 防内存泄漏的Handler类
     */
    class ActivityHandler(data: BaseActivity) :
        BaseHandler<BaseActivity?>(data) {
        override fun disposeMessage(msg: Message?) {}
    }

    protected fun showToast(msg: String) {
        SunToast.show(msg)
    }

    fun doExitActivity() {
        finish()
    }

    //实现类中资源释放
    open fun releaseResource() {}
}