package com.sunny.module.ble.master

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PaxMeetingInfo(val zoomList: List<PaxZoomInfo>?) : Parcelable

@Parcelize
data class PaxZoomInfo(
    val title: String,
    val url: String,
    val notes: String,
    val startDate: String,
    val endData: String
) : Parcelable