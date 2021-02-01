package com.sunny.module.weather.ui.weather

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.sunny.lib.common.base.BaseActivity
import com.sunny.lib.common.router.RouterConstant
import com.sunny.module.weather.R
import com.sunny.module.weather.WeatherConstant
import com.sunny.module.weather.logic.model.Weather
import com.sunny.module.weather.logic.model.WeatherItemInfo
import kotlinx.android.synthetic.main.weather_activity_weather.*

@Route(path = RouterConstant.Weather.PageWeather)
class WeatherActivity : BaseActivity() {

    companion object {
        const val TAG = "WeatherActivity"
    }

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

    private var mAdapter: WeatherInfoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weather_activity_weather)

        ARouter.getInstance().inject(this)

        initView()

        initData()

    }

    private fun initView() {
        topBar.setMiddleName(name)

        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun initData() {
        viewModel.locationLng = lng
        viewModel.locationLat = lat
        viewModel.placeName = name

        viewModel.weatherLiveData.observe(this, Observer {
            val weather = it.getOrNull()
            if (weather != null) {
                dealGetWeatherInfo(weather)

            } else {
                showToast("获取天气数据失败")
            }
        })

        viewModel.refreshWeather(lng, lat)
    }

    private fun dealGetWeatherInfo(weather: Weather) {

        val list = mutableListOf<WeatherItemInfo>()

        //
        val realtime = weather.realtime.realtime

        //
        var key = "天气状况"
        var value = WeatherConstant.getSkyconTip(realtime.skycon)
        list.add(WeatherItemInfo(key, value))

        //
        key = "温度"
        value = realtime.temperature.toString()
        list.add(WeatherItemInfo(key, value))

        //
        key = "能见度"
        value = realtime.visibility.toString()
        list.add(WeatherItemInfo(key, value))

        //
        key = "相对湿度(%)"
        value = "${realtime.humidity}%"
        list.add(WeatherItemInfo(key, value))

        //
        key = "气压(Pa)"
        value = "${realtime.pressure}Pa"
        list.add(WeatherItemInfo(key, value))

        //
        realtime.wind?.let {
            key = "风向"
            value = it.direction.toString()
            list.add(WeatherItemInfo(key, value))

            key = "风速"
            value = it.speed.toString()
            list.add(WeatherItemInfo(key, value))
        }

        //
        realtime.airQuality?.let { air ->

            key = "PM25浓度(μg/m3)"
            value = "${air.pm25}μg/m3"
            list.add(WeatherItemInfo(key, value))

            key = "臭氧浓度(μg/m3)"
            value = "${air.o3}μg/m3"
            list.add(WeatherItemInfo(key, value))

            key = "一氧化碳浓度(mg/m3)"
            value = "${air.co}μg/m3"
            list.add(WeatherItemInfo(key, value))

            air.aqi?.let { aqi ->
                key = "国标AQI"
                value = aqi.chn.toString()
                list.add(WeatherItemInfo(key, value))
            }
        }

        showWeatherInfo(list)
    }

    private fun showWeatherInfo(data: MutableList<WeatherItemInfo>) {
        if (mAdapter == null) {
            mAdapter = WeatherInfoAdapter(data)
            recyclerView.adapter = mAdapter

        } else {
            mAdapter?.setList(data)
        }
    }

}