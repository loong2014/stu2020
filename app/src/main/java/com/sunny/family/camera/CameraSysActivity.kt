package com.sunny.family.camera

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.sunny.family.R
import com.sunny.lib.base.BaseActivity
import com.sunny.lib.utils.AppConfigUtils
import com.sunny.lib.utils.FileUtils
import com.sunny.lib.utils.SunLog
import kotlinx.android.synthetic.main.act_camera.*
import java.io.File


class CameraSysActivity : BaseActivity() {

    private val TAG = "CameraSysActivity"
    private val requestCodeCaptureRaw = 6 //startActivityForResult时的请求码

    private val fileProviderAuthority by lazy {
        AppConfigUtils.pkgName + ".fileProvider" //FileProvider的签名(后面会介绍)
    }

    private lateinit var cameraSaveFile: File

    private lateinit var cameraSaveFileUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_camera)

        addListener()
    }

    private fun addListener() {

        btn_take_photo.setOnClickListener {
            takePhotoBySysCamera()
        }

        btn_scan_photo.setOnClickListener {

            val intent = Intent()
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                intent.action = Intent.ACTION_GET_CONTENT
            } else {
                intent.action = Intent.ACTION_OPEN_DOCUMENT
            }
            startActivity(intent)
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
            cameraSaveFileUri = FileProvider.getUriForFile(this, fileProviderAuthority, cameraSaveFile)

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

                var photoPath: String? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    cameraSaveFile.absolutePath
                } else {
                    cameraSaveFileUri.encodedPath
                }

                SunLog.i(TAG, "onActivityResult  photoPath :$photoPath")
                Glide.with(this).load(photoPath).into(iv_camera_last)

                // 将刚拍照的相片在相册中显示，必须使用fromFile
                sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(cameraSaveFile)))

                // 该方式会在系统相册生产图片对应的缩略图，不建议使用
//                val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(cameraSaveFileUri))
//                MediaStore.Images.Media.insertImage(contentResolver, bitmap, "sunny_demo", "from family")
            }
        }
        super.onActivityResult(requestCode, resultCode, intent)
    }
}