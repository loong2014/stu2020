package com.sunny.family;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.sunny.lib.image.modelloader.SunModelLoaderFactory;
import com.sunny.lib.utils.SunLog;

import java.nio.ByteBuffer;

/**
 * 1. 该类必须在包名下面，不能有二级目录
 * 2. build.gradle中必须添加:
 * 2.1. 头部添加 apply plugin: 'kotlin-kapt'
 * 2.2. dependencies中添加
 * implementation 'com.android.support:support-annotations:28.0.0'
 * kapt 'com.github.bumptech.glide:compiler:4.11.0' // 必须使用kapt而不是annotationProcessor
 * <p>
 * <p>
 * 在Glide4.11中目前依然不知道GlideApp和Glide有什么区别？
 * Glide的使用：http://www.zzvips.com/article/188897.html
 */
@GlideModule
public class SunAppGlideModule extends AppGlideModule {

    private static final String TAG = "SunAppGlideModule";

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        super.applyOptions(context, builder);

        // 内存缓存
        // 1. 配置缓存大小
        MemorySizeCalculator memoryCacheCalculator = new MemorySizeCalculator.Builder(context)
                .setMemoryCacheScreens(2)
                .build();
        builder.setMemoryCache(new LruResourceCache(memoryCacheCalculator.getMemoryCacheSize()));

        // 2. 也可以直接覆写缓存大小
//        int memoryCacheSizeBytes = 1024 * 1024 * 20; // 20mb
//        builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes));

        // 3. 也可以自定义缓存策略
//        builder.setMemoryCache(new YourAppMemoryCacheImpl());

        // Bitmap 池
        // 1
        MemorySizeCalculator bitmapPoolCalculator = new MemorySizeCalculator.Builder(context)
                .setBitmapPoolScreens(3)
                .build();
        builder.setBitmapPool(new LruBitmapPool(bitmapPoolCalculator.getBitmapPoolSize()));

        // 2
//        int bitmapPoolSizeBytes = 1024 * 1024 * 30; // 30mb
//        builder.setBitmapPool(new LruBitmapPool(bitmapPoolSizeBytes));

        //
//        builder.setBitmapPool(new YourAppBitmapPoolImpl());

        // 磁盘缓存
        // 1
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context));

        // 2
        int diskCacheSizeBytes = 1024 * 1024 * 100; // 100mb
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, "缓存文件夹在外存或内存上的名字", diskCacheSizeBytes));

        // 默认请求选项
        builder.setDefaultRequestOptions(
                new RequestOptions()

                        /*
                         * 解码格式
                         *
                         * Glide v3， 默认的 DecodeFormat 是 DecodeFormat.PREFER_RGB_565，它将使用 [Bitmap.Config.RGB_565]，除非图片包含或可能包含透明像素。
                         * 对于给定的图片尺寸，RGB_565 只使用 [Bitmap.Config.ARGB_8888] 一半的内存，但对于特定的图片有明显的画质问题，包括条纹(banding)和着色(tinting)。
                         *
                         * 为了避免RGB_565的画质问题，Glide 现在默认使用 ARGB_8888。结果是，图片质量变高了，但内存使用也增加了。
                         *
                         * 可通过 format(DecodeFormat.PREFER_RGB_565) 更改解码格式
                         */
                        .format(DecodeFormat.PREFER_RGB_565)
//                        .format(DecodeFormat.PREFER_ARGB_8888) // 启用硬件位图
                        .disallowHardwareConfig());

        // 未捕获异常策略 (UncaughtThrowableStrategy)
        final GlideExecutor.UncaughtThrowableStrategy myUncaughtThrowableStrategy = new GlideExecutor.UncaughtThrowableStrategy() {
            @Override
            public void handle(Throwable t) {
                SunLog.INSTANCE.e(TAG, "GlideExecutor :" + t);
            }
        };
//        builder.setDiskCacheExecutor(GlideExecutor.newDiskCacheExecutor(myUncaughtThrowableStrategy));
        builder.setDiskCacheExecutor(GlideExecutor.newDiskCacheBuilder().setUncaughtThrowableStrategy(myUncaughtThrowableStrategy).build());
//        builder.setResizeExecutor(GlideExecutor.newSourceExecutor(myUncaughtThrowableStrategy));
        builder.setSourceExecutor(GlideExecutor.newSourceBuilder().setUncaughtThrowableStrategy(myUncaughtThrowableStrategy).build());

        // 日志级别，默认INFO
//        builder.setLogLevel(Log.DEBUG);

    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
        // 注册组件
//        registry.append(Photo.class, InputStream.class, new CustomModelLoader.Factory());
//        registry.prepend(String.class, InputStream.class, new CustomUrlModelLoader.Factory());
//        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
        registry.prepend(String.class, ByteBuffer.class, new SunModelLoaderFactory());

    }

    private void glideTest() {
    }

    /**
     * 禁用清单解析。这样可以改善 Glide 的初始启动时间，并避免尝试解析元数据时的一些潜在问题
     *
     * @return false
     */
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
