package com.sunny.module.web

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.sunny.lib.common.base.BaseActivity
import com.sunny.lib.common.router.RouterConstant
import kotlinx.android.synthetic.main.web_activity_http.*
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

@Route(path = RouterConstant.Web.PageHttp)
class WebHttpActivity : BaseActivity() {

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

        btn_get.setOnClickListener { doGetRequest() }

        btn_post.setOnClickListener {
            doPostRequest()
        }

    }

    private fun showResponse(str: String) {
        runOnUiThread {
            response_text.text = str
        }
    }

    private fun doGetRequest() {

        // 开启线程发起请求
        thread {

            var connection: HttpURLConnection? = null
            try {
                //
                val url = URL("https://www.baidu.com")

                //
                connection = url.openConnection() as HttpURLConnection

                //
                connection.requestMethod = "GET"

                //
                connection.connectTimeout = 8000
                connection.readTimeout = 8000

                //
                val input = connection.inputStream

                //
                val response = StringBuilder()
                response.append("doGet\n")
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
