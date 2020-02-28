package com.sunny.lib.image;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.stream.HttpGlideUrlLoader;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.sunny.family.R;


/**
 * Glide:https://github.com/bumptech/glide
 * https://muyangmin.github.io/glide-docs-cn/doc/configuration.html
 */
public class ImageConfig {
    public static String NetImgUrl = "https://cn.bing.com/sa/simg/hpb/LaDigue_EN-CA1115245085_1920x1080.jpg";

    public static String NetGifUrl = "https://upfile.asqql.com/2009pasdfasdfic2009s305985-ts/2019-9/20199272112958338.gif";

    private static RequestOptions mSunGlideRequestOptions = null;

//    // Glide 配置集合
//    val requestOptions: RequestOptions = RequestOptions()
//                .placeholder(R.drawable.default_img) // 占位图
//                .circleCrop() // 根据控件大小，裁剪一个圆
//                .centerCrop() // 完全填充控件，裁剪超出部分
//                .fitCenter() // // 默认等比例缩小显示


    public static RequestOptions getSunGlideRequestOptions() {

        if (mSunGlideRequestOptions == null) {
            mSunGlideRequestOptions = new RequestOptions()

                    .placeholder(R.drawable.default_img) // 占位符
                    .error(R.drawable.default_img) // 错误符
//                    .fallback(R.drawable.default_img) // 后备回调符,在请求的url/model为 null 时展示
                    .fallback(new ColorDrawable(Color.RED)) // 后备回调符

//                    .fitCenter() // // 默认等比例缩小显示
                    .circleCrop() // 根据控件大小，裁剪一个圆
//                    .centerCrop() // 完全填充控件，裁剪超出部分

                    // 自定义参数
                    .set(HttpGlideUrlLoader.TIMEOUT, 1000)
//                    .timeout(100) // 超时
//                    .set(Option.memory("key"), "value")

                    // 配置缓存,默认AUTOMATIC
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
            // 跳过缓存
//                    .skipMemoryCache(true)

            // 仅跳过磁盘缓存
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)

            // 仅从缓存加载图片
//                    .onlyRetrieveFromCache(true)

            // 禁用硬件位图
//                    .disallowHardwareConfig()

            ;
        }
        return mSunGlideRequestOptions;
    }

    /**
     * 1. 在 ListView 或 RecyclerView 中加载图片的代码和在单独的 View 中加载完全一样。Glide 已经自动处理了 View 的复用和请求的取消：
     * 2. 对 url 进行 null 检验并不是必须的，如果 url 为 null，Glide 会清空 View 的内容，或者显示 placeholder Drawable 或 fallback Drawable 的内容。
     * 3. Glide 唯一的要求是，对于任何可复用的 View 或 Target ，如果它们在之前的位置上，用 Glide 进行过加载操作，
     * 那么在新的位置上要去执行一个新的加载操作，或调用 clear() API 停止 Glide 的工作。
     * 比如RecyclerView中不需要图片的item确显示了图片，就是因为设置的背景被glide加载后覆盖
     * <p>
     * 4. Glide 提供了一个辅助方法 into(ImageView) ，它接受一个 ImageView 参数并为其请求的资源类型包装了一个合适的 ImageViewTarget：
     */
    private void GlideDemo(Context context, ImageView imageView, String url) {

//        Glide.get(this).bitmapPool

        // 获取缓冲池
        Glide.get(context).getBitmapPool();
        Glide.get(context).getArrayPool();
        Glide.with(context)
                // 资源类型，默认asDrawable
//                .asBitmap()
//                .asDrawable()
//                .asGif()
//                .asFile()
                .load(url)// 默认指定TranscodeType为Drawable
//                .load(NetGifUrl)
                /*
                 * apply() 方法可以被调用多次，因此 RequestOption 可以被组合使用。
                 * 如果 RequestOptions 对象之间存在相互冲突的设置，那么只有最后一个被应用的 RequestOptions 会生效
                 */
                .apply(getSunGlideRequestOptions())

                // 不同于 Glide v3，Glide v4 将不会默认应用交叉淡入或任何其他的过渡效果。每个请求必须手动应用过渡。
                .transition(DrawableTransitionOptions.withCrossFade(3000)) //交叉淡入,时长是3秒,默认是300ø

//                .apply(SunAppGlideModule)


                /*
                 * 缩略图 (Thumbnail) 允许你指定一个 RequestBuilder 以与你的主请求并行启动。
                 * thumbnail() 会在主请求加载过程中展示。如果主请求在缩略图请求之前完成，则缩略图请求中的图像将不会被展示。
                 * thumbnail() API 允许你简单快速地加载图像的低分辨率版本，并且同时加载图像的无损版本，
                 * 这可以减少用户盯着加载指示器 【例如进度条–译者注】 的时间
                 */
                // 1
                .thumbnail(Glide.with(context).load(url)
                        .override(100) // 指定要加载图片的大小
                )
                // 2
                .thumbnail(0.25F) // 缩略图是原图的25%


                /*
                 * 主请求失败时开始新的请求，不响应thumbnail的失败
                 */
                .error(Glide.with(context).load("fallbackUrl"))

                /*
                 * 在Glide中，Transformations 可以获取资源并修改它，然后返回被修改后的资源。
                 * 通常变换操作是用来完成剪裁或对位图应用过滤器，但它也可以用于转换GIF动画，甚至自定义的资源类型。
                 */
                .centerCrop().circleCrop()// 默认变换
//                .transform(new MultiTransformation(new FitCenter(), new CircleCrop()) // 多重变换，传入变换参数的顺序，决定了这些变换的应用顺序
//                .transform(new FitCenter(), new CircleCrop() // 多重变换，可省略掉MultiTransformation

                .transition(DrawableTransitionOptions.withCrossFade(2000)) // 过渡选项
//                .transition(BitmapTransitionOptions.withCrossFade()) // Bitmap 过渡选项

                .into(imageView);


        Glide.with(context)
                .load(NetImgUrl)
                .into(new Target<Drawable>() {
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {

                    }

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void getSize(@NonNull SizeReadyCallback cb) {

                    }

                    @Override
                    public void removeCallback(@NonNull SizeReadyCallback cb) {

                    }

                    @Override
                    public void setRequest(@Nullable Request request) {

                    }

                    @Nullable
                    @Override
                    public Request getRequest() {
                        return null;
                    }

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onStop() {

                    }

                    @Override
                    public void onDestroy() {

                    }
                });
    }
}
