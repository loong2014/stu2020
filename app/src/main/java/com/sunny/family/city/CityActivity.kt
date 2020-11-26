package com.sunny.family.city

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.sunny.family.R
import com.sunny.family.city.adapter.CityAdapter
import com.sunny.lib.base.BaseActivity
import com.sunny.lib.router.RouterConstant
import com.sunny.lib.utils.SunLog
import kotlinx.android.synthetic.main.act_city.*

@Route(path = RouterConstant.PageCity)
class CityActivity : BaseActivity() {

    companion object {
        const val TAG = "City-Act"
    }

    lateinit var mAdapter: CityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_city)

        initView()
    }

    private fun initView() {
        initTopBar()
        initRecyclerView()
    }

    private fun initTopBar() {
        mTopBar.setMiddleName("城市列表")
    }

    private fun initRecyclerView() {
        mRecyclerview.layoutManager = GridLayoutManager(this, 1)
        mRecyclerview.setHasFixedSize(true)

        mAdapter = CityAdapter(buildCityData())
        mRecyclerview.adapter = mAdapter

        mAdapter.setOnItemClickListener { adapter, _, position ->

            val itemModel: CityItemModel = adapter.getItem(position) as CityItemModel
            if (itemModel.isCity()) {
                showToast(itemModel.showName)
            }
        }
        mRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val stickView = recyclerView.findChildViewUnder(0f, 0f)

                SunLog.i(TAG, "onScrolled $stickView")
            }
        })

    }
}