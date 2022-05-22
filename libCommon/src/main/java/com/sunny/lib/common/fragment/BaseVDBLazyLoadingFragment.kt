package com.sunny.lib.common.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.sunny.lib.common.base.BaseFragment
import com.sunny.lib.common.vm.BaseViewModel

class BaseVDBLazyLoadingFragment<VM : BaseViewModel, VDB : ViewDataBinding> : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}