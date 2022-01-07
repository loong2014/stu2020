package com.sunny.module.web

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.sunny.lib.common.base.BaseActivity
import com.sunny.lib.base.utils.ContextProvider
import com.sunny.module.web.api.curCacheType
import com.sunny.module.web.api.curHasNet
import com.sunny.module.web.api.setCacheType
import com.sunny.module.web.api.setHasNet
import kotlinx.android.synthetic.main.web_activity_cache.*

class HttpCacheActivity : BaseActivity() {

    lateinit var mViewModel: HttpCacheViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(ContextProvider.appContext as Application)
        ).get(HttpCacheViewModel::class.java)

        setContentView(R.layout.web_activity_cache)

        btn_request_type.text = "请求类型($curCacheType)"
        btn_request_type.setOnClickListener {
            val newValue = if (curCacheType == 0) 1 else 0
            setCacheType(newValue)
            btn_request_type.text = "请求类型($newValue)"
        }

        btn_request_net.text = "网络类型($curHasNet)"
        btn_request_net.setOnClickListener {
            val newValue = !curHasNet
            setHasNet(newValue)
            btn_request_net.text = "网络类型($newValue)"
        }
        btn_request.setOnClickListener {
            mViewModel.doRequest()
        }

        mViewModel.resultLiveData.observe(this) {
            tv_response.text = it ?: "None"
        }
    }
}