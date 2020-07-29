package com.sunny.family.arcode

import android.os.Bundle
import com.sunny.family.R
import com.sunny.lib.base.BaseActivity
import com.sunny.lib.utils.SunLog
import kotlinx.android.synthetic.main.act_qrcode.*

/**
 * Created by zhangxin17 on 2020/7/29
 */
class QrCodeActivity : BaseActivity() {

    companion object {
        const val TAG = "QrCode-QrCodeActivity"
        const val QrUrl = "https://www.baidu.com/"

        //        const val QrImgSize = 437
        const val QrImgSize = 500
    }

    private val mQrModel: QrCodeMode by lazy {
        QrCodeMode()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_qrcode)

        initView()
    }

    private fun initView() {
        btn_qr_none.setOnClickListener {
            doShowDefQr()
        }

        btn_qr_login.setOnClickListener {
            doShowQrImageWithIcon(R.mipmap.icon_qr_login)
        }

        btn_qr_pay.setOnClickListener {
            doShowQrImageWithIcon(R.mipmap.icon_qr_pay)
        }

        btn_qr_vip.setOnClickListener {
            doShowQrImageWithIcon(R.mipmap.icon_qr_vip)
        }
    }

    private fun doShowDefQr() {
        SunLog.i(TAG, "build begin")
        val bmp = mQrModel.createQrBitmap(QrUrl, QrImgSize, QrImgSize)
        SunLog.i(TAG, "build finish")
        bmp?.let {
            iv_qr_img.setImageBitmap(it)
        }
    }

    private fun doShowQrImageWithIcon(iconResId: Int) {
        val bmp = mQrModel.createQrBitmapWithIcon(QrUrl, QrImgSize, QrImgSize, iconResId)

        bmp?.let {
            iv_qr_img.setImageBitmap(it)
        }

    }
}