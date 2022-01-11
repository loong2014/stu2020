package com.sunny.lib.common.base

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sunny.lib.common.manager.ActivityLifeManager
import com.sunny.lib.common.utils.SunToast

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityLifeManager.onCreate(this)
    }

    protected fun showToast(msg: String) {
        SunToast.show(msg)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityLifeManager.onDestroy(this)
    }

    fun doExitActivity() {
        finish()
    }

    fun getActivity():Activity{
        return this
    }
}