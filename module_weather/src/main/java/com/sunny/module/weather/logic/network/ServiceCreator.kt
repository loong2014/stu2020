package com.sunny.module.weather.logic.network

import com.sunny.lib.base.log.SunLog
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {
    private const val BASE_URL = "https://api.caiyunapp.com/"

    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClient()) // 使用自定义OkHttp
            .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    inline fun <reified T> create(): T = create(T::class.java)


    private fun getOkHttpClient(): OkHttpClient {

        //
        val loggingInterceptor = HttpLoggingInterceptor(logger = object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                SunLog.i("SunHttp", message)

            }
        })
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        //
        val clientBuilder = OkHttpClient.Builder()
        clientBuilder.addInterceptor(loggingInterceptor)

        return clientBuilder.build()
    }


//
//    private OkHttpClient getOkHttpClient() {
//        //日志显示级别
//        HttpLoggingInterceptor.Level level= HttpLoggingInterceptor.Level.BODY;
//        //新建log拦截器
//        HttpLoggingInterceptor loggingInterceptor=new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
//            @Override
//            public void log(String message) {
//                Log.d("zcb","OkHttp====Message:"+message);
//            }
//        });
//        loggingInterceptor.setLevel(level);
//        //定制OkHttp
//        OkHttpClient.Builder httpClientBuilder = new OkHttpClient
//                .Builder();
//        //OkHttp进行添加拦截器loggingInterceptor
//        httpClientBuilder.addInterceptor(loggingInterceptor);
//        return httpClientBuilder.build();
//    }
}