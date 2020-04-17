package com.sunny.family.image

import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.widget.ImageView
import com.facebook.drawee.view.SimpleDraweeView
import com.sunny.family.R
import com.sunny.lib.base.BaseActivity
import com.sunny.lib.image.ImageConfig
import com.sunny.lib.utils.SunLog
import kotlinx.android.synthetic.main.act_image_fresco.*


class FrescoImageActivity : BaseActivity() {
    val logTag = "Image-FrescoImageActivity"

    lateinit var image1: SimpleDraweeView
    lateinit var image2: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_image_fresco)

        initView()
    }

    private fun initView() {
        image1 = iv_image as SimpleDraweeView
        image2 = iv_image_2

        btn_cancel_load.setOnClickListener {
        }

        btn_show_net_image.setOnClickListener {
            showImage(ImageConfig.NetImgUrl, image1)
        }

        btn_show_net_gif.setOnClickListener {

        }
    }

    private fun showImage(url: String, view: SimpleDraweeView) {
        val uri = Uri.parse(url)

        view.setImageURI(uri)
    }

    private fun showImageTip(msg: String, reset: Boolean = false) {

        val isUiThread: Boolean = Thread.currentThread() == Looper.getMainLooper().thread
        SunLog.i(logTag, "$isUiThread : $msg")

        if (reset) {
            tv_image_tip.text = ""

        } else {
            var tip = tv_image_tip.text
            tip = "$tip\n$msg"
            tv_image_tip.text = tip
        }
    }

}

