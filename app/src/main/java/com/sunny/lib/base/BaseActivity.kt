package com.sunny.lib.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sunny.lib.manager.ActivityLifeManager

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityLifeManager.onCreate(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityLifeManager.onDestroy(this)
    }
}