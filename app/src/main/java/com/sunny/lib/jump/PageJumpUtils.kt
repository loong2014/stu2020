package com.sunny.lib.jump

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import com.sunny.family.detail.DetailActivity
import com.sunny.family.home.HomeActivity
import com.sunny.family.photoalbum.PhotoAlbumActivity
import com.sunny.family.player.PlayerLocalActivity
import com.sunny.family.player.PlayerNetActivity
import com.sunny.lib.jump.params.BaseParam
import com.sunny.lib.jump.params.JumpPlayerParam
import com.sunny.lib.utils.ContextProvider
import com.sunny.lib.utils.SunToast
import com.sunny.player.config.VideoType

object PageJumpUtils {

    fun jumpHomePage(intent: Intent? = null, context: Context? = ContextProvider.appContext) {
        doPageJump(intent, context, HomeActivity::class.java)
    }

    fun jumpPhotoAlbumPage(intent: Intent? = null, context: Context? = ContextProvider.appContext) {
        doPageJump(intent, context, PhotoAlbumActivity::class.java)
    }

    fun jumpDetailPage(intent: Intent? = null, context: Context? = ContextProvider.appContext) {
        doPageJump(intent, context, DetailActivity::class.java)
    }

    fun jumpPlayerPage(intent: Intent? = null, context: Context? = ContextProvider.appContext, jumpParams: JumpPlayerParam) {

        when (jumpParams.videoType) {
            VideoType.LOCAL -> {
                doPageJump(intent, context, PlayerLocalActivity::class.java, jumpParams)
            }

            VideoType.NETWORK -> {
                doPageJump(intent, context, PlayerNetActivity::class.java, jumpParams)
            }

            else -> {
                SunToast.show("错误的跳转类型")
            }
        }
    }

    private fun doPageJump(intent: Intent? = null, context: Context? = ContextProvider.appContext, clazz: Class<*>, params: BaseParam? = null) {
        val jumpContext = context ?: ContextProvider.appContext

        val jumpIntent = intent ?: Intent()
        if (jumpContext is Application) {
            jumpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        jumpIntent.component = ComponentName(jumpContext, clazz)


        val jsonStr: String = Gson().toJson(params)

        jumpIntent.putExtra(JumpConfig.keyJumpParams, jsonStr)

        jumpContext.startActivity(jumpIntent)
    }

}