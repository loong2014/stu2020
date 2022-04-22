package com.sunny.module.weather.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // http://pax-us.autotsp.com/v2/search/all?keyword=in&zipcode=90051
    @GET("v2/search/all")
    suspend fun requestSearchResultAllAsync(
        @Query("keyword") keyword: String?,
        @Query("zipcode") zipcode: String?,
        @Query("ffid") ffid: String?,
    ): Response<PaxApiResponseV2<PaxSearchResult>>

    // 直播搜索：
    //http://pax-us.autotsp.com/v2/search/live?keyword=in&zipcode=90051
    //短视频搜索：
    //http://pax-us.autotsp.com/v2/search/short_video?keyword=in
    //点播视频搜索：
    //http://pax-us.autotsp.com/v2/search/video?keyword=in
    @GET("v2/search/{categoryPath}")
    suspend fun requestSearchResultInfoAsync(
        @Path("categoryPath") categoryPath: String,
        @Query("keyword") keyword: String?,
        @Query("zipcode") zipcode: String?,
        @Query("ffid") ffid: String?,
    ): Response<PaxApiResponseV2<PaxSearchResultInfo>>
}