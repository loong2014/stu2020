package com.sunny.lib.base

import android.app.Activity
import android.os.Bundle
import com.sunny.lib.manager.ActivityLifeManager.onCreate
import com.sunny.lib.manager.ActivityLifeManager.onDestroy

open class BaseActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreate(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        onDestroy(this)
    }
}