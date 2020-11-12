package com.sunny.lib.jump

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import com.sunny.family.city.CityActivity
import com.sunny.family.cityexpan.ExpandableCityActivity
import com.sunny.family.dialog.StuDialogAct
import com.sunny.family.home.HomeActivity
import com.sunny.family.image.GlideImageActivity
import com.sunny.family.photoalbum.PhotoAlbumActivity
import com.sunny.family.player.PlayerLocalActivity
import com.sunny.family.player.PlayerNetActivity
import com.sunny.family.sensor.SensorActivity
import com.sunny.family.weather.WeatherActivity
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

    fun jumpCityPage(intent: Intent? = null, context: Context? = ContextProvider.appContext) {
        doPageJump(intent, context, CityActivity::class.java)
    }

    fun jumpExpandableCityPage(intent: Intent? = null, context: Context? = ContextProvider.appContext) {
        doPageJump(intent, context, ExpandableCityActivity::class.java)
    }

    fun jumpWeatherPage(intent: Intent? = null, context: Context? = ContextProvider.appContext) {
        doPageJump(intent, context, WeatherActivity::class.java)
    }

    fun jumpImagePage(intent: Intent? = null, context: Context? = ContextProvider.appContext) {
        doPageJump(intent, context, GlideImageActivity::class.java)
//        doPageJump(intent, context, FrescoImageActivity::class.java)
    }

    fun jumpSensorPage(intent: Intent? = null, context: Context? = ContextProvider.appContext) {
        doPageJump(intent, context, SensorActivity::class.java)
    }

    fun jumpDialogPage(intent: Intent? = null, context: Context? = ContextProvider.appContext) {
        doPageJump(intent, context, StuDialogAct::class.java)
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