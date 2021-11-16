package com.sunny.lib.common.fresco;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.datasource.DataSubscriber;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.image.CloseableBitmap;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.sunny.lib.base.log.SunLog;
import com.sunny.lib.common.thread.SunThreadPool;
import com.sunny.lib.utils.ContextProvider;

/**
 * 使用Fresco原生方法实现util<br>
 *
 * @author jiwenjie
 * @date 2017/9/11
 */
public class FrescoCacheUtil {
    private static final String TAG = FrescoCacheUtil.class.getSimpleName();


    private static ImageRequest buildImageRequest(String inputUri, int resizeWidth,
                                                  int resizeHeight) {
        String uriString = inputUri;
//        // 图片域名国广处理
//        if (!TextUtils.isEmpty(inputUri)) {
//            if (AppConfigUtils.isMgtvBroadcast() || AppConfigUtils.isCibnBroadcast()) {
//                uriString = DomainUtils.urlDomainReplace(inputUri);
//            }
//        }

        ResizeOptions resizeOptions = null;
        if (resizeWidth > 0 && resizeHeight > 0) {
            resizeOptions = new ResizeOptions(resizeWidth, resizeHeight);
        }
        return ImageRequestBuilder.newBuilderWithSource(Uri.parse(uriString))
                .setResizeOptions(resizeOptions)
                // .setWidgetContext(ContextProvider.getApplicationContext())
                .build();
    }

    public static void getBitmap(final String inputUri, final String imageTag, int resizeWidth,
                                 int resizeHeight, final ICommonGetBitmap callback) {
        getBitmap(inputUri, imageTag, resizeWidth, resizeHeight, callback, Bitmap.Config.ARGB_8888);
    }

    /**
     * 获取Fresco图片库中bitmap，resizeWidth=0, resizeHeight=0时返回原图大小
     *
     * @param inputUri
     * @param imageTag
     * @param resizeWidth
     * @param resizeHeight
     * @param callback
     */
    public static void getBitmap(final String inputUri, final String imageTag, int resizeWidth,
                                 int resizeHeight, final ICommonGetBitmap callback, Bitmap.Config bitmapConfig) {
        final ImageRequest imageRequest = buildImageRequest(inputUri, resizeWidth, resizeHeight);
        getBitmap(imageRequest, imageTag, callback, bitmapConfig);
    }


    public static void getBitmap(final ImageRequest imageRequest, final String imageTag,
                                 final ICommonGetBitmap callback) {
        getBitmap(imageRequest, imageTag, callback, Bitmap.Config.ARGB_8888);
    }

    /**
     * 使用Fresco原生的方法实现，imageRequest包含resize参数<br>
     * 条件：SDKVersion < 1.0.4使用<br>
     * 取bitmap失败后不做图片加载重试操作，只会清除掉异常缓存<br>
     *
     * @param imageRequest
     * @param imageTag
     * @param callback
     */
    public static void getBitmap(final ImageRequest imageRequest, final String imageTag,
                                 final ICommonGetBitmap callback, final Bitmap.Config bitmapConfig) {
        if (imageRequest == null) {
            return;
        }
        ImagePipeline imagePipeline = ImagePipelineFactory.getInstance().getImagePipeline();
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline
                .fetchDecodedImage(imageRequest, ContextProvider.appContext);
        Uri uri = imageRequest.getSourceUri();
        final String uriString = uri != null ? uri.toString() : "";

        DataSubscriber<CloseableReference<CloseableImage>> subscriber = new BaseDataSubscriber<CloseableReference<CloseableImage>>() {
            @Override
            protected void onNewResultImpl(
                    DataSource<CloseableReference<CloseableImage>> dataSource) {
                if (!dataSource.isFinished()) {
                    return;
                }
                CloseableReference<CloseableImage> imageReference = dataSource.getResult();
                Bitmap bitmap = null;
                Bitmap srcBitmap = null;
                if (imageReference != null) {
                    try {
                        CloseableImage image = imageReference.get();
                        srcBitmap = image instanceof CloseableBitmap
                                ? ((CloseableBitmap) image).getUnderlyingBitmap()
                                : null;
                        if (srcBitmap != null) {
                            bitmap = createBitmap(srcBitmap, bitmapConfig);

                            // api19以上有这个方法
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                SunLog.i(TAG,
                                        "getBitmap()-onNewResultImpl()-tag=" + imageTag
                                                + "-bitmap-w=" + bitmap.getWidth() + "-h="
                                                + bitmap.getHeight() + "-size="
                                                + bitmap.getAllocationByteCount());
                            }
                        }
                    } catch (OutOfMemoryError oom) {
                        SunLog.e(TAG, "catch OOM error first ===> tryAgain, error:" + oom);
                        Runtime.getRuntime().gc(); //手动尝试 gc
                        try {
                            // 再次尝试 createBitmap
                            bitmap = createBitmap(srcBitmap, bitmapConfig);
                        } catch (OutOfMemoryError e) {
                            SunLog.e(TAG, "catch OOM error second ==> doNothing, error:" + e);
                        }
                    } finally {
                        CloseableReference.closeSafely(imageReference);
                    }
                }
                callback.onGetBitmap(bitmap);
            }

            @Override
            protected void onFailureImpl(
                    DataSource<CloseableReference<CloseableImage>> dataSource) {
                Throwable throwable = dataSource.getFailureCause();
                if (throwable != null) {
                    String msg = throwable.getMessage();
                    if (FrescoConsts.THROWABLE_UNKNOWN_IMAGE_FORMAT.equals(msg)) {
                        long start = System.currentTimeMillis();
                        clearCache(imageRequest);
                        SunLog.i(TAG, "getBitmap()--onFailureImpl()-clearCache()-tag=" + imageTag
                                + "-ms=" + (System.currentTimeMillis() - start));
                    }
                    SunLog.i(TAG, "getBitmap()-onFailureImpl()-tag=" + imageTag + "-msg=" + msg
                            + "-uri=" + uriString);
                } else {
                    SunLog.i(TAG, "getBitmap()--onFailureImpl()-throwable=null-tag=" + imageTag);
                }

                callback.onGetBitmap(null);
            }
        };

        dataSource.subscribe(subscriber, SunThreadPool.getGlobalSingleThreadPoolInstance());
    }

    private static Bitmap createBitmap(Bitmap srcBitmap, Bitmap.Config bitmapConfig) {
        // canvas复制bitmap
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap bitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(),
                bitmapConfig);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(srcBitmap, 0.0f, 0.0f, paint);
        return bitmap;
    }

    /**
     * 清除指定图片uri的内存和磁盘中的缓存,使用的是默认的CacheKeyFactory<br>
     * 条件：SDKVersion < 1.0.4使用<br>
     *
     * @param uriString The uri of the image to evict
     */
    public static void clearCache(String uriString) {
        ImagePipelineFactory.getInstance().getImagePipeline().evictFromCache(Uri.parse(uriString));
    }

    /**
     * 清除指定图片uri的内存和磁盘中的缓存,自定义的CacheKeyFactory需使用<br>
     * 条件：SDKVersion < 1.0.4使用<br>
     *
     * @param imageRequest The ImageRequest of the image to evict for special uri
     */
    public static void clearCache(final ImageRequest imageRequest) {
        if (imageRequest == null) {
            return;
        }
        final ImagePipeline imagePipeline = ImagePipelineFactory.getInstance().getImagePipeline();
        imagePipeline.evictFromMemoryCache(imageRequest.getSourceUri());
        imagePipeline.evictFromDiskCache(imageRequest);
    }
}
