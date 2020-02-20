package com.sunny.family.detail

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.sunny.family.R
import com.sunny.family.detail.view.VerticalLinearLayoutManager
import com.sunny.family.detail.view.block.BlockType
import com.sunny.family.detail.view.common.RebuildDataModel
import com.sunny.lib.base.BaseActivity
import com.sunny.lib.city.CityInfo
import com.sunny.lib.jump.PageJumpUtils
import com.sunny.lib.utils.ContextProvider
import com.sunny.lib.utils.SunLog
import com.sunny.lib.utils.SunToast
import kotlinx.android.synthetic.main.act_detail.*

class DetailActivity : BaseActivity(), SunDetailHelper.DetailCallback {
    val logTag = "Detail-DetailActivity"

    lateinit var mDetailHelper: SunDetailHelper

    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: SunDetailBlockAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_detail)
        mDetailHelper = SunDetailHelper(this)

        initRecyclerView()

        initData()
    }

    private fun initRecyclerView() {

        mRecyclerView = detail_recycler_view

        //
        val blockFactory = SunDetailBlockFactory()
        mAdapter = SunDetailBlockAdapter(blockFactory)
        mRecyclerView.adapter = mAdapter

        //
        val layoutManager = VerticalLinearLayoutManager(ContextProvider.appContext)
        layoutManager.recycleChildrenOnDetach = true
        mRecyclerView.layoutManager = layoutManager

    }

    private fun initData() {

        mDetailHelper.doGetCityList()

    }

    private fun doSetCityList(list: List<CityInfo>) {
        SunLog.i(logTag, "doSetCityList")

        val rebuildList = mutableListOf<RebuildDataModel>()

        list.forEach {
            val model = RebuildDataModel()
            model.uiType = BlockType.BLOCK_SUN
            model.data = it
            rebuildList.add(model)
        }
        mAdapter.bindData(rebuildList)
        mAdapter.notifyDataSetChanged()

    }

    override fun onGetCityList(list: List<CityInfo>?) {
        if (list.isNullOrEmpty()) {
            SunToast.show("no city list")
        } else {
            runOnUiThread {
                doSetCityList(list)
            }
        }
    }

    private fun doJumpWeatherPage(cityInfo: CityInfo) {
        val intent = Intent();
        intent.putExtra("cityName", cityInfo.name)
        PageJumpUtils.jumpWeatherPage(intent, this)
    }

}