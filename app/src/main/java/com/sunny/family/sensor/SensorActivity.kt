package com.sunny.family.sensor

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sunny.family.R
import com.sunny.family.sensor.rv.SensorAdapter
import com.sunny.family.sensor.rv.SensorItemDecoration
import com.sunny.family.sensor.rv.SensorLayoutManager
import com.sunny.lib.base.BaseActivity
import com.sunny.lib.utils.SunLog
import kotlinx.android.synthetic.main.act_sensor.*

/**
 * Created by zhangxin17 on 2020/3/26
 */
class SensorActivity : BaseActivity() {

    private val logTag = "Sensor-SensorActivity"

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: SensorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_sensor)

        initView()
    }

    private fun initView() {
        mRecyclerView = sensor_list

        mAdapter = SensorAdapter(this)
        mRecyclerView.adapter = mAdapter
        mRecyclerView.addItemDecoration(SensorItemDecoration())
        mRecyclerView.layoutManager = SensorLayoutManager(this)


        mAdapter.setOnClickListener(object : IBlockItemClickListener {
            override fun onItemClick(itemView: View, sensorInfo: SensorInfo, position: Int) {
                SunLog.i(logTag, "onItemClick  position:$position , $sensorInfo")
                dealItemClick(sensorInfo)
            }

            override fun onItemTouch(itemView: View, sensorInfo: SensorInfo, position: Int) {
                SunLog.i(logTag, "onItemTouch  position:$position , $sensorInfo")
                dealItemClick(sensorInfo)
            }
        })

        btn_all_sensor.setOnClickListener {
            getSensorList()
        }
    }

    private fun getSensorList() {
        SunSensorManager.getSensorList(object : ISensorListCallback {
            override fun onGetSensorList(list: List<SensorInfo>?) {
                runOnUiThread {
                    showSensorList(list)
                }
            }
        }, true)
    }

    private fun showSensorList(list: List<SensorInfo>?) {
        if (list.isNullOrEmpty()) {
            tv_sensor_count.text = "无"
            return
        }
        tv_sensor_count.text = "${list.size}"
        list.forEach {
            SunLog.i(logTag, "${it.showName}(${it.sensor?.type})")
        }
        mAdapter.setSensorList(list)
        mAdapter.notifyDataSetChanged()
    }

    private fun dealItemClick(sensorInfo: SensorInfo) {

    }

    private fun updateCurSensor(name: String) {
        cur_sensor_name.text = name
    }
}