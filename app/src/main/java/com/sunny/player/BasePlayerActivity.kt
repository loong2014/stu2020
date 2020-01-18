package com.sunny.player

import android.net.Uri
import android.os.Bundle
import com.sunny.family.player.PlayerViewHelper
import com.sunny.lib.base.BaseActivity
import com.sunny.player.control.IVideoControl
import com.sunny.player.view.SunVideoView

open abstract class BasePlayerActivity : BaseActivity() {

    lateinit var mVideoControl: IVideoControl

    lateinit var playerViewHelper: PlayerViewHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        playerViewHelper = PlayerViewHelper(this)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

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