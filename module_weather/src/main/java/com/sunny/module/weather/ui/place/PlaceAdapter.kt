package com.sunny.module.weather.ui.place

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.sunny.lib.common.router.RouterConstant
import com.sunny.module.weather.R
import com.sunny.module.weather.city.PlaceInfo

class PlaceAdapter(private val fragment: Fragment, private var placeList: ArrayList<PlaceInfo>)
    : RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeName: TextView = view.findViewById(R.id.tvPlaceName)
        val placeAddress: TextView = view.findViewById(R.id.tvPlaceAddress)
    }

    fun updateList(list: List<PlaceInfo>) {
        placeList.clear()
        placeList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_item_place, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return placeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        var name = place.name
        if (place.nameEn.isNotEmpty()) {
            name += "( ${place.nameEn} )"
        }

        holder.placeName.text = name
        holder.placeAddress.text = "[${place.lng} , ${place.lat}]"

        holder.itemView.setOnClickListener {
            ARouter.getInstance().build(RouterConstant.Weather.PageWeather)
                    .withString("name", place.name)
                    .withString("lng", place.lng)
                    .withString("lat", place.lat)
                    .navigation()
        }
    }

}