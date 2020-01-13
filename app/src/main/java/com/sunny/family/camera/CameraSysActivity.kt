package com.sunny.family.camera

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import com.bumptech.glide.Glide
import com.sunny.family.R
import com.sunny.lib.base.BaseActivity
import com.sunny.lib.utils.SunLog
import kotlinx.android.synthetic.main.act_camera.*
import java.io.File


class CameraSysActivity : BaseActivity() {

    private val logTag = "CameraSysActivity"
    private val requestCodeTakePicture = 101
    private val requestCodeTakeVideo = 102
    private val requestCodeTakeVoice = 103

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
            takePicture()
        }

        btn_take_video.setOnClickListener {
            takeVideo()
        }

        btn_take_voice.setOnClickListener {
            takeVoice()
        }

        btn_scan_photo.setOnClickListener {
            CameraHelper.jumpSysAlbum(this)
        }
    }

    /**
     * 调用系统相机拍照
     */
    private fun takePicture() {
        cameraSaveFile = CameraHelper.buildPictureFile()

        val intent = Intent()

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
        intent.action = MediaStore.ACTION_IMAGE_CAPTURE
        intent.addCategory(Intent.CATEGORY_DEFAULT)
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())

        startActivityForResult(intent, requestCodeTakePicture)
    }

    /**
     * 调用系统相机拍视频
     */
    private fun takeVideo() {
        cameraSaveFile = CameraHelper.buildVideoFile()
        cameraSaveFileUri = CameraHelper.getUriForFile(cameraSaveFile)

        val intent = Intent()

        intent.addCategory(Intent.CATEGORY_DEFAULT)

        //设置图片保存的格式
        intent.action = MediaStore.ACTION_VIDEO_CAPTURE

        // 临时访问权限
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

        //设置视频录制的最长时间 30分钟
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30 * 60)

        //设置视频的最大大小 1000M
        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1000 * 1024 * 1024L)

        //设置视频录制的画质
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1)

        //设置拍照后图片保存的位置
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraSaveFileUri)

        startActivityForResult(intent, requestCodeTakeVideo)
    }

    /**
     * 调用系统录音
     */
    private fun takeVoice() {

        cameraSaveFile = CameraHelper.buildVoiceFile()

        val intent = Intent()
        intent.action = MediaStore.Audio.Media.RECORD_SOUND_ACTION

        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

        startActivityForResult(intent, requestCodeTakeVoice)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {

        if (Activity.RESULT_OK == resultCode) {

            when (requestCode) {
                requestCodeTakeVideo -> {

                }

                requestCodeTakePicture -> {

                    val photoPath: String? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        cameraSaveFile.absolutePath
                    } else {
                        cameraSaveFileUri.encodedPath
                    }


                    SunLog.i(logTag, "onActivityResult  photoPath :$photoPath")
                    Glide.with(this).load(photoPath).into(iv_camera_last)

                    CameraHelper.notifyPhotoAlbumChange(cameraSaveFile)
                }

                requestCodeTakeVoice -> {

                }

            }

            CameraHelper.notifyPhotoAlbumChange(cameraSaveFile)
        }
        super.onActivityResult(requestCode, resultCode, intent)
    }
}