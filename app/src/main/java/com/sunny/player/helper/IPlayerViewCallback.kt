package com.sunny.player.helper

import com.sunny.player.model.VideoItemModel

/**
 * Created by zhangxin17 on 2020-01-14
 */
interface IPlayerViewCallback {

    fun onVideoItemClick(itemModel: VideoItemModel)

}