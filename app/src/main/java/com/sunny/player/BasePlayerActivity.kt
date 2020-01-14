package com.sunny.player

import android.net.Uri
import com.sunny.lib.base.BaseActivity
import com.sunny.player.control.IVideoControl
import com.sunny.player.view.SunVideoView

open abstract class BasePlayerActivity : BaseActivity() {

    lateinit var mVideoControl: IVideoControl

    fun setVideoView(videoView: SunVideoView) {
        mVideoControl = videoView
    }

    fun setVideoUri(videoPath: String) {
        mVideoControl.setVideoPath(Uri.parse(videoPath), null)
    }

    fun playStart() {
        mVideoControl.startPlay()
    }

    fun playStop() {
        mVideoControl.pausePlay()
    }
}