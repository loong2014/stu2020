package com.sunny.family.city

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.sunny.family.R
import com.sunny.family.city.block.*
import com.sunny.lib.base.BaseActivity
import com.sunny.lib.city.CityInfo
import com.sunny.lib.city.CityManager
import com.sunny.lib.city.CityType
import com.sunny.lib.router.RouterConstant
import com.sunny.lib.utils.HandlerUtils
import com.sunny.lib.utils.SunToast
import com.sunny.view.data.SunBlockData
import com.sunny.view.layout.SunBlockLayoutManager
import kotlinx.android.synthetic.main.act_city_old.*

class CityActivityOld : BaseActivity() {
    val logTag = "City-CityActivity"

    lateinit var mRecyclerView: CityRecyclerView
    lateinit var mAdapter: CityBlockAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_city_old)

        initView()

        initData()
    }

    private fun initView() {
        mRecyclerView = city_recycler_view

        //
        mAdapter = CityBlockAdapter(CityBlockFactory())
        mRecyclerView.adapter = mAdapter

        //
        mRecyclerView.layoutManager = SunBlockLayoutManager(this)

    }

    private fun initData() {

        HandlerUtils.workHandler.post {
            val cityList: List<CityInfo>? = CityManager.chinaCityList

            if (cityList.isNullOrEmpty()) {
                SunToast.show("no city list")

            } else {

                val blockDataList = mutableListOf<SunBlockData>()
                cityList.forEach {

                    val blockData = SunBlockData()
                    blockData.name = it.name
                    blockData.data = it
                    blockData.uiType = when (it.type) {
                        CityType.Tip -> CityBlockTipHolder.BlockType

                        CityType.Direct -> CityDirectBlockHolder.BlockType

                        CityType.Special -> CitySpecialBlockHolder.BlockType

                        CityType.Normal -> CityNormalBlockHolder.BlockType

                        CityType.Autonomy -> CityAutonomyBlockHolder.BlockType

                        else -> CityBlockTipHolder.BlockType
                    }

                    blockDataList.add(blockData)


                    it.children?.let { childrenList ->
                        childrenList.forEach {
                            val blockData = SunBlockData()
                            blockData.name = it.name
                            blockData.data = it
                            blockData.uiType = when (it.type) {
                                CityType.Tip -> CityBlockTipHolder.BlockType

                                CityType.Direct -> CityDirectBlockHolder.BlockType

                                CityType.Special -> CitySpecialBlockHolder.BlockType

                                CityType.Normal -> CityNormalBlockHolder.BlockType

                                CityType.Autonomy -> CityAutonomyBlockHolder.BlockType

                                else -> CityBlockTipHolder.BlockType
                            }

                            blockDataList.add(blockData)
                        }
                    }
                }

                runOnUiThread {
                    doSetCityList(blockDataList)
                }
            }
        }
    }

    private fun doSetCityList(list: List<SunBlockData>) {
        mAdapter.bindData(list)
        mAdapter.notifyDataSetChanged()
    }
}