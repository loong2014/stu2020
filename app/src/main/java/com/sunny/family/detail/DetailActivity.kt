package com.sunny.family.detail

import android.os.Bundle
import com.sunny.family.R
import com.sunny.family.detail.view.DetailBlockFactory
import com.sunny.family.detail.view.HomeRecyclerView
import com.sunny.lib.base.BaseActivity
import kotlinx.android.synthetic.main.act_detail.*

class DetailActivity : BaseActivity() {

    lateinit var mRecyclerView: HomeRecyclerView
    lateinit var mBlockFactory: DetailBlockFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_detail)

        initView()

        initData()
    }

    private fun initView() {
        mRecyclerView = detail_recycler_view
    }

    private fun initData() {

    }
}