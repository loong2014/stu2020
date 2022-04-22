package com.sunny.module.weather.api

import retrofit2.Response

interface ApiHelper {
    suspend fun requestSearchResultAllAsync(keyword: String):
            Response<PaxApiResponseV2<PaxSearchResult>>

    suspend fun requestSearchResultInfoAsync(categoryPath: String, keyword: String):
            Response<PaxApiResponseV2<PaxSearchResultInfo>>
}