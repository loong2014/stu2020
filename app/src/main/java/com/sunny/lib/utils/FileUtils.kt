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
        STORAGE_PATH + File.separator + "SunCamera"
    }

    fun init(context: Context) {

        SunLog.i(TAG, "storagePath :$STORAGE_PATH")
        var file = File(STORAGE_PATH)
        if (!file.exists()) {
            file.mkdirs()
        }

        SunLog.i(TAG, "storagePathCamera :$STORAGE_PATH_CAMERA")
        file = File(STORAGE_PATH_CAMERA)
        if (!file.exists()) {
            file.mkdirs()
        }
    }

    fun buildCameraFilePath(): String {
        val dataTake = System.currentTimeMillis()
        val jpegName = "picture_$dataTake.jpg"
        val filePath = STORAGE_PATH_CAMERA + File.separator + jpegName
        SunLog.i(TAG, "buildCameraFilePath :$filePath")
        return filePath
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

    private fun buildFolder(absPath: String) {
        val file = File(absPath)
        if (!file.exists()) {
            file.mkdirs()
        }
    }

    fun deleteFile(absPath: String): Boolean {
        val file = File(absPath)
        if (file.exists()) {
            return file.delete()
        }
        return false
    }

}