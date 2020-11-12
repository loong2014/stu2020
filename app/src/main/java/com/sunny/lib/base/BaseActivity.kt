package com.sunny.lib.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sunny.lib.manager.ActivityLifeManager
import com.sunny.lib.utils.SunToast

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
}