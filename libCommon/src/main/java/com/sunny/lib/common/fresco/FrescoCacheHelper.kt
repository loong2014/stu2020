package com.sunny.lib.common.fresco

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import com.facebook.cache.common.CacheErrorLogger
import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.common.internal.Supplier
import com.facebook.common.memory.MemoryTrimType
import com.facebook.common.memory.MemoryTrimmable
import com.facebook.common.memory.MemoryTrimmableRegistry
import com.facebook.common.util.ByteConstants
import com.facebook.imagepipeline.cache.MemoryCacheParams
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.sunny.lib.base.log.SunLog

/**
 * 设置内存紧张时的策略
 *
 * MemoryCacheParams{
 * maxCacheSize : 已解码图片缓存最大值,以字节为单位
 * maxCacheEntries : 已解码缓存图片最大数量
 *
 * maxEvictionQueueSize : 内存中准备清理图片最大内存
 * maxEvictionQueueEntries : 内存中准备清理图片最大数量
 *
 * maxCacheEntrySize : 内存中单张图片的最大大小
 * }
 */
class FrescoCacheHelper : MemoryTrimmableRegistry {

    private var memoryTrimmable: MemoryTrimmable? = null

    private fun getBitmapConfig(): Bitmap.Config {
        return if (FrescoUtils.lowDevice()) {
            Bitmap.Config.RGB_565
        } else {
            Bitmap.Config.ARGB_8888
        }
    }

    private fun getDiskSize(): Int {
        return if (FrescoUtils.lowDevice()) {
            30
        } else {
            60
        }
    }

    /**
     * 一级缓存
     */
    private val bitmapMemoryCacheParams = Supplier<MemoryCacheParams> {
        if (FrescoUtils.lowDevice()) {
            MemoryCacheParams(
                    11 * ByteConstants.MB, 70,
                    3 * ByteConstants.MB, Int.MAX_VALUE,
                    Int.MAX_VALUE)
        } else {
            MemoryCacheParams(
                    22 * ByteConstants.MB, 70,
                    3 * ByteConstants.MB, Int.MAX_VALUE,
                    Int.MAX_VALUE)
        }
    }

    /**
     * 二级缓存
     */
    private val encodedMemoryCacheParams = Supplier<MemoryCacheParams> {
        MemoryCacheParams(
                5 * ByteConstants.MB, Int.MAX_VALUE,
                0, Int.MAX_VALUE,
                8 * ByteConstants.MB)
    }

    /**
     * 三级缓存
     */
    private fun getDiskCacheConfig(context: Context): DiskCacheConfig {
        val diskSize = getDiskSize()

        return DiskCacheConfig.newBuilder(context)
                /* 缓存图片基路径 */
                .setBaseDirectoryPath(Environment.getExternalStorageDirectory().absoluteFile)
                /* 缓存文件夹名 */
                .setBaseDirectoryName("letv_img")
                /* 错误缓存的日志记录器 */
                .setCacheErrorLogger(GlobalDiskCacheErrorLogger())
                /* 默认缓存的最大值 */
                .setMaxCacheSize(diskSize * ByteConstants.MB.toLong())
                /* 低磁盘空间时，缓存的最大值 */
                .setMaxCacheSizeOnLowDiskSpace(diskSize / 2 * ByteConstants.MB.toLong())
                /* 非常低磁盘空间时，缓存的最大值 */
                .setMaxCacheSizeOnVeryLowDiskSpace(diskSize / 3 * ByteConstants.MB.toLong())
                /* 磁盘配置的版本 */
                .setVersion(1)
                .build()
    }


    fun buildConfig(context: Context): ImagePipelineConfig {
        return ImagePipelineConfig.newBuilder(context.applicationContext)
                .setDownsampleEnabled(true)

                /* 一级缓存配置 */
                .setBitmapMemoryCacheParamsSupplier(bitmapMemoryCacheParams)

                /* 二级缓存配置 */
                .setEncodedMemoryCacheParamsSupplier(encodedMemoryCacheParams)

                /* 三级缓存配置 */
                .setMainDiskCacheConfig(getDiskCacheConfig(context))

                .setBitmapsConfig(getBitmapConfig())

                .setMemoryTrimmableRegistry(this)
                .build()
    }

    /**
     * 主动清除缓存
     */
    fun tryTrimMemoryCache(type: MemoryTrimType) {
        memoryTrimmable?.trim(type)
    }

    override fun registerMemoryTrimmable(trimmable: MemoryTrimmable?) {
        SunLog.d(FrescoUtils.TAG, "registerMemoryTrimmable")
        memoryTrimmable = trimmable
    }

    override fun unregisterMemoryTrimmable(trimmable: MemoryTrimmable?) {
        SunLog.d(FrescoUtils.TAG, "unregisterMemoryTrimmable")
        memoryTrimmable = null
    }

    inner class GlobalDiskCacheErrorLogger : CacheErrorLogger {
        override fun logError(category: CacheErrorLogger.CacheErrorCategory?,
                              clazz: Class<*>?,
                              message: String?,
                              throwable: Throwable?) {
            if (FrescoUtils.logEnable()) {
                val sb = StringBuilder()
                sb.append("############# DiskCacheError #############\n")
                sb.append(category)
                sb.append("\n")
                sb.append(clazz)
                sb.append("\n")
                sb.append(message)
                sb.append("\n")
                sb.append(throwable)
                sb.append("##########################################")
                SunLog.d(FrescoUtils.TAG, sb.toString())
            }
        }
    }
}
