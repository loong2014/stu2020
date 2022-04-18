package com.sunny.module.ble.master

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class PaxCalendarInfo(
    val os: String? = "",
    var events: List<PaxCalendarEventInfo>? = null
) : Parcelable

@Parcelize
data class PaxCalendarEventInfo(
    val title: String = "",
    val url: String = "",
    val location: String = "",
//    val notes: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val isDateTitle: Boolean = false,
    val isNoMeeting: Boolean = false,
    var showTime: String = ""
) : Parcelable

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