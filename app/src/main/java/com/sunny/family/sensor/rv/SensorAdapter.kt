package com.sunny.family.sensor.rv

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sunny.family.R
import com.sunny.family.sensor.IBlockItemClickListener
import com.sunny.family.sensor.SensorBlockHolder
import com.sunny.family.sensor.SensorInfo
import com.sunny.lib.utils.SunLog

/**
 * Created by zhangxin17 on 2020/3/27
 */
class SensorAdapter(val context: Context) : RecyclerView.Adapter<SensorBlockHolder>() {
    private val logTag = "Sensor-SensorAdapter"

    private var mList: List<SensorInfo>? = null

    private var mLayoutInflater: LayoutInflater = LayoutInflater.from(context)

    private var mBlockClickListener: IBlockItemClickListener? = null

    fun setSensorList(list: List<SensorInfo>) {
        SunLog.i(logTag, "setSensorList")
        mList = list
    }

    fun setOnClickListener(listener: IBlockItemClickListener) {
        mBlockClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorBlockHolder {
        SunLog.i(logTag, "onCreateViewHolder  viewType:$viewType")
        return SensorBlockHolder.buildBlockHolder(context)
    }

    private fun getItemInfo(position: Int): SensorInfo? {
        return if (mList != null && mList!!.size > position) {
            mList!![position]
        } else {
            null
        }
    }

    override fun getItemCount(): Int {
        return if (mList != null) {
            mList!!.size
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: SensorBlockHolder, position: Int) {
        SunLog.i(logTag, "onBindViewHolder  position:$position")
        holder.setData(getItemInfo(position), position)
        holder.setBlockClickListener(mBlockClickListener)
    }


}