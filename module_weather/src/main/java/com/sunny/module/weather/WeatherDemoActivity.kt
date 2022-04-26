package com.sunny.module.weather

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.sunny.lib.common.base.BaseActivity
import com.sunny.lib.common.router.RouterConstant
import com.sunny.module.weather.city.CityUtils
import com.sunny.module.weather.databinding.WeatherActivityDemoBinding
import com.sunny.module.weather.ui.place.PlaceViewModel
import dagger.hilt.android.AndroidEntryPoint

@Route(path = RouterConstant.Weather.PageDemo)
class WeatherDemoActivity : BaseActivity() {

    private lateinit var mActivityBinding: WeatherActivityDemoBinding

    lateinit var placeViewModel: PlaceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mActivityBinding = DataBindingUtil.setContentView(this, R.layout.weather_activity_demo)

        placeViewModel = ViewModelProvider(this).get(PlaceViewModel::class.java)

        initView()

        CityUtils.tryLoadCsvDataToDb()

        placeViewModel.queryResultCount.observe(this, Observer {

        })
    }

    private fun initView() {
        mActivityBinding.topBar.setMiddleName("模块：天气")
//
//        btnLoadData.setOnClickListener {
//            CityUtils.tryLoadCsvDataToDb()
//        }
//
//        btnQueryData.setOnClickListener {
//            placeViewModel.queryCityInfo("河南")
//        }
//
//        btnJumpSearch.setOnClickListener {
//            RouterJump.navigation(RouterConstant.Weather.PageSearch)
//        }
//        btnJumpWeather.setOnClickListener {
//            ARouter.getInstance().build(RouterConstant.Weather.PageWeather)
//                .withString("name", "北京市")
//                .withString("lng", "116.407526")
//                .withString("lat", "39.90403")
//                .navigation()
//        }
    }
}