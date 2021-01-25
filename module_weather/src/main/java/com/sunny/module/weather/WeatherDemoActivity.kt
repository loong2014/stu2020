package com.sunny.module.weather

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.sunny.lib.common.base.BaseActivity
import com.sunny.lib.common.router.RouterConstant
import com.sunny.lib.common.router.RouterJump
import com.sunny.module.weather.city.CityUtils
import com.sunny.module.weather.ui.place.PlaceViewModel
import kotlinx.android.synthetic.main.weather_activity_demo.*

@Route(path = RouterConstant.Weather.PageDemo)
class WeatherDemoActivity : BaseActivity() {

    lateinit var placeViewModel: PlaceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.weather_activity_demo)

        placeViewModel = ViewModelProvider(this).get(PlaceViewModel::class.java)

        initView()

        CityUtils.tryLoadCsvDataToDb()

        placeViewModel.queryResultCount.observe(this, Observer {

        })
    }

    private fun initView() {
        top_bar.setMiddleName("模块：天气")
        top_bar.setOnBackBtnClickListener(View.OnClickListener {
            doExitActivity()
        })

        btnLoadData.setOnClickListener {
            CityUtils.tryLoadCsvDataToDb()
        }

        btnQueryData.setOnClickListener {
            placeViewModel.queryCityInfo("河南")
        }

        btnJumpSearch.setOnClickListener {
            RouterJump.navigation(RouterConstant.Weather.PageSearch)
        }
        btnJumpWeather.setOnClickListener {
            ARouter.getInstance().build(RouterConstant.Weather.PageWeather)
                    .withString("name", "北京市")
                    .withString("lng", "116.407526")
                    .withString("lat", "39.90403")
                    .navigation()
        }
    }
}