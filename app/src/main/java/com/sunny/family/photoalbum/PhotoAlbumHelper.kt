package com.sunny.family.photoalbum

import com.sunny.family.model.MediaModel
import com.sunny.lib.utils.FileUtils
import com.sunny.lib.utils.HandlerUtils
import java.io.File

/**
 * Created by zhangxin17 on 2020-01-15
 */
class PhotoAlbumHelper {

    private val logTag = "PhotoAlbumHelper"

    fun getPictureList(callback: PhotoAlbumCallback) {
        HandlerUtils.getUiHandler().postDelayed({
            val dataList = mutableListOf<MediaModel>()

            val dirFile = File(FileUtils.PICTURE_FOLDER_PATG)

            dirFile.listFiles()?.forEach {
                it?.let {
                    if (FileUtils.isPictureFile(it)) {
                        dataList.add(MediaModel(it.absolutePath, it.name))
                    }
                }
            }

            callback.onGetPicturePathList(dataList)
        }, 1000)

    }

    interface PhotoAlbumCallback {
        fun onGetPicturePathList(list: List<MediaModel>) {

        }
    }
}