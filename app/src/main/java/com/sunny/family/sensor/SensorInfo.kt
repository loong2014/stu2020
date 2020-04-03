package com.sunny.family.sensor

import android.content.Context
import android.hardware.Sensor
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sunny.family.R
import com.sunny.lib.utils.SunLog

/**
 * Created by zhangxin17 on 2020/3/27
 */
data class SensorInfo(var showName: String = "showName", var sensor: Sensor? = null, var canShown: Boolean = false)

interface ISensorListCallback {
    fun onGetSensorList(list: List<SensorInfo>?)
}

interface IBlockItemClickListener {
    fun onItemClick(itemView: View, sensorInfo: SensorInfo, position: Int)
    fun onItemTouch(itemView: View, action: Int, sensorInfo: SensorInfo, position: Int)
}

class SensorBlockHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val logTag = "Sensor-SensorBlockHolder"

    var showName: TextView = itemView.findViewById(R.id.item_name)

    companion object {
        fun buildBlockHolder(context: Context): SensorBlockHolder {
            return SensorBlockHolder(LayoutInflater.from(context).inflate(R.layout.item_view_sensor, null))
        }
    }

    fun setData(info: SensorInfo?, position: Int) {
        SunLog.i(logTag, "setData $info")
        if (info == null || !info.canShown) {
            return
        }

        showName.text = info.showName
        itemView.setTag(R.id.tag_block_view_data, info)
        itemView.setTag(R.id.tag_block_view_position, position)
    }

    fun setBlockClickListener(listener: IBlockItemClickListener?) {
        if (listener == null) {
            return
        }

        itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (v == null) {
                    return
                }

                val info: SensorInfo = v.getTag(R.id.tag_block_view_data) as SensorInfo
                val pos: Int = v.getTag(R.id.tag_block_view_position) as Int
                listener.onItemClick(v, info, pos)
            }
        })
//
//        itemView.setOnTouchListener(object : View.OnTouchListener {
//            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                if (v == null || event == null) {
//                    return true
//                }
//
//                val info: SensorInfo = v.getTag(R.id.tag_block_view_data) as SensorInfo
//                val pos: Int = v.getTag(R.id.tag_block_view_position) as Int
//
//                SunLog.i(logTag, "onTouch  ${info.showName} , event :$event")
//                listener.onItemTouch(v, event.action, info, pos)
//                return true
//            }
//        })
    }

}
