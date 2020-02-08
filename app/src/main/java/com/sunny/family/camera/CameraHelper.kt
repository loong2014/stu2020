package com.sunny.family.camera

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.sunny.lib.utils.AppConfigUtils
import com.sunny.lib.utils.ContextProvider
import com.sunny.lib.utils.FileUtils
import com.sunny.lib.utils.SunLog
import java.io.File

object CameraHelper {

    private val logTag = "CameraHelper"

    private val CameraFileProvideAuthorities by lazy {
        AppConfigUtils.pkgName + ".fileprovider" //FileProvider的签名(后面会介绍)
    }

    fun buildPictureFile(): File {
        val dataTake = System.currentTimeMillis()
        val jpegName = "picture_$dataTake.jpg"

        val filePath = FileUtils.PICTURE_FOLDER_PATH + File.separator + jpegName
        SunLog.i(logTag, "buildPictureFile :$filePath")

        val file = File(filePath)
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        file.setWritable(true) // 不加这句会报Read-only警告
        return file
    }

    fun buildVideoFile(): File {
        val dataTake = System.currentTimeMillis()
        val videoName = "video_$dataTake.mp4"

        val filePath = FileUtils.VIDEO_FOLDER_PATH + File.separator + videoName
        SunLog.i(logTag, "buildVideoFile :$filePath")

        val file = File(filePath)
        file.setWritable(true)
        return file
    }

    fun buildVoiceFile(): File {
        val dataTake = System.currentTimeMillis()
        val voiceName = "voice_$dataTake.amr"

        val filePath = FileUtils.VOICE_FOLDER_PATH + File.separator + voiceName
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

    fun getAudioFilePathFromUri(uri: Uri): String? {

        var filePath: String? = null

        var cursor: Cursor? = null
        try {
            cursor = ContextProvider.contentResolver.query(uri, null, null, null, null)

            cursor?.moveToFirst()

            cursor?.getColumnIndex(MediaStore.Audio.AudioColumns.DATA)
                    ?.let {
                        filePath = cursor.getString(it)
                    }

        } catch (e: Exception) {
        } finally {
            cursor?.close()
        }
        return filePath
    }

    fun saveVoiceData(dataPath: String?, file: File): Boolean {
        if (dataPath.isNullOrEmpty()) {
            return false
        }
        return FileUtils.saveFileToFile(dataPath, file.absolutePath)
    }

    fun notifyPictureFileChanged(file: File) {

        // 将刚拍照的相片在相册中显示，必须使用fromFile
        ContextProvider.appContext.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)))

        // 该方式会在系统相册生产图片对应的缩略图，不建议使用
//                val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(cameraSaveFileUri))
//                MediaStore.Images.Media.insertImage(contentResolver, bitmap, "sunny_demo", "from family")

    }

    fun notifyVideoFileChanged(file: File) {
        ContextProvider.appContext.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)))
    }

    fun notifyVoiceFileChanged(imageFile: File) {

    }

}