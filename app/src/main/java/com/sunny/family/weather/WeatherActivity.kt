package com.sunny.family.weather

import android.os.Bundle
import com.sunny.family.R
import com.sunny.lib.base.BaseActivity
import com.sunny.lib.utils.ResUtils
import kotlinx.android.synthetic.main.act_weather.*

class WeatherActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_weather)

        initData()
    }

    private fun initData() {

        val cityName: String? = intent.getStringExtra("cityName")

        tv_city_name.text = ResUtils.getString(R.string.weather_name_tip, { cityName ?: "none" })
    }
}