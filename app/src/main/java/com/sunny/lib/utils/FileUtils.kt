package com.sunny.lib.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

/**
 * Created by zhangxin17 on 2020-01-10
 */
object FileUtils {

    val TAG = "FileUtils"

    val STORAGE_PATH: String by lazy {
        Environment.getExternalStorageDirectory().absolutePath
    }

    val STORAGE_PATH_CAMERA: String by lazy {
        buildFolder(STORAGE_PATH + File.separator + "SunCamera")
    }

    val PICTURE_FOLDER_PATG: String by lazy {
        buildFolder(STORAGE_PATH_CAMERA + File.separator + "picture")
    }

    val VIDEO_FOLDER_PATG: String by lazy {
        buildFolder(STORAGE_PATH_CAMERA + File.separator + "/DCIM/Camera")
    }

    val VOICE_FOLDER_PATG: String by lazy {
        buildFolder(STORAGE_PATH_CAMERA + File.separator + "voice")
    }

    fun init(context: Context) {
        SunLog.i(TAG, "storagePath :$STORAGE_PATH")
        SunLog.i(TAG, "storagePathCamera :$STORAGE_PATH_CAMERA")
    }

    fun isPictureFile(file: File?): Boolean {
        file?.let {
            return if (it.isFile) {
                return when (getSuffix(it.name)) {
                    "png", "jpg" -> true
                    else -> false
                }
            } else {
                false
            }
        }
        return false
    }

    fun isVideoFile(file: File?): Boolean {
        file?.let {
            return if (it.isFile) {
                return when (getSuffix(it.name)) {
                    "mp4" -> true
                    else -> false
                }
            } else {
                false
            }
        }
        return false
    }

    /**
     * 保存图片
     */
    fun saveCameraBitmap(bitmap: Bitmap): String {
        val dataTake = System.currentTimeMillis()
        val jpegName = STORAGE_PATH_CAMERA + File.separator +
                "picture_$dataTake.jpg"

        var fos: FileOutputStream? = null
        var bos: BufferedOutputStream? = null

        try {
            fos = FileOutputStream(jpegName)
            bos = BufferedOutputStream(fos)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            return jpegName
        } catch (e: Exception) {
            SunLog.e("FileUtils", "saveCameraBitmap error :$e.toString()")
        } finally {
            try {
                bos?.flush()
                bos?.close()
                fos?.close()
            } catch (e: Exception) {
            }
        }

        return ""
    }

    private fun buildFolder(absPath: String): String {
        val file = File(absPath)
        if (!file.exists()) {
            file.mkdirs()
        }
        return absPath
    }

    private fun getSuffix(str: String): String {
        val list = str.split('.')
        return list.last()
    }


    fun deleteFile(absPath: String): Boolean {
        val file = File(absPath)
        if (file.exists()) {
            return file.delete()
        }
        return false
    }

}