package com.sunny.family.camera

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import com.bumptech.glide.Glide
import com.sunny.family.R
import com.sunny.lib.base.BaseActivity
import com.sunny.lib.utils.BackgroundUtils
import com.sunny.lib.utils.FileUtils
import com.sunny.lib.utils.SunLog
import com.sunny.lib.utils.SunToast
import kotlinx.android.synthetic.main.act_camera_sys.*
import java.io.File


class CameraSysActivity : BaseActivity() {

    private val logTag = "CameraSysActivity"

    private val requestCodeTakePicture = 101
    private val requestCodeTakeVideo = 102
    private val requestCodeTakeVoice = 103

    private lateinit var pictureSaveFile: File
    private lateinit var videoSaveFile: File
    private lateinit var voiceSaveFile: File

    private lateinit var pictureSaveFileUri: Uri
    private lateinit var videoSaveFileUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_camera_sys)

        addListener()

        tv_top_tip.text = "系统拍照和系统相册"
    }

    private fun addListener() {

        btn_take_photo.setOnClickListener {
            doTakePicture()
        }

        btn_take_video.setOnClickListener {
            doTakeVideo()
        }

        btn_take_voice.setOnClickListener {
            doTakeVoice()
        }

        btn_scan_photo.setOnClickListener {
            CameraHelper.jumpSysAlbum(this)
        }
    }

    /**
     * 进行拍照
     */
    private fun doTakePicture() {
        pictureSaveFile = CameraHelper.buildPictureFile()
        pictureSaveFileUri = CameraHelper.getUriForFile(pictureSaveFile)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.addCategory(Intent.CATEGORY_DEFAULT)

        // 临时访问权限
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

        //设置拍照后图片保存的位置
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureSaveFileUri)

        SunLog.i(logTag, "doTakePicture")
        startActivityForResult(intent, requestCodeTakePicture)
    }

    /**
     * 处理拍照
     */
    private fun dealTakePicture() {
        SunLog.i(logTag, "dealTakePicture")

        val picturePath: String? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            pictureSaveFile.absolutePath
        } else {
            pictureSaveFileUri.encodedPath
        }

        CameraHelper.notifyPictureFileChanged(pictureSaveFile)

        //
        Glide.with(this).load(picturePath).into(iv_camera_picture)

        //
        val bitmap = BitmapFactory.decodeFile(pictureSaveFile.absolutePath)
        updateBg(bitmap)
    }

    /**
     * 进行拍视频
     */
    private fun doTakeVideo() {
        videoSaveFile = CameraHelper.buildVideoFile()
        videoSaveFileUri = CameraHelper.getUriForFile(videoSaveFile)

        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        intent.addCategory(Intent.CATEGORY_DEFAULT)

        // 临时访问权限
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

        //设置视频录制的最长时间 30分钟
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30 * 60)

        //设置视频的最大大小 1000M
        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1000 * 1024 * 1024L)

        //设置视频录制的画质
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1)

        //设置拍照后图片保存的位置
        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoSaveFileUri)

        SunLog.i(logTag, "doTakeVideo")
        startActivityForResult(intent, requestCodeTakeVideo)
    }

    /**
     * 处理拍视频
     */
    private fun dealTakeVideo() {
        SunLog.i(logTag, "dealTakeVideo")

        val videoPath: String? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            videoSaveFile.absolutePath
        } else {
            videoSaveFileUri.encodedPath
        }

        CameraHelper.notifyVideoFileChanged(videoSaveFile)

        if (videoPath.isNullOrEmpty()) {
            SunToast.show("video path is empty")

        } else {
            val bitmap: Bitmap = FileUtils.getVideoThumb(videoPath)

            //
            iv_camera_picture.setImageBitmap(bitmap)

            //
            updateBg(bitmap)
        }
    }

    /**
     * 进行录音
     */
    private fun doTakeVoice() {
        voiceSaveFile = CameraHelper.buildVoiceFile()

        val intent = Intent()
        intent.action = MediaStore.Audio.Media.RECORD_SOUND_ACTION

        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

        SunLog.i(logTag, "takeVoice")
        startActivityForResult(intent, requestCodeTakeVoice)
    }

    /**
     * 处理录音
     */
    private fun dealTakeVoice(uri: Uri) {
        val filePath = CameraHelper.getAudioFilePathFromUri(uri)

        SunLog.i(logTag, "getAudioFilePathFromUri  $filePath")

        CameraHelper.saveVoiceData(filePath, voiceSaveFile)

        CameraHelper.notifyVoiceFileChanged(voiceSaveFile)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        SunLog.i(logTag, "onActivityResult requestCode :$requestCode , resultCode :$resultCode")

        if (Activity.RESULT_OK == resultCode) {

            when (requestCode) {

                // 图片
                requestCodeTakePicture -> {
                    dealTakePicture()
                }

                // 视频
                requestCodeTakeVideo -> {
                    dealTakeVideo()
                }

                // 音频
                requestCodeTakeVoice -> {
                    intent?.data?.let {
                        dealTakeVoice(it)
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, intent)
    }

    private fun updateBg(bitmap: Bitmap) {

        BackgroundUtils.getBackgroundColor(bitmap, object : BackgroundUtils.IBackgroundColorCallback {
            override fun onGetBackgroundColor(colors: IntArray) {
                runOnUiThread {
                    BackgroundUtils.updateBackground(act_camera, colors)
                }
            }

            override fun onGetBackgroundColorError() {
                SunLog.i(logTag, "updateBg  onGetBackgroundColorError")
            }
        })
    }

    private fun updateBg(imgUrl: String) {

        BackgroundUtils.getBackgroundColor(imgUrl, object : BackgroundUtils.IBackgroundColorCallback {
            override fun onGetBackgroundColor(colors: IntArray) {
                runOnUiThread {
                    BackgroundUtils.updateBackground(act_camera, colors)
                }
            }

            override fun onGetBackgroundColorError() {
                SunLog.i(logTag, "updateBg  onGetBackgroundColorError")
            }
        })
    }
}