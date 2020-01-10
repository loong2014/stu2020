package com.sunny.family.camera

import android.graphics.Bitmap
import android.os.Bundle
import com.cjt2325.cameralibrary.JCameraView
import com.cjt2325.cameralibrary.listener.ErrorListener
import com.cjt2325.cameralibrary.listener.JCameraListener
import com.sunny.family.R
import com.sunny.lib.base.BaseActivity
import com.sunny.lib.utils.FileUtils
import com.sunny.lib.utils.SunLog
import com.sunny.lib.utils.ToastUtils
import kotlinx.android.synthetic.main.act_camera.*


class CameraActivity : BaseActivity() {
    val _tag = "CameraActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_camera)

        initCamera()
    }

    private fun initCamera() {
        //设置视频保存路径
        jCameraView.setSaveVideoPath(FileUtils.STORAGE_PATH_CAMERA)

        //设置只能录像或只能拍照或两种都可以（默认两种都可以）
        jCameraView.setFeatures(JCameraView.BUTTON_STATE_BOTH)

        //设置视频质量
        jCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_MIDDLE)

        //JCameraView监听
        jCameraView.setErrorLisenter(object : ErrorListener {
            override fun AudioPermissionError() {
                SunLog.e(_tag, "AudioPermissionError")
            }

            override fun onError() {
                SunLog.e(_tag, "open camera error")
            }
        })

        jCameraView.setJCameraLisenter(object : JCameraListener {
            override fun recordSuccess(url: String?, firstFrame: Bitmap?) {
                SunLog.e(_tag, "url = $url")
            }

            override fun captureSuccess(bitmap: Bitmap?) {
                SunLog.e(_tag, "bitmap = " + bitmap?.width)
            }
        })

        jCameraView.setLeftClickListener { finish() }

        jCameraView.setRightClickListener { ToastUtils.show("Right") }

    }

    override fun onResume() {
        super.onResume()
        jCameraView.onResume()
    }

    override fun onPause() {
        super.onPause()
        jCameraView.onPause()
    }


}