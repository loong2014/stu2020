package com.sunny.player.helper

import com.sunny.player.view.SunVideoView

/**
 * Created by zhangxin17 on 2020-01-14
 */
interface IPlayerViewHelper {

    fun setPlayerViewCallback(callback: IPlayerViewCallback)

    fun setPlayerView(videoView: SunVideoView)
}