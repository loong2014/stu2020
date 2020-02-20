package com.sunny.lib.http.java;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpMain {

    public static void main(String[] args) {

        testSync();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://api.github.com/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        GitHubService service = retrofit.create(GitHubService.class);
//
////        Call<List<ResultBody>> repos = service.listRepos("octocat");
//        Call<ResponseBody> repos = service.listRepos("octocat");
//
//
//        List<ResultBody> list = (List<ResultBody>) repos.request().body();
//        for (ResultBody repo:list){
//            log("name:"+repo.getName());
//        }
//        log(repos.toString());

    }

    private static void testAsync() {
//        https://baobab.kaiyanapp.com/api/v2/feed?num=2&udid=26868b32e808498db32fd51fb422d00175e179df&vc=83
        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://api.github.com/")
                .baseUrl("https://baobab.kaiyanapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GitHubService service = retrofit.create(GitHubService.class);
        final Call<ResultBody> repos = service.listRepos(2, "26868b32e808498db32fd51fb422d00175e179df", "83");

        log("" + repos.request());

        repos.enqueue(new Callback<ResultBody>() {
            @Override
            public void onResponse(Call<ResultBody> call, Response<ResultBody> response) {
                ResultBody result = response.body();
                log("onResponse-" + result.getNewestIssueType());
            }

            @Override
            public void onFailure(Call<ResultBody> call, Throwable t) {
                log("onFailure");

            }
        });

    }

    private static void testSync() {

//        https://baobab.kaiyanapp.com/api/v2/feed?num=2&udid=26868b32e808498db32fd51fb422d00175e179df&vc=83
        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://api.github.com/")
                .baseUrl("http://baobab.kaiyanapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GitHubService service = retrofit.create(GitHubService.class);
        final Call<ResultBody> repos = service.listRepos(2, "26868b32e808498db32fd51fb422d00175e179df", "83");

        log("" + repos.request());

        Response<ResultBody> response = null;
        try {
            response = repos.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (response != null && response.isSuccessful()) {
            ResultBody result = response.body();
            log("onResponse-" + result.getNewestIssueType());

        } else {
            log("onFailure");
        }
    }

    private static void log(String msg) {
        System.out.println("HttpMain-" + msg);

    }
}
