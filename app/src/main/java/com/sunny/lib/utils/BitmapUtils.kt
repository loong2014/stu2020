package com.sunny.lib.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by zhangxin17 on 2020-01-19
 */
object BitmapUtils {

    fun getBitmap(url: String, callback: IBitmapCallback) {

        HandlerUtils.workHandler.post {
            try {
                val imageUrl = URL(url)

                val imageConn: HttpURLConnection = imageUrl.openConnection() as HttpURLConnection
                imageConn.doOutput = true
                imageConn.connect()

                val imageIs: InputStream = imageConn.inputStream

                val bitmap = BitmapFactory.decodeStream(imageIs)
                imageIs.close()

                callback.onGetBitmap(bitmap)
            } catch (e: Exception) {
                callback.onGetBitmap(null)
            }
        }
    }

    fun getLocalBitmap(filePath:String){


        val bitmap =BitmapFactory.decodeFile(filePath)

//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;// 设置为ture,只读取图片的大小，不把它加载到内存中去
//        BitmapFactory.decodeFile(filePath, options);
//
//        // Calculate inSampleSize
//        options.inSampleSize = calculateInSampleSize(options, 480, 800);// 此处，选取了480x800分辨率的照片
//
//        // Decode bitmap with inSampleSize set
//        options.inJustDecodeBounds = false;// 处理完后，同时需要记得设置为false
//
//        return BitmapFactory.decodeFile(filePath, options);
    }


    interface IBitmapCallback {
        fun onGetBitmap(bitmap: Bitmap?)
    }
}