package com.sunny.family.player

import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import com.google.gson.Gson
import com.sunny.family.R
import com.sunny.lib.base.BaseActivity
import com.sunny.lib.jump.JumpConfig
import com.sunny.lib.jump.params.JumpPlayerParams
import kotlinx.android.synthetic.main.act_player_video_view.*

class VideoViewActivity : BaseActivity() {

    var jumpParams: JumpPlayerParams? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_player_video_view)

        intent.getStringExtra(JumpConfig.keyJumpParams)?.let {
            jumpParams = Gson().fromJson(it, JumpPlayerParams::class.java)
        }

        initVideoView()

    }

    private fun initVideoView() {
        jumpParams?.let {
            sys_video_view.setMediaController(MediaController(this))

            val uri = Uri.parse(it.videoPath)
            sys_video_view.setVideoURI(uri)

            sys_video_view.start()
        }
    }


}