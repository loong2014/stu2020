package com.sunny.family.camera

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import com.bumptech.glide.Glide
import com.sunny.family.R
import com.sunny.lib.base.BaseActivity
import com.sunny.lib.utils.*
import kotlinx.android.synthetic.main.act_camera_sys.*
import java.io.File


class CameraSysActivity : BaseActivity() {

    private val logTag = "CameraSysActivity"

    private val requestCodeTakePicture = 101
    private val requestCodeTakeVideo = 102
    private val requestCodeTakeVoice = 103
    private val requestCodecapture = 104
    private val requestCodeSysFileSelect = 105
    private val requestCodeSysFileOpt = 106

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

        btn_sys_capture.setOnClickListener {
            doTackCapture()
        }

        btn_sys_file_select.setOnClickListener {
            doSysFileSelect()
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

        SunLog.i(logTag, "doTakeVoice")
        startActivityForResult(intent, requestCodeTakeVoice)
    }

    /**
     * 处理录音
     */
    private fun dealTakeVoice(uri: Uri) {
        SunLog.i(logTag, "dealTakeVoice")

        val filePath = CameraHelper.getAudioFilePathFromUri(uri)

        SunLog.i(logTag, "getAudioFilePathFromUri  $filePath")

        CameraHelper.saveVoiceData(filePath, voiceSaveFile)

        CameraHelper.notifyVoiceFileChanged(voiceSaveFile)
    }

    /**
     * 进行截屏
     */
    private fun doTackCapture() {

        val type = 2
        when (type) {
            // 对当前activity进行截屏，无法截取webview
            1 -> {
                window.decorView.isDrawingCacheEnabled = true
                val bitmap = window.decorView.getDrawingCache()
                showBitmap(bitmap)
            }
            // 系统api
            2 -> {
                // 5.0之后开发api
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val intent = (getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager)
                            .createScreenCaptureIntent()
                    SunLog.i(logTag, "doTackCapture")

                    startActivityForResult(intent, requestCodecapture)
                } else {
                    SunToast.show("sdk version must >= 21")
                }
            }
            // 使用反射调用截屏
            3 -> {
//                DisplayMetrics mDisplayMetrics = new DisplayMetrics();
//                float[] dims = { mDisplayMetrics.widthPixels,
//                    mDisplayMetrics.heightPixels };
//                try {
//                    Class<?> demo = Class.forName("android.view.SurfaceControl");
//                    Method method = demo.getDeclaredMethod("screenshot", int.class,int.class);
//                    mScreenBitmap = (Bitmap) method.invoke(null,(int) dims[0],(int) dims[1]);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                val displayMetrics = DisplayMetrics()
                windowManager.defaultDisplay.getMetrics(displayMetrics)

                val dims = arrayOf(
                        displayMetrics.widthPixels,
                        displayMetrics.heightPixels
                )

                try {
                    val demo = Class.forName("android.view.SurfaceControl")
                    val method = demo.getDeclaredMethod("screenshot", Int.javaClass, Int.javaClass)
                    val bitmap: Bitmap = method.invoke(null, dims[0], dims[1]) as Bitmap

                    showBitmap(bitmap)
                } catch (e: Exception) {
                    SunLog.i(logTag, "doTackCapture error :$e")
                }
            }
        }
    }

    /**
     * 处理截屏
     */
    private fun dealTakeCapture(data: Intent) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return
        }
        SunLog.i(logTag, "dealTakeCapture")

        val mediaProjection: MediaProjection = (getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager)
                .getMediaProjection(Activity.RESULT_OK, data)

        val imageReader = ImageReader.newInstance(
                ScreenUtils.screenWidth, ScreenUtils.screenHeight,
                PixelFormat.RGBA_8888, 3)



        SunLog.i(logTag, "setOnImageAvailableListener")

        imageReader.setOnImageAvailableListener(object : ImageReader.OnImageAvailableListener {
            override fun onImageAvailable(reader: ImageReader?) {

                SunLog.i(logTag, "onImageAvailable")

                if (reader == null) {
                    return
                }

                val bitmap: Bitmap? = FileUtils.createBitmap(reader.acquireLatestImage())

                SunLog.i(logTag, "image :$bitmap")
                showBitmap(bitmap)

            }

        }, null)

        val virtualDisplay = mediaProjection.createVirtualDisplay("screen_mirror",
                ScreenUtils.screenWidth, ScreenUtils.screenHeight,
                Resources.getSystem().displayMetrics.densityDpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                imageReader.surface, null, null)
    }


    /**
     * 进行文件选择——SAF
     * https://weilu.blog.csdn.net/article/details/104199446
     */
    private fun doSysFileSelect() {

        //通过系统的文件浏览器选择一个文件
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)

        //筛选，只显示可以“打开”的结果，如文件(而不是联系人或时区列表)
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        //过滤只显示图像类型文件
        intent.type = "image/*"

        startActivityForResult(intent, requestCodeSysFileSelect)
    }

    /**
     * 处理选择的文件
     */
    private fun dealSysFileSelect(uri: Uri) {
        val imageProjection = arrayOf(
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media._ID)

        val cursor = contentResolver.query(uri, imageProjection,
                null, null, null, null)

        if (cursor != null && cursor.moveToFirst()) {

            var displayName = cursor.getString(cursor.getColumnIndexOrThrow(imageProjection[0]))
            var size = cursor.getString(cursor.getColumnIndexOrThrow(imageProjection[1]))
            SunLog.i(logTag, "uri :${uri} , name :$displayName , size :$size")
        }
        cursor?.close()

        Glide.with(this).load(uri).into(iv_camera_picture)
    }

    private fun dealSysSave() {

        val sysType = 1

        val sysIntent = Intent()
        when (sysType) {
            // 保存文件
            1 -> {
                sysIntent.action = Intent.ACTION_CREATE_DOCUMENT
                sysIntent.addCategory(Intent.CATEGORY_OPENABLE)
                sysIntent.type = "text/plain"
                sysIntent.putExtra(Intent.EXTRA_TITLE, "Sun1234.txt")
            }
            2 -> {
                sysIntent.action = Intent.ACTION_OPEN_DOCUMENT_TREE
            }
        }

        startActivityForResult(sysIntent, requestCodeSysFileOpt)
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
                requestCodecapture -> {
                    intent?.let {
                        dealTakeCapture(it)
                    }
                }
                // 系统文件选择
                requestCodeSysFileSelect -> {
                    intent?.data?.let {
                        dealSysFileSelect(it)
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, intent)
    }

    private fun showBitmap(bitmap: Bitmap?) {
        Glide.with(this).load(bitmap).into(iv_camera_picture)
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