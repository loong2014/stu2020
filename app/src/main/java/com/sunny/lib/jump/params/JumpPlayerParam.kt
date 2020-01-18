package com.sunny.lib.jump.params

import com.sunny.player.config.VideoType

/**
 * Created by zhangxin17 on 2020-01-15
 */
data class JumpPlayerParam(var videoId: String = "",
                           var videoPath: String = "",
                           var videoName: String = "",
                           var videoImg: String = "",
                           var videoType: VideoType = VideoType.NONE) : BaseParam()
