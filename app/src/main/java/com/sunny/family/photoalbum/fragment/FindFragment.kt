package com.sunny.family.photoalbum.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sunny.family.R
import com.sunny.lib.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_find.*

class FindFragment(private val tip: String) : BaseFragment() {

    private lateinit var rootView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_find, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tip_name.text = tip

        initData()

        setListener()
    }

    private fun initData() {

    }

    private fun setListener() {

    }

}