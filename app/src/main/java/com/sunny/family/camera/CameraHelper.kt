package com.sunny.family.camera

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.sunny.lib.utils.AppConfigUtils
import com.sunny.lib.utils.ContextProvider
import java.io.File

class CameraHelper {

    companion object {

        private val CameraFileProvideAuthorities by lazy {
            AppConfigUtils.pkgName + ".fileprovider" //FileProvider的签名(后面会介绍)
        }

        fun getUriForFile(imageFile: File): Uri {
            return FileProvider.getUriForFile(ContextProvider.appContext,
                    CameraFileProvideAuthorities, imageFile)
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