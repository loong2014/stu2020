package com.sunny.family.photoalbum

import com.sunny.family.model.PictureModel
import com.sunny.family.model.VideoModel
import com.sunny.family.model.VoiceModel
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
            val dataList = mutableListOf<PictureModel>()

            val dirFile = File(FileUtils.PICTURE_FOLDER_PATH)

            dirFile.listFiles()?.forEach {
                it?.let {
                    if (FileUtils.isPictureFile(it)) {
                        dataList.add(PictureModel(it.absolutePath, it.name))
                    }
                }
            }

            callback.onGetPictureList(dataList)
        }, 1000)
    }

    fun getVideoList(callback: PhotoAlbumCallback) {
        HandlerUtils.getUiHandler().postDelayed({
            val dataList = mutableListOf<VideoModel>()

            val dirFile = File(FileUtils.VIDEO_FOLDER_PATH)

            dirFile.listFiles()?.forEach {
                it?.let {
                    if (FileUtils.isVideoFile(it)) {
                        dataList.add(VideoModel(it.absolutePath, it.name, 100))
                    }
                }
            }

            callback.onGetVideoList(dataList)
        }, 1000)
    }


    fun getVoiceList(callback: PhotoAlbumCallback) {
        HandlerUtils.getUiHandler().postDelayed({
            val dataList = mutableListOf<VoiceModel>()

            val dirFile = File(FileUtils.VOICE_FOLDER_PATH)

            dirFile.listFiles()?.forEach {
                it?.let {
                    if (FileUtils.isVoiceFile(it)) {
                        dataList.add(VoiceModel(it.absolutePath, it.name, 100))
                    }
                }
            }

            callback.onGetVoiceList(dataList)
        }, 1000)
    }

    interface PhotoAlbumCallback {
        fun onGetPictureList(list: List<PictureModel>) {
        }

        fun onGetVideoList(list: List<VideoModel>) {
        }

        fun onGetVoiceList(list: List<VoiceModel>) {

        }
    }
}