package com.sunny.module.ble.meeting

data class PaxCalendarInfo(
    val os: String? = "",
    var events: List<PaxCalendarEventInfo>? = null
)

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
)

data class PaxMeetingInfo(val zoomList: List<PaxZoomInfo>?)

data class PaxZoomInfo(
    val title: String,
    val url: String,
    val notes: String,
    val startDate: String,
    val endData: String
)