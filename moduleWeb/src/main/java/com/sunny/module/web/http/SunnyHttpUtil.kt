package com.sunny.module.web.http

import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

object SunnyHttpUtil {
//http://bet.swuyu.com/god/top?category=2&role=1

    private const val BASE_UEL = "http://bet.swuyu.com/"

    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_UEL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @JvmStatic
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    /**
     * 泛型实例化
     */
    inline fun <reified T> create(): T = create(T::class.java)

    @JvmStatic
    fun doHttpGetRequestOkHttp(address: String, listener: SunnyHttpCallback) {
        val client = OkHttpClient()
        val request = Request.Builder()
                .url(address)
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                listener.onFinish(response.body?.string() ?: "empty")
            }

            override fun onFailure(call: Call, e: IOException) {
                listener.onError(e)
            }
        })
    }

    @JvmStatic
    fun doHttpGetRequestDefault(address: String, listener: SunnyHttpCallback) {

        //
        thread {
            //
            var connection: HttpURLConnection? = null
            try {
                //
                val response = StringBuilder()
                response.append("doGet\n")

                //
                val url = URL(address)

                //
                connection = url.openConnection() as HttpURLConnection

                // 默认"GET"
                connection.requestMethod = "GET"

                //
                connection.connectTimeout = 8000
                connection.readTimeout = 8000

                //
                val input = connection.inputStream

                val reader = BufferedReader(InputStreamReader(input))

                // use函数，保证表达式中的代码全部执行完毕后自动将外层的流关闭
                reader.use {
                    reader.forEachLine {
                        response.append(it)
                    }
                }

                //
                listener.onFinish(response.toString())

            } catch (e: Exception) {
                //
                listener.onError(e)

            } finally {
                connection?.disconnect()
            }
        }
    }
}