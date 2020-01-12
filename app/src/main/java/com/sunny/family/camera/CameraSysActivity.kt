package com.sunny.family.camera

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import com.bumptech.glide.Glide
import com.sunny.family.R
import com.sunny.lib.base.BaseActivity
import com.sunny.lib.utils.FileUtils
import com.sunny.lib.utils.SunLog
import kotlinx.android.synthetic.main.act_camera.*
import java.io.File


class CameraSysActivity : BaseActivity() {

    private val logTag = "CameraSysActivity"
    private val requestCodeCaptureRaw = 6 //startActivityForResult时的请求码

    private lateinit var cameraSaveFile: File

    private lateinit var cameraSaveFileUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_camera)

        addListener()

        tv_top_tip.text = "系统拍照和系统相册"
    }

    private fun addListener() {

        btn_take_photo.setOnClickListener {
            takePhotoBySysCamera()
        }

        btn_scan_photo.setOnClickListener {
            CameraHelper.jumpSysAlbum(this)
        }
    }

    /**
     * 调用系统相机拍照
     */
    private fun takePhotoBySysCamera() {
        cameraSaveFile = File(FileUtils.buildCameraFilePath())

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        //如果是7.0以上，使用FileProvider，否则会报错
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cameraSaveFileUri = CameraHelper.getUriForFile(cameraSaveFile)

            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

        } else {
            cameraSaveFileUri = Uri.fromFile(cameraSaveFile)
        }

        //设置拍照后图片保存的位置
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraSaveFileUri)

        //设置图片保存的格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())

        intent.resolveActivity(packageManager)?.let {
            startActivityForResult(intent, requestCodeCaptureRaw) //调起系统相机
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {

        if (Activity.RESULT_OK == resultCode) {

            if (requestCodeCaptureRaw == requestCode) {

                val photoPath: String? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    cameraSaveFile.absolutePath
                } else {
                    cameraSaveFileUri.encodedPath
                }

                SunLog.i(logTag, "onActivityResult  photoPath :$photoPath")
                Glide.with(this).load(photoPath).into(iv_camera_last)

                CameraHelper.notifyPhotoAlbumChange(cameraSaveFile)
            }
        }
        super.onActivityResult(requestCode, resultCode, intent)
    }
}