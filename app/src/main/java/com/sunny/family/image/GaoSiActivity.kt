package com.sunny.family.image

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sunny.family.R
import com.sunny.lib.base.BaseActivity
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.act_gaosi.*


/**
 * Created by zhangxin17 on 2020/4/17
 */
class GaoSiActivity : BaseActivity() {

    val imgUrl = "http://puui.qpic.cn/vcover_vt_pic/0/mzc00200len60tj1576067642/260"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.act_gaosi)

        initView()
    }

    private fun initView() {
        iv_bg.setBackgroundResource(R.drawable.chuntian)

        btn_gaosi.setOnClickListener {
            doGaoSiEffect()
        }

        btn_normal.setOnClickListener {
            doNormalEffect()
        }
    }

    private fun doGaoSiEffect() {
        iv_image.visibility = View.GONE
        iv_gaosi.visibility = View.VISIBLE

        Glide.with(this)
                .load(imgUrl)
                .apply(RequestOptions.bitmapTransform(BlurTransformation(this, 14, 3)))
                .into(iv_gaosi)

    }

    private fun doNormalEffect() {

        iv_gaosi.visibility = View.GONE
        iv_image.visibility = View.VISIBLE
        Glide.with(this)
                .load(imgUrl)
                .into(iv_image)
    }
}