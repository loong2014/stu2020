package com.sunny.module.weather.api

import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val paxApiServiceKt: ApiService) :
    ApiHelper {

    private fun getFFid(): String {
        return ""
    }

    private fun getZipCode(): String {
        return "90275"
    }

    override suspend fun requestSearchResultAllAsync(keyword: String) =
        paxApiServiceKt.requestSearchResultAllAsync(keyword, getZipCode(), getFFid())

    override suspend fun requestSearchResultInfoAsync(categoryPath: String, keyword: String) =
        paxApiServiceKt.requestSearchResultInfoAsync(categoryPath, keyword, getZipCode(), getFFid())
}