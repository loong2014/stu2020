package com.sunny.family.player

import android.content.Context
import com.sunny.family.player.http.model.PlayerInfo
import com.sunny.lib.utils.SunLog
import com.sunny.player.config.VideoType
import com.sunny.player.helper.BasePlayerViewHelper

class PlayerViewHelper(context: Context) : BasePlayerViewHelper(context) {
    private val logTag = SunLog.buildPlayerTag("PlayerViewHelper")

    fun setPlayerInfo(playerInfo: PlayerInfo) {
        SunLog.i(logTag, "setPlayerInfo $playerInfo")

        playerInfo.videoUrl?.let {
            return when (playerInfo.videoType) {
                VideoType.NETWORK.type -> {
                    setVideoPath(it)
                }
                VideoType.LOCAL.type -> {
                    setVideoPath(it)
                }
                else -> {
                    SunLog.i(logTag, "setPlayerJump  not support videoType :${playerInfo.videoType}")
                }
            }
        }

        SunLog.i(logTag, "setPlayerInfo video url is empty")
    }

}