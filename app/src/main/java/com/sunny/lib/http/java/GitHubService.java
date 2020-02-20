package com.sunny.lib.http.java;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GitHubService {
//        https://baobab.kaiyanapp.com/api/v2/feed?num=2&udid=26868b32e808498db32fd51fb422d00175e179df&vc=83

    @GET("api/v2/feed")
    Call<ResultBody> listRepos(@Query("num") int num, @Query("udid") String udid, @Query("vc") String vc);
}
