package com.sunny.family.image

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Looper
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.FutureTarget
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.sunny.family.R
import com.sunny.lib.base.BaseActivity
import com.sunny.lib.image.ImageConfig
import com.sunny.lib.utils.HandlerUtils
import com.sunny.lib.utils.SunLog
import kotlinx.android.synthetic.main.act_image.*


class ImageActivity : BaseActivity() {
    val logTag = "Image-ImageActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_image)

        initView()

    }

    private fun initView() {

        btn_cancel_load.setOnClickListener {
            clearGlideView()
        }

        btn_show_net_image.setOnClickListener {
            //            showNetImageOrGifByAnsy(ImageConfig.NetImgUrl, iv_image)
            showNetImageOrGif(ImageConfig.NetImgUrl, iv_image)
        }

        btn_show_net_gif.setOnClickListener {
            //            showNetImageOrGif(ImageConfig.NetGifUrl, iv_image_2)
            Glide.with(this).load(R.raw.large_giphy_logo).into(iv_image_2)
        }
    }

    private fun clearGlideView() {
        // 尽管及时取消不必要的加载是很好的实践，但这并不是必须的操作。
        // 实际上，当 Glide.with() 中传入的 Activity 或 Fragment 实例销毁时，Glide 会自动取消加载并回收资源。
        Glide.with(this).clear(iv_image)
        Glide.with(this).clear(iv_image_2)
    }

    private fun showNetImageOrGif(url: String, view: ImageView) {
        tv_image_path.text = url

        showImageTip("load")

//        val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
//        val factory: DrawableCrossFadeFactory = Builder().setCrossFadeEnabled(true).build()
//
//        GlideApp.with(context)
//                .load(url)
//                .transition(withCrossFade(factory))
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(R.color.placeholder)
//                .into(imageView)
//
        Glide.with(this)

                .load(url)
                .skipMemoryCache(true) // 跳过缓存
//                .fitCenter()
//                .transition(DrawableTransitionOptions.with(factory)) // 过渡选项
//                .transform(MultiTransformation(FitCenter(), CircleCrop())

//                .transform(FillSpaceTransformation() // 多重变换，可省略掉MultiTransformation
//                .transforms(FitCenter(), RoundedCorners(60))
                .transition(DrawableTransitionOptions.withCrossFade(3000)) //交叉淡入，默认非

                .listener(object : RequestListener<Drawable?> {

                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable?>?, isFirstResource: Boolean): Boolean {
                        showImageTip("RequestListener-onLoadFailed")
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable?>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        showImageTip("RequestListener-onResourceReady")

                        // 播放次数
                        if (resource is GifDrawable) {
                            resource.setLoopCount(1)
                        }
                        return false
                    }
                })

//                .thumbnail(Glide.with(this).load(ImageConfig.NetImgUrl))

                .into(view)
//                .into(DrawableImageViewTarget(view))
//                .into(object : CustomTarget<Drawable?>() {
//                    override fun onLoadCleared(placeholder: Drawable?) {
//                        showImageTip("CustomTarget-onLoadCleared")
//                    }
//
//                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
//                        showImageTip("CustomTarget-onResourceReady")
//                        view.setImageDrawable(resource)
//                    }
//                })

    }

    private fun showNetImageOrGifByAnsy(url: String, view: ImageView) {

        HandlerUtils.workHandler.post {

            // 在子线程中加载
            val futureTarget: FutureTarget<Bitmap> =
                    Glide.with(this)
                            .asBitmap()
                            .load(url)
                            .apply(ImageConfig.getSunGlideRequestOptions())
                            .submit(view.width, view.height) // 必须指定高宽

            val bitmap: Bitmap = futureTarget.get()

            // 在主线程中显示
            runOnUiThread {
                view.setImageBitmap(bitmap)
            }
        }

    }

    private fun showImageTip(msg: String, reset: Boolean = false) {

        val isUiThread: Boolean = Thread.currentThread() == Looper.getMainLooper().thread
        SunLog.i(logTag, "$isUiThread : $msg")

//        GlideApp
        if (reset) {
            tv_image_tip.text = ""

        } else {
            var tip = tv_image_tip.text
            tip = "$tip\n$msg"
            tv_image_tip.text = tip
        }
    }

}

