package com.sunny.family.player.http

import android.content.Context
import com.sunny.family.player.http.model.PlayerInfo
import com.sunny.lib.http.ErrorInfo
import com.sunny.lib.utils.HandlerUtils
import com.sunny.player.config.PlayerConfig
import com.sunny.player.config.VideoType

class PlayerNetHelper(context: Context) {

    fun getPlayInfo(videoId: String, callback: PlayerInfoCallback) {

        HandlerUtils.workHandler.postDelayed({

            val playerInfo = when (videoId) {
                PlayerConfig.video1_id -> {
                    PlayerInfo(
                            videoId = PlayerConfig.video1_id,
                            videoImg = PlayerConfig.video1_img,
                            videoUrl = PlayerConfig.video1_url,
                            videoName = PlayerConfig.video1_name)
                }

                PlayerConfig.video2_id -> {
                    PlayerInfo(
                            videoId = PlayerConfig.video2_id,
                            videoImg = PlayerConfig.video2_img,
                            videoUrl = PlayerConfig.video2_url,
                            videoName = PlayerConfig.video2_name)
                }

                PlayerConfig.video3_id -> {
                    PlayerInfo(
                            videoId = PlayerConfig.video3_id,
                            videoImg = PlayerConfig.video3_img,
                            videoUrl = PlayerConfig.video3_url,
                            videoName = PlayerConfig.video3_name)
                }

                PlayerConfig.video4_id -> {
                    PlayerInfo(
                            videoId = PlayerConfig.video4_id,
                            videoImg = PlayerConfig.video4_img,
                            videoUrl = PlayerConfig.video4_url,
                            videoName = PlayerConfig.video4_name)
                }

                else -> {
                    null
                }
            }

            if (playerInfo != null) {
                playerInfo.videoType = VideoType.NETWORK.type
                callback.onGetPlayerInfo(playerInfo)

            } else {
                callback.onGetPlayerInfoError(ErrorInfo.ERROR_DATA_EMPTY.type, ErrorInfo.ERROR_DATA_EMPTY.msg)
            }

        }, 1000)

    }


    interface PlayerInfoCallback {
        fun onGetPlayerInfo(playerInfo: PlayerInfo)

        fun onGetPlayerInfoError(code: String, msg: String)
    }
}