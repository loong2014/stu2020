package com.sunny.lib.base.network

enum class NetWorkType(val netName: String) {
    AUTO("无状态"),
    CONNECT("已连接"),

    NONE("无网络"),

    OTHER("未知"),

    WIFI("wifi网络"),

    MOBILE("移动网络"),
    MOBILE_2G("2G"),
    MOBILE_3G("3G"),
    MOBILE_4G("4G"),
    MOBILE_OTHER(""),

}