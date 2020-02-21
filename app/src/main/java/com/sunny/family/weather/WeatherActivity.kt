package com.sunny.family.weather

import android.graphics.Color
import android.os.Bundle
import com.sunny.family.R
import com.sunny.lib.base.BaseActivity
import com.sunny.lib.utils.ResUtils
import com.sunny.lib.utils.SunToast
import com.sunny.lib.weather.SunWeatherConfig
import heweather.com.weathernetsdk.view.HeContent
import heweather.com.weathernetsdk.view.SynopticNetworkCustomView
import kotlinx.android.synthetic.main.act_weather.*


/**
 * key:05Htvxl7FH
 *
 * http://api.p.weatherdt.com/plugin/data?key=05Htvxl7FH&location=郑州
 */
class WeatherActivity : BaseActivity() {

    lateinit var mSynopticNetworkCustomView: SynopticNetworkCustomView
    lateinit var cityName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_weather)

        cityName = intent.getStringExtra("cityName").let {
            it ?: ""
        }

        if (cityName.isBlank()) {
            SunToast.show("cityName is invalid :$cityName")
        }


        SunWeatherConfig.initWeatherCity(cityName)
//        SunWeatherConfig.initWeatherCity("")
        initView()

    }

    private fun initView() {

        if (cityName.isBlank()) {
            tv_city_name.text = ResUtils.getString(R.string.weather_name_tip, "自动定位")
        } else {
            tv_city_name.text = ResUtils.getString(R.string.weather_name_tip, cityName)
        }

        // 固定控件
        mSynopticNetworkCustomView = synopticNetworkCustomView

        /**
         * 设置控件的对齐方式 默认居中
         * 详见viewGravity
         */
        mSynopticNetworkCustomView.setViewGravity(HeContent.GRAVITY_CENTER)

        /**
         * 设置控件的显示风格 默认横向
         * 详见viewType
         */
        mSynopticNetworkCustomView.setViewType(HeContent.TYPE_HORIZONTAL)

        /**
         * 设置控件内边距 默认为0
         * left 左边距
         * top 上边距
         * right 右边距
         * bottom 下边距
         */
        mSynopticNetworkCustomView.setViewPadding(5, 5, 5, 5)

        /**
         * 设置控件文字颜色 默认为黑色
         */
        mSynopticNetworkCustomView.setViewTextColor(Color.BLACK)

        //显示控件
        mSynopticNetworkCustomView.show()

//
//        // 悬浮控件
//        val suspendView = SuspendView(this)
//
//        //显示悬浮控件
//        suspendView.show()
    }

}