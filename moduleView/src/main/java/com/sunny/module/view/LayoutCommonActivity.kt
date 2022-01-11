package com.sunny.module.view

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.sunny.lib.common.base.BaseActivity
import com.sunny.module.view.databinding.ViewActCommonBinding

class LayoutCommonActivity : BaseActivity() {

    lateinit var mActBinding: ViewActCommonBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActBinding = DataBindingUtil.setContentView(this, R.layout.view_act_common)
    }
}