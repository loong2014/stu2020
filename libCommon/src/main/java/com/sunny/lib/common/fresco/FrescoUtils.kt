package com.sunny.lib.common.fresco

import android.content.ComponentCallbacks2
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.text.TextUtils
import com.facebook.common.memory.MemoryTrimType
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.sunny.lib.common.utils.DomainUtils
import com.sunny.lib.common.utils.HandlerUtils
import com.sunny.lib.base.log.SunLog
import com.sunny.lib.utils.AppConfigUtils

object FrescoUtils {

    const val TAG = "FrescoTag"

    private val frescoHelper by lazy {
        FrescoCacheHelper()
    }

    /**
     * 日志是否打开
     */
    @JvmStatic
    fun logEnable(): Boolean {
        return true
    }

    /**
     * 是否是低端设备
     */
    @JvmStatic
    fun lowDevice(): Boolean {
        return AppConfigUtils.isLowCostDevice()
    }


    @JvmStatic
    fun init(context: Context) {

        val config = frescoHelper.buildConfig(context)
        Fresco.initialize(context, config)
    }

    /**
     * 内存不足
     */
    @JvmStatic
    fun onLowMemory() {
        SunLog.i(TAG, "onLowMemory")
        frescoHelper.tryTrimMemoryCache(MemoryTrimType.OnAppBackgrounded)
    }

    /**
     * 内存不足时的通知，无论当前应用是前台还是后台，
     * 只要内存不足，就进行图片缓存的释放
     */
    @JvmStatic
    fun onTrimMemory(level: Int) {
        SunLog.i(TAG, "onTrimMemory  $level")

        val type = when (level) {

            ComponentCallbacks2.TRIM_MEMORY_BACKGROUND,
            ComponentCallbacks2.TRIM_MEMORY_COMPLETE,
            ComponentCallbacks2.TRIM_MEMORY_MODERATE ->
                MemoryTrimType.OnSystemLowMemoryWhileAppInBackground /* 内存不足，当前应用在后台 */

            ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL,
            ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW ->
                MemoryTrimType.OnSystemLowMemoryWhileAppInForeground /* 内存不足，当前应用在前台 */

//            ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE ->
//                MemoryTrimType.OnSystemMemoryCriticallyLowWhileAppInForeground /* 内存严重不足，当前应用在前台 */

            else -> MemoryTrimType.OnAppBackgrounded /* 应用进入后台 */
        }

        frescoHelper.tryTrimMemoryCache(type)
    }

    /**
     * 继续Fresco图片加载，旧平台的Fresco版本不支持，该方法已兼容
     */
    @JvmStatic
    fun resume() {
        if (lowDevice()) {
            return
        }

        val pipeline = Fresco.getImagePipeline()
        if (pipeline.isPaused) {
            pipeline.resume()
        }
    }

    /**
     * 暂停Fresco图片加载，旧平台的Fresco版本不支持，该方法已兼容
     */
    @JvmStatic
    fun pause() {
        if (lowDevice()) {
            return
        }

        val pipeline = Fresco.getImagePipeline()
        if (!pipeline.isPaused) {
            pipeline.pause()
        }

    }


    private fun buildUri(url: String?): Uri? {
        if (TextUtils.isEmpty(url)) {
            return null
        }

        val domainUrl = DomainUtils.urlDomainReplace(url)

        return Uri.parse(domainUrl)
    }

    private fun getUIHandler(): Handler {
        return HandlerUtils.uiHandler
    }

    @JvmStatic
    fun loadImageUrl(url: String?, view: LeFrescoImageView?) {
        if (view == null) {
            return
        }

        if (view.controller != null) {
            return
        }

        val uri: Uri = buildUri(url) ?: return

        getUIHandler().post {
            view.controller = Fresco.newDraweeControllerBuilder()
                    .setUri(uri)
                    .setAutoPlayAnimations(true)
                    .setOldController(view.controller)
                    .build()
        }
    }

    @JvmStatic
    fun loadImageUrl(url: String?, view: LeFrescoImageView?, width: Int, height: Int) {
        if (view == null) {
            return
        }

        val uri: Uri = buildUri(url) ?: return

        val request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(ResizeOptions(width, height)).build()

        getUIHandler().post {
            view.controller = Fresco.newDraweeControllerBuilder()
                    .setUri(uri)
                    .setAutoPlayAnimations(true)
                    .setOldController(view.controller)
                    .setImageRequest(request)
                    .build()
        }
    }

}
