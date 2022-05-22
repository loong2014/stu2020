package com.sunny.module.view.xxx.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import com.sunny.lib.common.base.BaseFragment
import com.sunny.module.view.R

/**
 * Created by zhangxin17 on 2020/11/19
 */
class XxxDrawerFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootLayout = inflater.inflate(R.layout.view_fragment_xxx_drawer, container, false)
        val nameView: AppCompatTextView = rootLayout.findViewById(R.id.fragment_name)
        nameView.text = "侧滑布局"
        return rootLayout
    }
}