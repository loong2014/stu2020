package com.sunny.family.camera

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import com.bumptech.glide.Glide
import com.sunny.family.R
import com.sunny.lib.utils.SunLog
import com.sunny.player.BasePlayerActivity
import kotlinx.android.synthetic.main.act_camera.*
import java.io.File


class CameraSysActivity : BasePlayerActivity() {

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

        addVideoListener()
        setVideoView(video_view)
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

    private fun addVideoListener() {
        btn_video_start.setOnClickListener {
            playStart()
        }

        btn_video_stop.setOnClickListener {
            playStop()
        }
    }


    /**
     * 调用系统相机拍照
     */
    private fun takePicture() {
        cameraSaveFile = CameraHelper.buildPictureFile()
        cameraSaveFileUri = CameraHelper.getUriForFile(cameraSaveFile)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.addCategory(Intent.CATEGORY_DEFAULT)

        // 临时访问权限
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

        //设置拍照后图片保存的位置
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraSaveFileUri)

        SunLog.i(logTag, "takePicture")
        startActivityForResult(intent, requestCodeTakePicture)
    }

    /**
     * 调用系统相机拍视频
     */
    private fun takeVideo() {
        cameraSaveFile = CameraHelper.buildVideoFile()
        cameraSaveFileUri = CameraHelper.getUriForFile(cameraSaveFile)

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
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraSaveFileUri)

        SunLog.i(logTag, "takeVideo")
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

        SunLog.i(logTag, "takeVoice")
        startActivityForResult(intent, requestCodeTakeVoice)
    }

    private fun getFilePath(): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cameraSaveFile.absolutePath
        } else {
            cameraSaveFileUri.encodedPath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        SunLog.i(logTag, "onActivityResult requestCode :$requestCode , resultCode :$resultCode")

        video_control_layout.visibility = View.INVISIBLE

        if (Activity.RESULT_OK == resultCode) {

            when (requestCode) {

                requestCodeTakePicture -> {
                    val picturePath = getFilePath()
                    SunLog.i(logTag, "onActivityResult  picturePath :$picturePath")
                    Glide.with(this).load(picturePath).into(iv_camera_last)
                }

                requestCodeTakeVideo -> {
                    val videoPath = getFilePath()
                    if (videoPath != null) {
                        setVideoUri(videoPath)
                        video_control_layout.visibility = View.VISIBLE
                    }
                }

                requestCodeTakeVoice -> {

                }
            }

            CameraHelper.notifyPhotoAlbumChange(cameraSaveFile)
        }
        super.onActivityResult(requestCode, resultCode, intent)
    }
}