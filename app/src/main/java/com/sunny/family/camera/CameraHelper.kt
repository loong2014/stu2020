package com.sunny.family.camera

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.sunny.lib.utils.AppConfigUtils
import com.sunny.lib.utils.ContextProvider
import com.sunny.lib.utils.FileUtils
import com.sunny.lib.utils.SunLog
import java.io.File

class CameraHelper {

    companion object {

        private val logTag = "CameraHelper"

        private val CameraFileProvideAuthorities by lazy {
            AppConfigUtils.pkgName + ".fileprovider" //FileProvider的签名(后面会介绍)
        }

        fun buildPictureFile(): File {
            val dataTake = System.currentTimeMillis()
            val jpegName = "picture_$dataTake.jpg"

            val filePath = FileUtils.STORAGE_PATH_CAMERA + File.separator +
                    "picture" + File.separator + jpegName
            SunLog.i(logTag, "buildPictureFile :$filePath")

            val file = File(filePath)
            file.setWritable(true) // 不加这句会报Read-only警告
            return file
        }

        fun buildVideoFile(): File {
            val dataTake = System.currentTimeMillis()
            val videoName = "video_$dataTake.mp4"

            // 不添加"/DCIM/Camera"目录，相册中无法查看视频文件
            val filePath = FileUtils.STORAGE_PATH_CAMERA + File.separator +
                    "/DCIM/Camera" + File.separator + videoName
            SunLog.i(logTag, "buildVideoFile :$filePath")

            val file = File(filePath)
            file.setWritable(true)
            return file
        }

        fun buildVoiceFile(): File {
            val dataTake = System.currentTimeMillis()
            val voiceName = "voice_$dataTake.amr"

            val filePath = FileUtils.STORAGE_PATH_CAMERA + File.separator +
                    "voice" + File.separator + voiceName
            SunLog.i(logTag, "buildVoiceFile :$filePath")

            val file = File(filePath)
            file.setWritable(true)
            return file
        }

        fun getUriForFile(file: File): Uri {
            //如果是7.0以上，使用FileProvider，否则会报错
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(ContextProvider.appContext, CameraFileProvideAuthorities, file)
            } else {
                Uri.fromFile(file)
            }
        }

        fun jumpSysAlbum(context: Context?) {

            val intent = Intent()
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                intent.action = Intent.ACTION_GET_CONTENT
            } else {
                intent.action = Intent.ACTION_OPEN_DOCUMENT
            }

            if (context == null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                ContextProvider.appContext.startActivity(intent)
            } else {
                context.startActivity(intent)
            }
        }

        fun notifyPhotoAlbumChange(imageFile: File) {

            // 将刚拍照的相片在相册中显示，必须使用fromFile
            ContextProvider.appContext.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(imageFile)))

            // 该方式会在系统相册生产图片对应的缩略图，不建议使用
//                val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(cameraSaveFileUri))
//                MediaStore.Images.Media.insertImage(contentResolver, bitmap, "sunny_demo", "from family")

        }
    }


}