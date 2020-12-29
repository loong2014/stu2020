package com.sunny.lib.common.base

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {

    lateinit var mActivity: AppCompatActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mActivity = activity as AppCompatActivity
    }
}