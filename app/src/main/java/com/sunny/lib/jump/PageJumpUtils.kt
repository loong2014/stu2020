package com.sunny.lib.jump

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.sunny.family.home.HomeActivity
import com.sunny.family.photoalbum.PhotoAlbumActivity
import com.sunny.lib.utils.ContextProvider

object PageJumpUtils {

    fun jumpHomePage(intent: Intent?, context: Context?) {
        doPageJump(intent, context, HomeActivity::class.java)
    }

    fun jumpPhotoAlbumPage(intent: Intent?, context: Context?) {
        doPageJump(intent, context, PhotoAlbumActivity::class.java)
    }

    private fun doPageJump(intent: Intent?, context: Context?, clazz: Class<*>) {
        val jumpContext = context ?: ContextProvider.appContext

        val jumpIntent = intent ?: Intent()
        if (jumpContext is Application) {
            jumpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        jumpIntent.component = ComponentName(jumpContext, clazz)

        jumpContext.startActivity(jumpIntent)
    }

}