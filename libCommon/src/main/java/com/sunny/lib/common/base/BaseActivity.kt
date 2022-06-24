package com.sunny.lib.common.base

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sunny.lib.common.font.PaxFontHelper
import com.sunny.lib.common.utils.SunToast

open class BaseActivity : AppCompatActivity() {

    lateinit var mmActivity: BaseActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        PaxFontHelper.byFactory(this)
        super.onCreate(savedInstanceState)
        mmActivity = this
    }

    protected fun showToast(msg: String) {
        SunToast.show(msg)
    }

    fun doExitActivity() {
        finish()
    }

    fun getActivity(): Activity {
        return this
    }
}