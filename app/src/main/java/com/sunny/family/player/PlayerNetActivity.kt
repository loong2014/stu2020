package com.sunny.family.player

import android.app.Activity
import android.os.Bundle
import com.google.gson.Gson
import com.sunny.family.R
import com.sunny.family.player.http.PlayerNetHelper
import com.sunny.family.player.http.model.PlayerInfo
import com.sunny.lib.jump.JumpConfig
import com.sunny.lib.jump.params.JumpPlayerParam
import com.sunny.lib.utils.SunLog
import kotlinx.android.synthetic.main.act_player.*

class PlayerNetActivity : Activity() {

    private val logTag = SunLog.buildPlayerTag("PlayerNetActivity")

    private lateinit var playerViewHelper: PlayerViewHelper

    private lateinit var playerNetHelper: PlayerNetHelper

    lateinit var jumpParams: JumpPlayerParam

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.getStringExtra(JumpConfig.keyJumpParams)?.let {
            jumpParams = Gson().fromJson(it, JumpPlayerParam::class.java)
        }

        setContentView(R.layout.act_player)

        initPlayer()
        initData()
    }


    private fun initPlayer() {
        SunLog.i(logTag, "initPlayer")
        playerViewHelper = PlayerViewHelper(this)
        playerViewHelper.initPlayerView(sun_player_view)
    }

    private fun initData() {
        playerNetHelper = PlayerNetHelper(this)

        playerNetHelper.getPlayInfo(jumpParams.videoId, object : PlayerNetHelper.PlayerInfoCallback {
            override fun onGetPlayerInfo(playerInfo: PlayerInfo) {
                SunLog.i(logTag, "onGetPlayerInfo  $playerInfo")

                runOnUiThread {
                    playerViewHelper.setPlayerInfo(playerInfo)
                }
            }

            override fun onGetPlayerInfoError(code: String, msg: String) {
                SunLog.i(logTag, "onGetPlayerInfoError  $code , $msg")
            }
        })
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