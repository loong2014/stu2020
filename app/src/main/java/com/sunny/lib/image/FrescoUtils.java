package com.sunny.lib.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.internal.Supplier;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.util.ByteConstants;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.sunny.family.R;
import com.sunny.lib.utils.AppConfigUtils;
import com.sunny.lib.utils.HandlerUtils;
import com.sunny.lib.utils.SunLog;

import org.jetbrains.annotations.Nullable;

/**
 * author: wenhao
 * created on: 2019-05-29 16:50
 * description:
 */
public class FrescoUtils {
    private static final String TAG = "FrescoUtils";
    private static int count = 0;

    public static void init(Context context) {
        SunLog.INSTANCE.i("FrescoUtils", "init");

        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(context)
                .setMaxCacheSize(40 * ByteConstants.MB) // 最大磁盘空间
                .build();

        final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(40 * ByteConstants.MB, // 内存缓存中总图片的最大大小,以字节为单位。
                256, // 内存缓存中图片的最大数量。
                10 * ByteConstants.MB, // 内存缓存中准备清除但尚未被删除的总图片的最大大小,以字节为单位。
                256, // 内存缓存中准备清除的总图片的最大数量。
                2 * ByteConstants.MB);// 内存缓存中单个图片的最大大小。

        Supplier<MemoryCacheParams> mSupplierMemoryCacheParams = new Supplier<MemoryCacheParams>() {
            @Override
            public MemoryCacheParams get() {
                return bitmapCacheParams;
            }
        };

        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(context)
                .setDownsampleEnabled(true).setResizeAndRotateEnabledForNetwork(true)
                .setMainDiskCacheConfig(diskCacheConfig)
                .setBitmapMemoryCacheParamsSupplier(mSupplierMemoryCacheParams)
                .setBitmapsConfig(AppConfigUtils.INSTANCE.isLowCostDevice()
                        ? Bitmap.Config.RGB_565
                        : null)
                .build();
        Fresco.initialize(context, config);
    }

    public static void setImageURI(String url, final SimpleDraweeView simpleDraweeView) {
        SunLog.INSTANCE.i(TAG, "setImageURI  count :" + count++);

        if (simpleDraweeView == null) {
            return;
        }

        if (TextUtils.isEmpty(url)) {
            return;
        }

        final Uri uri = (url != null) ? Uri.parse(url) : null;

        HandlerUtils.INSTANCE.getUiHandler().post(new Runnable() {
            @Override
            public void run() {
                DraweeController controller = Fresco.newDraweeControllerBuilder().setUri(uri)
                        .setAutoPlayAnimations(true)
                        .setOldController(simpleDraweeView.getController()).build();
                simpleDraweeView.setController(controller);
            }
        });
    }

    public static void setImageURI(String url, final SimpleDraweeView simpleDraweeView, int width,
                                   int height) {
        SunLog.INSTANCE.i(TAG, "setImageURI2  count :" + count++);

        if (simpleDraweeView == null) {
            return;
        }

        if (simpleDraweeView.getController() != null) {
            return;
        }

        if (TextUtils.isEmpty(url)) {
            return;
        }

        final Uri uri = (url != null) ? Uri.parse(url) : null;

        final ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(width, height)).build();

