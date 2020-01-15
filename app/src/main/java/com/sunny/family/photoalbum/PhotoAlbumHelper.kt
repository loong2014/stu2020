package com.sunny.family.photoalbum

import com.sunny.family.model.MediaModel

/**
 * Created by zhangxin17 on 2020-01-15
 */
class PhotoAlbumHelper {

    private val logTag = "PhotoAlbumHelper"

    fun getPictureList(callback: PhotoAlbumCallback) {

    }


    interface PhotoAlbumCallback {
        fun onGetPicturePathList(list: List<MediaModel>) {

        }
    }
}