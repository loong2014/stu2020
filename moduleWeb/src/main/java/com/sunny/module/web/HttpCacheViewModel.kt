package com.sunny.module.web

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sunny.module.web.api.LeApiListResponse
import com.sunny.module.web.api.webApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HttpCacheViewModel : ViewModel() {

    val resultLiveData = MutableLiveData<String>()

    fun doRequest() {
        webApi.getChannelDataList("647")
            .enqueue(object : Callback<LeApiListResponse> {

                override fun onFailure(
                    call: Call<LeApiListResponse>,
                    t: Throwable
                ) {
                    resultLiveData.postValue("ServiceError")
                }

                override fun onResponse(
                    call: Call<LeApiListResponse>,
                    response: Response<LeApiListResponse>
                ) {
                    val result = response.body()?.data?.let { list ->
                        if (list.isNullOrEmpty()) {
                            "ServiceError"
                        } else {
                            val sb = StringBuilder()
                            list.forEach {
                                sb.append("\n\n>>>")
                                sb.append(it.title)
                                sb.append("<<<")
                                it.dataList?.forEachIndexed { index, item ->
                                    sb.append("\nNo.$index ${item.name} --> ${item.subName}")
                                }
                            }
                            sb.toString()
                        }
                    }
                    resultLiveData.postValue(result ?: "ServiceError")
                }
            })
    }
}