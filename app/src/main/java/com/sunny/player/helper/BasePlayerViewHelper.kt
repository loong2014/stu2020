package com.sunny.player.helper

import android.content.Context
import android.net.Uri
import com.sunny.lib.jump.params.JumpPlayerParam
import com.sunny.lib.utils.SunLog
import com.sunny.player.control.IVideoControl
import com.sunny.player.control.IVideoViewListener
import com.sunny.player.view.SunVideoView

open class BasePlayerViewHelper(context: Context) {

    private val logTag = SunLog.buildTag("BasePlayerViewHelper")

    private lateinit var mVideoControl: IVideoControl

    private var mPlayerViewCallback: IPlayerViewCallback? = null

    fun initPlayerView(videoView: SunVideoView) {
        SunLog.i(logTag, "initPlayerView")

        mVideoControl = videoView
        mVideoControl.setVideoViewListener(mVideoViewListener)
    }

    fun setPlayerViewCallback(callback: IPlayerViewCallback) {
        mPlayerViewCallback = callback
    }

    fun setPlayerJump(jumpParam: JumpPlayerParam) {
        SunLog.i(logTag, "setPlayerJump")

        mVideoControl.setVideoPath(Uri.parse(jumpParam.videoPath), null)
    }

    private val mVideoViewListener = object : IVideoViewListener {

        override fun onPrepared() {
            mVideoControl.startPlay()
        }

        override fun onSeekComplete() {
        }

        override fun onBufferEnd() {
        }

        override fun onBufferTotalPercentUpdate(percent: Int) {
        }

        override fun onInfo(what: Int, extra: Int): Boolean {
            return false
        }

        override fun onVideoSizeChanged(width: Int, height: Int) {
        }

        override fun onCompletion() {
        }

        override fun onBufferStart(progress: Int) {
        }

        override fun onError(what: Int, extra: Int) {
        }

    }


    fun onActResume() {
        if (mVideoControl.inPlaybackState()) {
            mVideoControl.startPlay()
        }
    }

    fun onActPause() {
        if (mVideoControl.inPlaybackState()) {
            mVideoControl.pausePlay()
        }
    }

    fun onActDestroy() {
    }

}