package com.sunny.lib.utils

import android.content.Context
import android.graphics.Bitmap
import android.media.Image
import android.media.MediaMetadataRetriever
import android.os.Environment
import java.io.*
import java.nio.ByteBuffer

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

    val PICTURE_FOLDER_PATH: String by lazy {
        buildFolder(STORAGE_PATH_CAMERA + File.separator + "picture")
    }

    val VIDEO_FOLDER_PATH: String by lazy {
        buildFolder(STORAGE_PATH_CAMERA + File.separator + "DCIM/Camera")
    }

    val VOICE_FOLDER_PATH: String by lazy {
        buildFolder(STORAGE_PATH_CAMERA + File.separator + "voice")
    }

    fun init(context: Context) {
        SunLog.i(TAG, "storagePath :$STORAGE_PATH")
        SunLog.i(TAG, "storagePathCamera :$STORAGE_PATH_CAMERA")
    }

    fun isPictureFile(file: File?): Boolean {
        val fileName = file?.name

        return when {
            fileName.isNullOrEmpty() -> false
            fileName.endsWith(".png") -> true
            fileName.endsWith(".jpg") -> true
            else -> false
        }
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

    fun isVoiceFile(file: File?): Boolean {
        val fileName = file?.name

        return if (fileName == null) {
            false
        } else {
            when {
                fileName.endsWith(".amr") -> true
                else -> false
            }
        }
    }

    fun getVideoThumb(path: String): Bitmap {
        val media = MediaMetadataRetriever()
        media.setDataSource(path)
        return media.getFrameAtTime(0)
    }

//
//
//    public  static Bitmap getVideoThumb(String path) {
//
//        MediaMetadataRetriever media = new MediaMetadataRetriever();
//
//        media.setDataSource(path);
//
//        return  media.getFrameAtTime();
//
//    }

    fun createBitmap(image: Image?): Bitmap? {
        if (image == null) {
            return null
        }

        if (image.planes.isEmpty()) {
            return null
        }

        val plane: Image.Plane = image.planes[0]

        val width = ScreenUtils.screenWidth
        val height = ScreenUtils.screenHeight

        val buffer: ByteBuffer = plane.buffer
        val pixelStride = plane.pixelStride
        val rowStride = plane.rowStride
        val rowPadding = rowStride - pixelStride * width

        val bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888)
        bitmap.copyPixelsFromBuffer(buffer)
//        //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

        image.close()

        return bitmap
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

    /**
     * 保存数据
     */
    fun saveFileToFile(fromPath: String, toPath: String): Boolean {

        var fromIs: InputStream? = null
        var toOs: OutputStream? = null

        try {
            fromIs = FileInputStream(fromPath)
            toOs = FileOutputStream(toPath)
            val buffer = ByteArray(1024)
            var len: Int
            while (((fromIs.read(buffer)).also { len = it }) != -1) {
                toOs.write(buffer, 0, len)
            }
            return true
        } catch (e: Exception) {
            SunLog.e("FileUtils", "saveFileToFile error :$e.toString()")
        } finally {
            try {
                toOs?.close()
                fromIs?.close()
            } catch (e: Exception) {
            }
        }
        return false
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