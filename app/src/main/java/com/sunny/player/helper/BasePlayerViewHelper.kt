package com.sunny.player.helper

import com.sunny.player.view.SunVideoView

open class BasePlayerViewHelper : IPlayerViewHelper {

    lateinit var mPlayerViewCallback: IPlayerViewCallback

    lateinit var mPlayerView: SunVideoView

    override fun setPlayerViewCallback(callback: IPlayerViewCallback) {
        mPlayerViewCallback = callback
    }

    override fun setPlayerView(videoView: SunVideoView) {
        mPlayerView = videoView
    }

}