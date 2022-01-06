package com.sunny.module.web.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LeApiListResponse {
    var status: Int = 0
    var data: List<ChannelListResponse>? = null
}

class ChannelListResponse {
    var title: String? = null
    var dataList: List<LeAlbumInfoItem>? = null
}

class LeAlbumInfoItem {
    var dataType: Int = 0
    var categoryId: Int = 0
    var albumId: String? = null
    var name: String? = null
    var subName: String? = null
    var img: String? = null
}