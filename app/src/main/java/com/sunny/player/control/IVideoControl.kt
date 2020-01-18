package com.sunny.player.control

import android.net.Uri
import android.view.View
import com.sunny.player.view.SunMediaPlayer

interface IVideoControl {
    fun setVideoViewListener(listener: IVideoViewListener)

    val videoView: View

    val mediaPlayer: SunMediaPlayer?

    val videoWidth: Int

    val videoHeight: Int

    val videoDuration: Int

    val currentPosition: Int

    val isPlaying: Boolean

    fun setVideoPath(uri: Uri, headers: Map<String, String>?)

    fun startPlay()

    fun pausePlay()

    fun stopPlay()

    fun seekTo(position: Int)


    fun inPlaybackState(): Boolean
}