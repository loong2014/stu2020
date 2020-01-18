package com.sunny.family.player

import android.app.Activity
import android.os.Bundle
import com.google.gson.Gson
import com.sunny.family.R
import com.sunny.family.player.http.model.PlayerInfo
import com.sunny.lib.jump.JumpConfig
import com.sunny.lib.jump.params.JumpPlayerParam
import com.sunny.lib.utils.SunLog
import com.sunny.player.config.VideoType
import kotlinx.android.synthetic.main.act_player.*

class PlayerLocalActivity : Activity() {

    private val logTag = SunLog.buildPlayerTag("PlayerActivity")

    private lateinit var playerViewHelper: PlayerViewHelper

    lateinit var jumpParams: JumpPlayerParam

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.getStringExtra(JumpConfig.keyJumpParams)?.let {
            jumpParams = Gson().fromJson(it, JumpPlayerParam::class.java)
        }

        setContentView(R.layout.act_player)

        initPlayer()
    }

    private fun initPlayer() {
        SunLog.i(logTag, "initPlayer")
        playerViewHelper = PlayerViewHelper(this)
        playerViewHelper.initPlayerView(sun_player_view)

        playerViewHelper.setPlayerInfo(PlayerInfo(
                videoUrl = jumpParams.videoPath,
                videoName = jumpParams.videoName,
                videoImg = jumpParams.videoImg,
                videoType = VideoType.LOCAL.type
        ))
    }

    override fun onResume() {
        super.onResume()
        playerViewHelper.onActResume()
    }

    override fun onPause() {
        super.onPause()
        playerViewHelper.onActPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerViewHelper.onActDestroy()
    }

}