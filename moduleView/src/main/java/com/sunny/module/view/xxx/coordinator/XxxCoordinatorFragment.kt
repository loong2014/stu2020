package com.sunny.module.view.xxx.coordinator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sunny.lib.base.log.SunLog
import com.sunny.lib.common.base.BaseFragment
import com.sunny.lib.common.city.CityAdapter
import com.sunny.lib.common.city.CityItemModel
import com.sunny.lib.common.city.buildCityData
import com.sunny.module.view.R
import com.sunny.module.view.layout.CoordinatorLayoutActivity

/**
 * Created by zhangxin17 on 2020/11/19
 */
class XxxCoordinatorFragment : BaseFragment() {

    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: CityAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.view_fragment_xxx_coordinator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    fun initView(root: View) {
        mRecyclerView = root.findViewById(R.id.video_recycler_view)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mRecyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
        mRecyclerView.setHasFixedSize(true)

        mAdapter = CityAdapter(buildCityData())
        mRecyclerView.adapter = mAdapter

        mAdapter.setOnItemClickListener { adapter, view, position ->

            val itemModel: CityItemModel = adapter.getItem(position) as CityItemModel
            if (itemModel.isCity()) {
                showToast(itemModel.showName)
            }
        }
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val stickView = recyclerView.findChildViewUnder(0f, 0f)

                SunLog.i(CoordinatorLayoutActivity.TAG, "onScrolled $stickView")
            }
        })
    }
}