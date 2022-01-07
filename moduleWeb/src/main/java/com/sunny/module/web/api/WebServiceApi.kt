package com.sunny.module.web.api

import com.sunny.lib.base.log.SunLog
import com.sunny.lib.base.utils.ContextProvider
import com.sunny.module.web.retrofit.GodResult
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.File
import java.util.concurrent.TimeUnit

val webApi by lazy {
    buildWebApi()
}

var curCacheType: Int = 0

fun setCacheType(type: Int) {
    curCacheType = type
}

var curHasNet = false
fun setHasNet(has: Boolean) {
    curHasNet = has
}

private fun buildWebApi(): WebServiceApi {

    //
    val httpLoggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
//            val log = if (message.length > 128) {
//                message.substring(0, 128) + "..."
//            } else {
//                message
//            }
            SunLog.i("SunnyHttp", message)
        }
    })
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

    //
    val cacheSize: Long = 10 * 1024 * 1024 // 10M
    val cacheTime = 10//10秒
    val cacheDir = File(ContextProvider.appContext.cacheDir, "web_cache")
    val cache = Cache(cacheDir, cacheSize)

    //这是设置在多长时间范围内获取缓存里面
    // CacheControl.FORCE_CACHE--是int型最大值
    val FORCE_CACHE_SELF = CacheControl.Builder()
        .onlyIfCached()
        .maxStale(cacheTime, TimeUnit.SECONDS)
        .build()
    // 拦截器，保存缓存的方法
    val cacheInterceptor = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {

            var request = chain.request()
            SunLog.i("SunnyHttp", "intercept hasNet($curHasNet) , cacheType($curCacheType)")
            if (curHasNet) {
                // 有网时
                val response = chain.proceed(request)
                val control = request.cacheControl.toString()
                SunLog.i("SunnyHttp", "online 10s cache :$control")

                return if (curCacheType == 0) {
                    //总获取实时信息
                    response.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, max-age=0")
                        .build()
                } else {
                    //t（s）之后获取实时信息--此处是20s
                    response.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, max-age=$cacheTime")
                        .build()
                }
            } else {
                //无网时
                request = if (curCacheType == 0) {
                    //无网时一直请求有网请求好的缓存数据，不设置过期时间
                    request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)//此处不设置过期时间
                        .build()
                } else {
                    // 此处设置过期时间为t(s);t（s）之前获取在线时缓存的信息(此处t=20)，t（s）之后就不获取了
                    request.newBuilder()
                        .cacheControl(FORCE_CACHE_SELF)//此处设置了t秒---修改了系统方法
                        .build()
                }
                //下面注释的部分设置也没有效果，因为在上面已经设置了
                return chain.proceed(request).newBuilder()
                    .header("Cache-Control", "public, only-if-cached")
                    .removeHeader("Pragma")
                    .build();
            }
        }
    }

    val httpClient = OkHttpClient.Builder()
        .addInterceptor(cacheInterceptor)
        .addNetworkInterceptor(cacheInterceptor)
        .cache(cache)
        .addInterceptor(httpLoggingInterceptor)

    return Retrofit.Builder()
        .baseUrl("http://pax-us.leautolink.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient.build())
        .build()
        .create(WebServiceApi::class.java)
}
//PaxApiService providePaxApiService() {
//    httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//    OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
//    if (LogTree.isNetworkLog()) {
//        okHttpClient.addInterceptor(httpLoggingInterceptor);
//    }
//    return new Retrofit.Builder()
//        .baseUrl(UrlInfo.getHttpHost())
//        .addConverterFactory(GsonConverterFactory.create())
//        .addCallAdapterFactory(new LiveDataCallAdapterFactory())
//        .client(okHttpClient.build())
//        .build()
//        .create(PaxApiService.class);
//}

interface WebServiceApi {

    //http://api-itv.cp21.ott.cibntv.net/iptv/api/new/channel/data/list.json?channelId=647
    @GET("iptv/api/new/channel/data/list.json?")
    fun getChannelDataList(
        @Query("channelId") channelId: String
    ): Call<LeApiListResponse>

    /**
     * 添加请求参数
     */
    @GET("god/top")
    // http://pax-us.leautolink.com/v2/us/movies
    fun getMoviesInfo(): Call<GodResult>


}