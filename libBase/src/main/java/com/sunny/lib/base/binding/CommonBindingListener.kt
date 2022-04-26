package com.sunny.lib.base.binding

import android.view.View

interface CommonBindingListener {
    fun commonOpt(v: View)

    fun commonOpt(v: View, opt: String = "")
}