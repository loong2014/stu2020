package com.sunny.module.spa

import android.app.Presentation
import android.content.Context
import android.content.res.AssetFileDescriptor
import android.graphics.SurfaceTexture
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Display
import android.view.Surface
import android.view.TextureView
import androidx.appcompat.widget.AppCompatTextView
import timber.log.Timber

interface PresentationCallBack {
    fun onShown()

    fun onDismiss()

    fun onComplete()
}

class SunnyPresentation(context: Context, display: Display) :
    Presentation(context, display), TextureView.SurfaceTextureListener {

    var mCallback: PresentationCallBack? = null

    fun setCallBack(callBack: PresentationCallBack) {
        this.mCallback = callBack
    }

    private var mTextureView: TextureView? = null
    private var mSurface: Surface? = null
    private var mMediaPlayer: MediaPlayer? = null

    private var mTipView: AppCompatTextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val modeId = display.mode.modeId
//        Timber.i("onCreate modeId = $modeId")
//        window?.attributes = window?.attributes?.apply {
//            preferredDisplayModeId = modeId
//        }
//
//        window?.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)

        setContentView(R.layout.layout_sunny)
        mTextureView = findViewById(R.id.textureView)
        mTextureView?.surfaceTextureListener = this

//        mTipView = findViewById(R.id.msgTip)
//
//        mTipView?.text = "${display.displayId} >>> ${display.name}"
        setOnShowListener {
            mCallback?.onShown()
        }

        setOnDismissListener {
            mCallback?.onShown()
        }
    }

    fun dealStart() {
        Timber.i("dealStart")
        try {
            show()
        } catch (e: Exception) {
            Timber.e("show error :$e")
        }
    }

    fun dealStop() {
        Timber.i("dealStop")
        dismiss()
    }

    private val mCompletionListener = MediaPlayer.OnCompletionListener {
        mMediaPlayer?.start()
        mMediaPlayer?.isLooping = true
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        Timber.i("onSurfaceTextureAvailable($width , $height)")
        mSurface = Surface(surface)

        val afd: AssetFileDescriptor = resources.openRawResourceFd(R.raw.debug_video)

        mMediaPlayer = MediaPlayer()
        mMediaPlayer?.run {
            setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            setAudioStreamType(AudioManager.STREAM_MUSIC)
            setSurface(mSurface)
            prepare()
            isLooping = true
            setOnCompletionListener(mCompletionListener)
            start()
        }
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
        Timber.i("onSurfaceTextureSizeChanged($width , $height)")
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        Timber.i("onSurfaceTextureDestroyed")
        mMediaPlayer?.start()
        mMediaPlayer?.release()
        mMediaPlayer = null
        return true
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
        Timber.i("onSurfaceTextureUpdated")
    }
}