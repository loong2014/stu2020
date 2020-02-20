package com.sunny.family.cityexpan

import android.os.Bundle
import android.widget.ExpandableListView
import com.sunny.family.R
import com.sunny.lib.base.BaseActivity
import com.sunny.lib.city.CityInfo
import com.sunny.lib.city.CityManager
import com.sunny.lib.utils.HandlerUtils
import com.sunny.lib.utils.SunToast
import kotlinx.android.synthetic.main.act_city_expandable.*

class ExpandableCityActivity : BaseActivity() {
    val logTag = "City-ExpandableCityActivity"

    lateinit var mExpandableListView: ExpandableListView
    lateinit var mExpandableCityAdapter: ExpandableCityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_city_expandable)

        initView()

        initData()
    }

    private fun initView() {
        mExpandableListView = city_list
        mExpandableListView.setGroupIndicator(null)

        //
        mExpandableCityAdapter = ExpandableCityAdapter(this)
        mExpandableListView.setAdapter(mExpandableCityAdapter)

    }

    private fun initData() {

        HandlerUtils.workHandler.post {
            val cityList: List<CityInfo>? = CityManager.chinaCityList

            if (cityList.isNullOrEmpty()) {
                SunToast.show("no city list")

            } else {

                runOnUiThread {
                    mExpandableCityAdapter.setCityList(cityList)
                    mExpandableCityAdapter.notifyDataSetChanged()
                }
            }
        }
    }

}