        HandlerUtils.INSTANCE.getUiHandler().post(new Runnable() {
            @Override
            public void run() {
                DraweeController controller = Fresco.newDraweeControllerBuilder().setUri(uri)
                        .setAutoPlayAnimations(true)
                        .setOldController(simpleDraweeView.getController()).setImageRequest(request)
                        .build();

                simpleDraweeView.setController(controller);
            }
        });
    }

    public static void resetImageURI(String url, final SimpleDraweeView simpleDraweeView, int width,
                                     int height) {
        SunLog.INSTANCE.i(TAG, "setImageURI2  count :" + count++);
        if (simpleDraweeView == null) {
            return;
        }

        if (TextUtils.isEmpty(url)) {
            return;
        }

        Object tag = simpleDraweeView.getTag(R.id.tag_imageview_url);
        if (tag instanceof String && url.equals(tag)) {
            // Logger.i(TAG, "setImageURI2 tag :" + tag);
            return;
        } else {
            // Logger.i(TAG, "setImageURI2 url :" + url);
            simpleDraweeView.setTag(R.id.tag_imageview_url, url);
        }

        final Uri uri = (url != null) ? Uri.parse(url) : null;

        final ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(width, height)).build();

        HandlerUtils.INSTANCE.getUiHandler().post(new Runnable() {
            @Override
            public void run() {
                DraweeController controller = Fresco.newDraweeControllerBuilder().setUri(uri)
                        .setAutoPlayAnimations(true)
                        .setOldController(simpleDraweeView.getController()).setImageRequest(request)
                        .build();

                simpleDraweeView.setController(controller);
            }
        });
    }

    public static void setImageURILow(String url, String lowUrl,
                                      final SimpleDraweeView simpleDraweeView, int width, int height) {
        if (simpleDraweeView == null) {
            return;
        }

        if (TextUtils.isEmpty(url)) {
            return;
        }


        final Uri uri = (url != null) ? Uri.parse(url) : null;

        final ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(width, height)).build();

        ImageRequest lowImageReq = null;
        if (!TextUtils.isEmpty(lowUrl)) {
            lowImageReq = ImageRequestBuilder.newBuilderWithSource(Uri.parse(lowUrl))
                    .setResizeOptions(new ResizeOptions(width, height)).build();
        }

        final ImageRequest lowImageRequest = lowImageReq;

        HandlerUtils.INSTANCE.getUiHandler().post(new Runnable() {
            @Override
            public void run() {
                DraweeController controller = Fresco.newDraweeControllerBuilder().setUri(uri)
                        .setAutoPlayAnimations(true)
                        .setOldController(simpleDraweeView.getController()).setImageRequest(request)
                        .setLowResImageRequest(lowImageRequest).build();

                simpleDraweeView.setController(controller);
            }
        });
    }

    public static void setImageRes(int imageRes, SimpleDraweeView simpleDraweeView) {
        SunLog.INSTANCE.i(TAG, "setImageRes  count :" + count++);

        if (simpleDraweeView == null) {
            return;
        }
        // GenericDraweeHierarchy hierarchy = simpleDraweeView.getHierarchy();
        // hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
        simpleDraweeView.setActualImageResource(imageRes);
    }

    /**
     * 继续Fresco图片加载，旧平台的Fresco版本不支持，该方法已兼容
     */
    public static void resume() {
//        if (!EuiUtils.isOldPlatform() || ModuleUtil.isModule()) {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        if (imagePipeline.isPaused()) {
            imagePipeline.resume();
        }
//        }
    }

    /**
     * 暂停Fresco图片加载，旧平台的Fresco版本不支持，该方法已兼容
     */
    public static void pause() {
//        if (!SDKUtil.isOldPlatform() || ModuleUtil.isModule()) {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        if (!imagePipeline.isPaused()) {
            imagePipeline.pause();
        }
//        }
    }

    /**
     * 清除图片缓存
     */
    public static void clearMemoryCache() {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.clearMemoryCaches();
        // imagePipeline.clearDiskCaches();
    }

    public static void clearCache() {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        // combines above two lines
        imagePipeline.clearCaches();
    }

    /**
     * 清除单张图片的磁盘缓存
     */
    public static void clearCacheByUrl(String url) {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        Uri uri = Uri.parse(url);
        // imagePipeline.evictFromMemoryCache(uri);
        // imagePipeline.evictFromDiskCache(uri);
        imagePipeline.evictFromCache(uri);// 这个包含了从内存移除和从硬盘移除
    }

    public static void getImage(String url, final FrescoImageLoadingListener imageLoadingListener) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        if (imageLoadingListener == null) {
            return;
        }

        final Uri uri = (url != null) ? Uri.parse(url) : null;

        ImageRequestBuilder requestBuilder = ImageRequestBuilder.newBuilderWithSource(uri);
        ImageRequest imageRequest = requestBuilder.build();
        DataSource<CloseableReference<CloseableImage>> dataSource = ImagePipelineFactory
                .getInstance().getImagePipeline().fetchDecodedImage(imageRequest, null);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            protected void onNewResultImpl(@Nullable Bitmap bitmap) {
                if (bitmap != null && !bitmap.isRecycled()) {
                    imageLoadingListener.onSuccess(uri, bitmap);
                }
            }

            @Override
            protected void onFailureImpl(
                    DataSource<CloseableReference<CloseableImage>> dataSource) {
                Throwable throwable = null;
                if (dataSource != null) {
                    throwable = dataSource.getFailureCause();
                }
                imageLoadingListener.onFailure(uri, throwable);
            }
        }, UiThreadImmediateExecutorService.getInstance());
    }
}
