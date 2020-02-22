package com.sunny.lib.image;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.sunny.family.R;

/**
 * Glide:https://github.com/bumptech/glide
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
                    .placeholder(R.drawable.default_img) // 占位图
                    .circleCrop() // 根据控件大小，裁剪一个圆
//                    .centerCrop() // 完全填充控件，裁剪超出部分
//                    .fitCenter() // // 默认等比例缩小显示
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
    private void GlideDemo(Context context, ImageView imageView) {

        Glide.with(context)
                // 资源类型，默认asDrawable
//                .asBitmap()
//                .asDrawable()
//                .asGif()
//                .asFile()
                .load(NetImgUrl)// 默认指定TranscodeType为Drawable
//                .load(NetGifUrl)
                .apply(getSunGlideRequestOptions()) // 必须在load之后使用
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
