package com.sunny.module.web.retrofit

import retrofit2.Call
import retrofit2.http.*

//http://bet.swuyu.com/god/top?category=2&role=1


data class GodResult(val errorCode: Int, val data: List<God>)
data class God(val godId: Long, val name: String, val score: Int)

data class LotteryInfo(val bet: String)

data class SubmitResult(val errorCode: Int, val data: SubmitInfo)

data class SubmitInfo(val orderNo: String, val amount: Int)

const val betTesyStr = "{\"amount\":2,\"betway\":\"ssq\",\"count\":1,\"issueNo\":\"21004\",\"numbers\":[{\"back\":[\"09\"],\"front\":[\"05\",\"07\",\"11\",\"13\",\"14\",\"31\"],\"gg\":\"dans\"}],\"rebet\":1,\"times\":1,\"winstop\":false,\"zhj\":false}"

interface GodService {

    /**
     * 填入相对路径和参数
     */
    @GET("god/top?category=2&role=1")
    fun getGodTopData(): Call<GodResult>


    /**
     * 动态填入路径
     */
    @GET("{god}/top?category=2&role=1")
    fun getTopData(@Path("god") god: String): Call<GodResult>

    /**
     * 添加请求参数
     */
    @GET("god/top")
    fun getGodTopData(@Query("category") category: Int, @Query("role") role: Int = 1): Call<GodResult>

    /**
     * 采用@Post表示Post方法进行请求（传入部分url地址）
     * 采用@FormUrlEncoded注解的原因:API规定采用请求格式x-www-form-urlencoded,
     * 即表单形式需要配合@Field 向服务器提交需要的字段
     */
    @POST("bet/commit")
    @FormUrlEncoded
    fun doLotterySubmit(@Field("bet") bet: String): Call<SubmitResult>

}