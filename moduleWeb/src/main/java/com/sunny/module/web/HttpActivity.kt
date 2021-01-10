package com.sunny.module.web

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.sunny.lib.common.base.BaseActivity
import com.sunny.lib.common.router.RouterConstant
import com.sunny.module.web.http.SunnyHttpCallback
import com.sunny.module.web.http.SunnyHttpUtil
import com.sunny.module.web.retrofit.GodResult
import com.sunny.module.web.retrofit.GodService
import com.sunny.module.web.retrofit.SubmitResult
import com.sunny.module.web.retrofit.betTesyStr
import kotlinx.android.synthetic.main.web_activity_http.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

@Route(path = RouterConstant.Web.PageHttp)
class HttpActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.web_activity_http)

        initView()
    }

    private fun initView() {
        top_bar.setMiddleName("Http")
        top_bar.setOnBackBtnClickListener(View.OnClickListener {
            doExitActivity()
        })

        btn_get.setOnClickListener { doGetByRetrofit() }

        btn_post.setOnClickListener {
//            doPostRequest()
            doPostByRetrofit()
        }

    }

    private fun showResponse(str: String) {
        runOnUiThread {
            response_text.text = str
        }
    }


    /**
     * http://bet.swuyu.com/god/top?category=2&role=1
     */
    private fun doGetByRetrofit() {

        val godService = SunnyHttpUtil.create(GodService::class.java)

        godService.getGodTopData(2).enqueue(object : Callback<GodResult> {
            override fun onFailure(call: Call<GodResult>, t: Throwable) {
                showResponse(t.message.toString())
            }

            override fun onResponse(call: Call<GodResult>, response: Response<GodResult>) {

                val list = response.body()?.data
                val result = StringBuilder()
                list?.forEach {
                    result.append(it.godId)
                    result.append(" , ")
                    result.append(it.name)
                    result.append(" , ")
                    result.append(it.score)
                    result.append("\n")
                }

                showResponse(result.toString())
            }
        })
    }

    private fun doPostByRetrofit() {

        val godService = SunnyHttpUtil.create<GodService>()

        godService.doLotterySubmit(bet = betTesyStr).enqueue(object : Callback<SubmitResult> {
            override fun onFailure(call: Call<SubmitResult>, t: Throwable) {
                showResponse(t.message.toString())
            }

            override fun onResponse(call: Call<SubmitResult>, response: Response<SubmitResult>) {

                val result = response.body()?.data?.let {
                    it.orderNo + " , " + it.amount
                } ?: "error"
                showResponse(result);
            }
        })
    }

    private fun doGetRequest() {

        val address = "https://www.baidu.com"

        val httpCallback = object : SunnyHttpCallback {
            override fun onFinish(response: String) {
                showResponse(response)
            }

            override fun onError(e: Exception) {
                showResponse(e.message.toString())
            }
        }

//        SunnyHttpUtil.doHttpGetRequestDefault(address,httpCallback)
        SunnyHttpUtil.doHttpGetRequestOkHttp(address, httpCallback)

    }

    private fun doPostRequest() {

        // 开启线程发起请求
        thread {

            var connection: HttpURLConnection? = null
            try {
                //
                val url = URL("https://www.baidu.com")

                //
                connection = url.openConnection() as HttpURLConnection

                //
                connection.requestMethod = "POST"

                val output = DataOutputStream(connection.outputStream)
                output.writeBytes("username=admin&password=123456")
                //
                connection.connectTimeout = 8000
                connection.readTimeout = 8000

                //
                val input = connection.inputStream

                //
                val response = StringBuilder()
                response.append("doPost\n")
                val reader = BufferedReader(InputStreamReader(input))

                // use函数，保证表达式中的代码全部执行完毕后自动将外层的流关闭
                reader.use {
                    reader.forEachLine {
                        response.append(it)
                    }
                }

                //
                showResponse(response.toString())
            } catch (e: Exception) {
            } finally {
                connection?.disconnect()
            }
        }
    }
}
