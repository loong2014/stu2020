package com.sunny.family.player

import android.os.Bundle
import com.sunny.lib.base.BaseActivity

open abstract class BasePlayerActivity : BaseActivity() {

    val playerHelper: BasePlayerHelper by lazy {
        buildPlayerHelper()
    }

    abstract fun buildPlayerHelper(): BasePlayerHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}