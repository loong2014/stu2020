package com.sunny.module.weather.ui.place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunny.lib.common.base.BaseFragment
import com.sunny.lib.common.utils.SunToast
import com.sunny.module.weather.R
import com.sunny.module.weather.city.PlaceInfo
import kotlinx.android.synthetic.main.weather_fragment_place.*

class PlaceFragment : BaseFragment() {

    private val placeViewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) }

    private var mAdapter: PlaceAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.weather_fragment_place, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()

        initData()
    }

    private fun initView() {
        //
        mAdapter = PlaceAdapter(this, placeViewModel.getQueryResultList())
        recyclerView.adapter = mAdapter

        //
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        //
        searchPlaceEdit.addTextChangedListener {
            val content = it.toString()
            if (content.isNotEmpty()) {
                placeViewModel.queryCityInfo(content)
            } else {
                recyclerView.visibility = View.GONE
                bgImageView.visibility = View.VISIBLE
                placeViewModel.clearResult()
            }
        }
    }

    private fun initData() {

        placeViewModel.queryResultCount.observe(this, Observer {
            updateQueryResult()
        })
    }

    private fun updateQueryResult() {
        val list: List<PlaceInfo> = placeViewModel.getQueryResultList()

        if (list.isNotEmpty()) {
            recyclerView.visibility = View.VISIBLE
            bgImageView.visibility = View.GONE

            mAdapter?.updateList(list)

        } else {
            SunToast.show("数据为空")
            recyclerView.visibility = View.GONE
            bgImageView.visibility = View.VISIBLE
        }

    }
}