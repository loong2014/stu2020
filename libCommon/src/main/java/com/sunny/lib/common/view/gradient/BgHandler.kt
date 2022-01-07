package com.sunny.lib.common.view.gradient

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import androidx.annotation.RequiresApi
import com.sunny.lib.base.log.SunLog
import com.sunny.lib.base.utils.ContextProvider
import com.sunny.lib.common.R
import com.sunny.lib.common.fresco.FrescoCacheUtil
import java.lang.ref.WeakReference

/**
 * Created by songzhukai on 2020/9/24.
 */
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
class BgHandler(val view: View) : IBgHandler {

    val TAG = "BgHandler"
    val reference: WeakReference<View> = WeakReference(view)
    private var defaultBitmap: Bitmap
    private var currBitmap: Bitmap
    val context: Context
    private val msgHandler: MsgHandler
    private val HANDLE_DELAY_TIME = 500L //切换tab触发延时
    private val TRANSITION_TIME = 500 //背景过渡时间ms
    private var isBindGlobal: Boolean = false;

    init {
        val drawable = view.resources.getDrawable(R.mipmap.layout_bg) as BitmapDrawable
        defaultBitmap = drawable.bitmap
        currBitmap = drawable.bitmap
        context = ContextProvider.appContext
        msgHandler = MsgHandler(this)
    }

    //避免内存泄漏使用 companion object & WeakReference 持有 BgHandler
    companion object {
        class MsgHandler(bgHandler: BgHandler) : Handler(Looper.getMainLooper()) {
            private val mReference: WeakReference<BgHandler> = WeakReference(bgHandler)
            override fun handleMessage(msg: Message) {
                if (mReference.get() == null) {
                    return
                }
                val bgHandler = mReference.get()
                bgHandler!!.realHandle(msg.obj as BgMsg)
            }
        }
    }

    override fun bindGlobalImg(url: String) {
        SunLog.d(TAG, "bindGlobalImg")
        if (isBindGlobal) {
            return
        }
        loadBitmap(url) {
            isBindGlobal = true
            defaultBitmap = it
            handle(BgMsg(Type.Global))
        }
    }

    override fun handle(bgMsg: BgMsg) {
        SunLog.d(TAG, "handle= $bgMsg")
        msgHandler.removeCallbacksAndMessages(null)
        msgHandler.sendMessageDelayed(msgHandler.obtainMessage(0, bgMsg), HANDLE_DELAY_TIME)
    }

    private fun realHandle(bgMsg: BgMsg) {
        SunLog.d(TAG, "realHandle= $bgMsg")
        if (reference.get() == null) {
            SunLog.d(TAG, "reference.get() is null, view is gc, so nothing to do.")
            return
        }
        when (bgMsg.type) {
            Type.Global -> {
                SunLog.d(TAG, "processToDefault")
                processToDefault()
            }
            Type.Single -> {
                SunLog.d(TAG, "processToTarget by url, ${bgMsg.url}")
                processToTarget(bgMsg.url)
            }
        }
    }

    private fun processToTarget(url: String) {
        loadBitmap(url) {
            transitionBitmap(arrayListOf(currBitmap, it))
        }
    }

    private fun loadBitmap(url: String, block: (bitmap: Bitmap) -> Unit) {
        FrescoCacheUtil.getBitmap(
            url,
            "",
            context.resources.getDimension(R.dimen.dimen_1280).toInt(),
            context.resources.getDimension(R.dimen.dimen_720).toInt(),
            { bitmap ->
                if (bitmap != null) {
                    block(bitmap)
                }
            },
            Bitmap.Config.RGB_565
        )
    }

    private fun processToDefault() {
        if (currBitmap == defaultBitmap) {
            return
        }
        transitionBitmap(arrayListOf(currBitmap, defaultBitmap))
    }


    private fun transitionBitmap(bitmaps: ArrayList<Bitmap>) {
        SunLog.d(TAG, "transitionBitmap, bitmaps[0]=${bitmaps[0]}, bitmaps[1]=${bitmaps[1]}")
        view.post(Runnable {
            val drawable1: Drawable = BitmapDrawable(view.context.resources, bitmaps[0])
            val drawable2: Drawable = BitmapDrawable(view.context.resources, bitmaps[1])
            val transitionDrawable = TransitionDrawable(arrayOf(drawable1, drawable2))
            view.background = transitionDrawable
            transitionDrawable.startTransition(TRANSITION_TIME)
            currBitmap = bitmaps[1]
        })
    }
}