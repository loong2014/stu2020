package com.sunny.lib.jump

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.sunny.family.home.HomeActivity
import com.sunny.lib.utils.ContextProvider

object PageJumpUtils {

    fun jumpHomePage(intent: Intent?, context: Context?) {
        val jumpContext = context ?: ContextProvider.appContext

        val jumpIntent = intent ?: Intent()

        jumpIntent.component = ComponentName(jumpContext, HomeActivity::class.java)
        if (jumpContext is Application){
            jumpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        jumpContext.startActivity(jumpIntent)
    }
}