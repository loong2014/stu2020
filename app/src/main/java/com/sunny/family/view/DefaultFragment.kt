package com.sunny.family.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sunny.family.R
import com.sunny.lib.base.BaseFragment

/**
 * Created by zhangxin17 on 2020/11/19
 */
class DefaultFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_default, container, false)
    }
}