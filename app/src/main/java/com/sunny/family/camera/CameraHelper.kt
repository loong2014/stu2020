package com.sunny.family.camera

import android.content.Intent
import android.net.Uri
import android.os.Build
import com.sunny.lib.utils.ContextProvider
import java.io.File

class CameraHelper {

    companion object {

        fun jumpSysAlbum() {

            val intent = Intent()
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                intent.action = Intent.ACTION_GET_CONTENT
            } else {
                intent.action = Intent.ACTION_OPEN_DOCUMENT
            }
            ContextProvider.appContext.startActivity(intent)
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