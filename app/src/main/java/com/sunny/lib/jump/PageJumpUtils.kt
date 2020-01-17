package com.sunny.lib.jump

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import com.sunny.family.home.HomeActivity
import com.sunny.family.photoalbum.PhotoAlbumActivity
import com.sunny.family.player.VideoViewActivity
import com.sunny.lib.jump.params.BaseParams
import com.sunny.lib.jump.params.JumpPlayerParams
import com.sunny.lib.utils.ContextProvider

object PageJumpUtils {

    fun jumpHomePage(intent: Intent? = null, context: Context? = ContextProvider.appContext) {
        doPageJump(intent, context, HomeActivity::class.java)
    }

    fun jumpPhotoAlbumPage(intent: Intent? = null, context: Context? = ContextProvider.appContext) {
        doPageJump(intent, context, PhotoAlbumActivity::class.java)
    }

    fun jumpPlayerPage(intent: Intent? = null, context: Context? = ContextProvider.appContext, jumpParams: JumpPlayerParams) {
//        doPageJump(intent, context, PlayerActivity::class.java, jumpParams)
        doPageJump(intent, context, VideoViewActivity::class.java, jumpParams)
    }

    private fun doPageJump(intent: Intent? = null, context: Context? = ContextProvider.appContext, clazz: Class<*>, params: BaseParams? = null) {
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