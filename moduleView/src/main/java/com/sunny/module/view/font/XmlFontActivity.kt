package com.sunny.module.view.font

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.sunny.lib.common.base.BaseActivity
import com.sunny.lib.common.router.RouterConstant
import com.sunny.module.view.R
import com.sunny.module.view.databinding.ViewActXmlFontBinding
import com.sunny.module.view.xxx.adapter.StringAdapter
import com.sunny.module.view.xxx.adapter.StringModel

@Route(path = RouterConstant.View.PageFont)
class XmlFontActivity : BaseActivity() {

    lateinit var mActBinding: ViewActXmlFontBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActBinding = DataBindingUtil.setContentView(this, R.layout.view_act_xml_font)
        initView()
    }

    private fun initView() {

        val list = mutableListOf<StringModel>(
            StringModel("Hello World!", 1),
            StringModel("Hello World!", 2),
            StringModel("Hello World!", 3)
        )
        mActBinding.recyclerView.apply {

            layoutManager = LinearLayoutManager(this@XmlFontActivity)
            adapter = StringAdapter(list)
        }

    }
}