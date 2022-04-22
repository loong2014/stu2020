package com.sunny.module.weather.api

class PaxApiResponseV2<T> {
    var code: String? = null
    var message: String? = null
    var result: T? = null
}

class PaxApiResponseListV2<T> {
    var code: String? = null
    var message: String? = null
    var result: List<T>? = null
}

// http://pax-us.autotsp.com/v2/search/all?keyword=in&zipcode=90051
class PaxSearchResult {
    var liveResult: PaxSearchResultInfo? = null
    var shortVideoResult: PaxSearchResultInfo? = null
    var videoResult: PaxSearchResultInfo? = null
}

class PaxSearchResultInfo {
    var categoryTitle: String? = null
    var keyword: String? = null
    var list: List<PaxSearchResultItem>? = null
    var path: String? = null
    var icon: String? = null
}

class PaxSearchResultItem {
    // live/short_video/video
    var type: String? = null

    var title: String? = null
    var image: String? = null

    // live field
    var stationName: String? = null
    var stationId: String? = null

    // video field
    var videoId: String? = null
    var weblink: String? = null
    var duration: String? = null
}