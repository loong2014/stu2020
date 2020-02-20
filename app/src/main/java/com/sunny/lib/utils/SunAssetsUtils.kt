package com.sunny.lib.utils

import android.content.Context
import android.content.res.AssetManager
import java.io.ByteArrayOutputStream
import java.io.InputStream

object SunAssetsUtils {

    fun getLocalAssetsFileStr(fileName: String): String? {

        SunLog.i("SunAssetsUtils", "getLocalAssetsFileStr :$fileName")

        // 读取assets下的配置文件
        val context: Context = ContextProvider.appContext

        var fileString: String? = null

        val am: AssetManager = context.assets

        val baos = ByteArrayOutputStream()

        var inS: InputStream? = null

        try {
            inS = am.open(fileName)

            var len = 0

            while (inS.read().also { len = it } != -1) {
                baos.write(len)
            }

            fileString = baos.toString()

        } catch (e: Exception) {
            SunLog.e("SunAssetsUtils", "getLocalAssetsFileStr error :$e")

        } finally {
            try {
                baos.close()
                inS?.close()
            } catch (e: Exception) {
            }
        }

        return fileString
    }

}