package com.sunny.module.weather.ui.weather

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.sunny.lib.common.base.BaseActivity
import com.sunny.lib.common.router.RouterConstant
import com.sunny.module.weather.R
import com.sunny.module.weather.logic.model.Weather
import kotlinx.android.synthetic.main.weather_item_now.*

@Route(path = RouterConstant.Weather.PageWeather)
class WeatherActivity : BaseActivity() {


    @JvmField
    @Autowired(name = "lng")
    var lng: String = "116.407526"

    @JvmField
    @Autowired(name = "lat")
    var lat: String = "39.90403"

    @JvmField
    @Autowired(name = "name")
    var name: String = "北京市"

    private val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weather_activity_weather)
        ARouter.getInstance().inject(this)

        viewModel.locationLng = lng
        viewModel.locationLat = lat
        viewModel.placeName = name

        viewModel.weatherLiveData.observe(this, Observer {
            val weather = it.getOrNull()
            if (weather != null) {
                showWeatherInfo(weather)

            } else {
                showToast("获取天气数据失败")
            }
        })

        viewModel.refreshWeather(lng, lat)
    }

    private fun showWeatherInfo(weather: Weather) {
        tvPlaceName.text = viewModel.placeName

    }

}